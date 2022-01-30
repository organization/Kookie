/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.cache

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.compression.CompressBatchPromise
import be.zvz.kookie.network.mcpe.serializer.ChunkSerializer
import be.zvz.kookie.scheduler.AsyncTask
import be.zvz.kookie.world.ChunkHash
import be.zvz.kookie.world.ChunkListener
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashLongObjMaps
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket
import java.util.concurrent.ExecutionException

/**
 * This class is used by the current MCPE protocol system to store cached chunk packets for fast resending.
 *
 * TODO: make MemoryManager aware of this so the cache can be destroyed when memory is low
 * TODO: this needs a hook for world unloading
 */

typealias WorldId = Int

class ChunkCache(
    private val world: World
) : ChunkListener {
    /** @var CompressBatchPromise[] */
    private val caches: MutableMap<ChunkHash, CompressBatchPromise> = HashLongObjMaps.newMutableMap()
    private var hits: Int = 0
    private var misses: Int = 0

    /** Returns the percentage of requests to the cache which resulted in a cache hit. */
    val hitPercentage: Float
        get() {
            val total = hits + misses

            return if (total > 0) {
                hits.toFloat() / total
            } else {
                0F
            }
        }

    /**
     * Requests asynchronous preparation of the chunk at the given coordinates.
     *
     * @return CompressBatchPromise a promise of resolution which will contain a compressed chunk packet.
     */
    fun request(chunkX: Int, chunkZ: Int): CompressBatchPromise {
        world.registerChunkListener(this, chunkX, chunkZ)
        val chunk = world.getChunk(chunkX, chunkZ)
        if (chunk === null) {
            throw IllegalArgumentException("Cannot request an unloaded chunk")
        }
        val chunkHash = World.chunkHash(chunkX, chunkZ)
        caches[chunkHash]?.let {
            ++hits
            return it
        }

        ++misses

        world.timings.syncChunkSendPrepare.startTiming()
        try {
            val promise = CompressBatchPromise()
            caches[chunkHash] = promise

            val future = world.server.asyncPool.submit(
                AsyncTask(
                    {
                        promise.resolve(
                            LevelChunkPacket().apply {
                                this.chunkX = chunkX
                                this.chunkZ = chunkZ
                                subChunksLength = chunk.subChunks.size
                                data = ChunkSerializer.serialize(chunk)
                                    .toByteArray() // TODO: replace this with Cloudburst Protocol chunk serializer
                                isCachingEnabled = false // TODO: What is this?
                            }
                        )
                    },
                    Unit
                )
            )
            try {
                future?.get()
            } catch (_: InterruptedException) {
                world.logger.error("Failed preparing chunk $chunkX $chunkZ, retrying")
                restartPendingRequest(chunkX, chunkZ)
            } catch (_: ExecutionException) {
                world.logger.error("Failed preparing chunk $chunkX $chunkZ, retrying")
                restartPendingRequest(chunkX, chunkZ)
            }

            return promise
        } finally {
            world.timings.syncChunkSendPrepare.stopTiming()
        }
    }

    private fun destroy(chunkX: Int, chunkZ: Int): Boolean = caches.remove(World.chunkHash(chunkX, chunkZ)) !== null

    /**
     * Restarts an async request for an unresolved chunk.
     *
     * @throws \InvalidArgumentException
     */
    private fun restartPendingRequest(chunkX: Int, chunkZ: Int) {
        val chunkHash = World.chunkHash(chunkX, chunkZ)
        val existing = caches[chunkHash]
        if (existing === null || existing.hasResult()) {
            throw IllegalArgumentException("Restart can only be applied to unresolved promises")
        }
        existing.cancel()
        caches.remove(chunkHash)

        request(chunkX, chunkZ).onResolve(existing.callbacks)
    }

    override fun onChunkChanged(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        val cache = caches[World.chunkHash(chunkX, chunkZ)]
        if (cache !== null) {
            if (!cache.hasResult()) {
                // some requesters are waiting for this chunk, so their request needs to be fulfilled
                restartPendingRequest(chunkX, chunkZ)
            } else {
                // dump the cache, it'll be regenerated the next time it's requested
                destroy(chunkX, chunkZ)
            }
        }
    }

    override fun onBlockChanged(block: Vector3) {
        // FIXME: requesters will still receive this chunk after it's been dropped, but we can't mark this for a simple
        // sync here because it can spam the worker pool
        destroy(block.x.toInt() shr 4, block.z.toInt() shr 4)
    }

    override fun onChunkUnloaded(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        destroy(chunkX, chunkZ)
        world.unregisterChunkListener(this, chunkX, chunkZ)
    }

    companion object {
        /** @var self[][] */
        private val instances: MutableMap<WorldId, MutableList<ChunkCache>> = HashIntObjMaps.newMutableMap()

        /** Fetches the ChunkCache instance for the given world. This lazily creates cache systems as needed. */
        @JvmStatic
        fun getInstance(world: World): ChunkCache {
            val chunkCache = ChunkCache(world)
            instances.getOrPut(world.id) {
                val cacheMap = mutableListOf<ChunkCache>()
                world.addOnUnloadCallback {
                    cacheMap.forEach { cache ->
                        cache.caches.clear()
                    }
                    instances.remove(world.id)
                    world.logger.debug("Destroyed chunk packet caches for world#worldId")
                }
                cacheMap
            }.add(chunkCache)
            world.logger.debug("Created chunk packet cache (world#worldId, compressor#compressorId)")
            return chunkCache
        }
    }
}
