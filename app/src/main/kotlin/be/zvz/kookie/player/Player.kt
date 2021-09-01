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
package be.zvz.kookie.player

import be.zvz.kookie.Server
import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.crafting.CraftingGrid
import be.zvz.kookie.entity.Human
import be.zvz.kookie.entity.Location
import be.zvz.kookie.entity.Skin
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.network.mcpe.protocol.TextPacket
import be.zvz.kookie.permission.Permission
import be.zvz.kookie.permission.PermissionAttachment
import be.zvz.kookie.permission.PermissionAttachmentInfo
import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.world.ChunkHash
import be.zvz.kookie.world.ChunkListener
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk
import com.koloboke.collect.map.hash.HashLongObjMaps
import com.koloboke.collect.set.hash.HashLongSets
import org.slf4j.LoggerFactory
import kotlin.math.min
import kotlin.math.pow

open class Player(
    override var server: Server,
    val networkSession: NetworkSession,
    playerInfo: PlayerInfo,
    val authenticated: Boolean,
    spawnLocation: Location,
    val namedTag: CompoundTag,
    skin: Skin,
    location: Location
) : Human(skin, location), CommandSender, ChunkListener {
    private val logger = LoggerFactory.getLogger(Player::class.java)

    override val language: Language get() = server.language
    val username = playerInfo.getUsername()
    var displayName = username
    override val name: String get() = username
    override val permissionRecalculationCallbacks: Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>
        get() = TODO("Not yet implemented")
    lateinit var craftingGrid: CraftingGrid
        private set

    val usedChunks: MutableMap<ChunkHash, UsedChunkStatus> = HashLongObjMaps.newMutableMap()
    private val loadQueue: MutableSet<ChunkHash> = HashLongSets.newMutableSet()
    private var nextChunkOrderRun: Int = 5
    private val chunkSelector = ChunkSelector()
    private val chunkLoader = PlayerChunkLoader(spawnLocation)

    private var chunksPerTick: Int = server.configGroup.getProperty("chunk-sending.per-tick").asLong(4).toInt()
    private var spawnThreshold: Int =
        (server.configGroup.getProperty("chunk-sending.spawn-radius").asLong(4).toDouble().pow(2) * Math.PI).toInt()
    private var spawnChunkLoadCount: Int = 0
    var viewDistance: Int = -1
        set(value) {
            field = server.getAllowedViewDistance(value)
            spawnThreshold = min(
                value,
                (server.configGroup.getProperty("chunk-sending.spawn-radius").asLong(4).toDouble().pow(2) * Math.PI).toInt()
            )
            nextChunkOrderRun = 0
            // TODO: networkSession.syncViewAreaRadius(viewDistance)
            logger.debug("Setting view distance to $viewDistance (requested $value)")
        }

    override fun sendMessage(message: String) {
        networkSession.sendDataPacket(TextPacket.raw(message))
    }

    override fun sendMessage(message: TranslationContainer) {
        networkSession.sendDataPacket(TextPacket.translation(message.text, message.params.toMutableList()))
    }

    override fun getScreenLineHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun setScreenLineHeight(height: Int?) {
        TODO("Not yet implemented")
    }

    override fun setBasePermission(name: String, grant: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setBasePermission(permission: Permission, grant: Boolean) {
        TODO("Not yet implemented")
    }

    override fun unsetBasePermission(name: String) {
        TODO("Not yet implemented")
    }

    override fun unsetBasePermission(permission: Permission) {
        TODO("Not yet implemented")
    }

    override fun isPermissionSet(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(permission: Permission): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAttachment(plugin: Plugin, name: String?, value: Boolean?): PermissionAttachment {
        TODO("Not yet implemented")
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        TODO("Not yet implemented")
    }

    override fun recalculatePermissions(): Map<String, Boolean> {
        TODO("Not yet implemented")
    }

    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> {
        TODO("Not yet implemented")
    }

    open fun doChunkRequest() {
        if (nextChunkOrderRun != Int.MAX_VALUE && nextChunkOrderRun-- <= 0) {
            nextChunkOrderRun = Int.MAX_VALUE
            orderChunks()
        }

        if (loadQueue.isNotEmpty()) {
            requestChunks()
        }
    }

    /**
     * Requests chunks from the world to be sent, up to a set limit every tick.
     * This operates on the results of the most recent chunk order.
     */
    private fun requestChunks() {
        /** TODO: if(isConnected) return */

        Timings.playerChunkSend.startTiming()

        var count = 0

        loadQueue.forEach { chunkHash ->
            if (count >= chunksPerTick) {
                return@forEach
            }

            val (X, Z) = World.parseChunkHash(chunkHash)
            ++count

            usedChunks[chunkHash] = UsedChunkStatus.NEEDED
            world.registerChunkLoader(chunkLoader, X, Z, true)
            world.registerChunkListener(this, X, Z)

            val originWorld = world
            world.requestChunkPopulation(X, Z, chunkLoader).onCompletion(
                { chunk ->
                    val usedChunk = usedChunks[chunkHash]
                    if (/* TODO: !isConnected || */ usedChunk == null || world != originWorld) {
                        return@onCompletion
                    }
                    if (usedChunk != UsedChunkStatus.NEEDED) {
                        // TODO: make this an error
                        // we may have added multiple completion handlers, since the Player keeps re-requesting chunks
                        // it doesn't have yet (a relic from the old system, but also currently relied on for chunk resends).
                        // in this event, make sure we don't try to send the chunk multiple times.
                        return@onCompletion
                    }
                    loadQueue.remove(chunkHash)
                    usedChunks[chunkHash] = UsedChunkStatus.REQUESTED

                    /** TODO: Implements after implemented NetworkSession::startUsingChunk
                     * networkSession.startUsingChunk(X, Z) {
                     *     usedChunks[chunkHash] = UsedChunkStatus.SENT
                     *     if (spawnChunkLoadCount == -1) {
                     *         spawnEntitiesOnChunk(X, Z)
                     *     } else if (spawnChunkLoadCount++ == spawnThreshold) {
                     *         spawnChunkLoadCount = -1
                     *
                     *         spawnEntitiesOnAllChunks()
                     *
                     *         networkSession.notifyTerrainReady()
                     *     }
                     * }
                     */
                },
                { }
            )
        }

        Timings.playerChunkSend.stopTiming()
    }

    /**
     * Calculates which new chunks this player needs to use, and which currently-used chunks it needs to stop using.
     * This is based on factors including the player's current render radius and current position.
     */
    private fun orderChunks() {
        if (/* TODO: !isConnected || */ viewDistance == -1) {
            return
        }

        Timings.playerChunkOrder.startTiming()

        loadQueue.clear()
        val unloadChunks = HashLongObjMaps.newMutableMap(usedChunks)

        chunkSelector.selectChunks(viewDistance, location.x.toInt() shr 4, location.z.toInt() shr 4).forEach { chunkHash ->
            val status = usedChunks[chunkHash]?.equals(UsedChunkStatus.NEEDED)
            if (status === null || status == true) {
                loadQueue.add(chunkHash)
            }
            unloadChunks.remove(chunkHash)
        }

        unloadChunks.keys.forEach { chunkHash ->
            val (chunkX, chunkZ) = World.parseChunkHash(chunkHash)
            unloadChunk(chunkX, chunkZ)
        }

        if (loadQueue.isNotEmpty() || unloadChunks.isNotEmpty()) {
            chunkLoader.currentLocation = location
            // TODO: networkSession.syncViewAreaCenterPoint(location, viewDistance)
        }

        Timings.playerChunkOrder.stopTiming()
    }

    private fun unloadChunk(chunkX: Int, chunkZ: Int, world: World = this.world) {
        val chunkHash = World.chunkHash(chunkX, chunkZ)
        if (usedChunks.containsKey(chunkHash)) {
            world.getChunk(chunkX, chunkZ)?.entities?.values?.forEach {
                if (it !== this) {
                    it.despawnFrom(this)
                }
            }
            // stopUsingChunk() always empty method...Why call it?
            // TODO: networkSession.stopUsingChunk(chunkX, chunkZ)
            usedChunks.remove(chunkHash)
        }
        world.unregisterChunkLoader(chunkLoader, chunkX, chunkZ)
        world.unregisterChunkListener(this, chunkX, chunkZ)
        loadQueue.remove(chunkHash)
    }

    private fun spawnEntitiesOnAllChunks() {
        usedChunks.forEach { (chunkHash, status) ->
            if (status == UsedChunkStatus.SENT) {
                val (chunkX, chunkZ) = World.parseChunkHash(chunkHash)
                spawnEntitiesOnChunk(chunkX, chunkZ)
            }
        }
    }

    private fun spawnEntitiesOnChunk(chunkX: Int, chunkZ: Int) {
        world.getChunk(chunkX, chunkZ)?.let {
            it.entities.values.forEach { entity ->
                if (entity !== this && !entity.isFlaggedForDespawn()) {
                    entity.spawnTo(this)
                }
            }
        }
    }

    /**
     * Returns whether the player is using the chunk with the given coordinates,
     *  irrespective of whether the chunk has been sent yet.
     */
    open fun isUsingChunk(chunkX: Int, chunkZ: Int): Boolean = usedChunks.containsKey(World.chunkHash(chunkX, chunkZ))

    /** Returns a usage status of the given chunk, or null if the player is not using the given chunk.  */
    open fun getUsedChunkStatus(chunkX: Int, chunkZ: Int): UsedChunkStatus? = usedChunks[World.chunkHash(chunkX, chunkZ)]

    /** Returns whether the target chunk has been sent to this player. */
    open fun hasReceivedChunk(chunkX: Int, chunkZ: Int): Boolean = getUsedChunkStatus(chunkX, chunkZ) == UsedChunkStatus.SENT

    override fun onChunkChanged(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        if (hasReceivedChunk(chunkX, chunkZ)) {
            usedChunks[World.chunkHash(chunkX, chunkZ)] = UsedChunkStatus.NEEDED
            nextChunkOrderRun = 0
        }
    }

    override fun onChunkUnloaded(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        if (isUsingChunk(chunkX, chunkZ)) {
            logger.debug("Detected forced unload of chunk $chunkX $chunkZ")
            unloadChunk(chunkX, chunkZ)
        }
    }
}
