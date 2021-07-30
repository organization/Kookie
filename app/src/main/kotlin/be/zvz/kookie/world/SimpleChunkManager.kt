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
package be.zvz.kookie.world

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.world.format.Chunk
import com.koloboke.collect.map.hash.HashLongObjMaps

open class SimpleChunkManager(
    override var minY: Int,
    override var maxY: Int
) : ChunkManager {
    protected val chunks: MutableMap<Long, Chunk> = HashLongObjMaps.newMutableMap()

    override fun getBlockAt(x: Int, y: Int, z: Int): Block {
        if (isInWorld(x, y, z)) {
            getChunk(x shr 4, z shr 4)?.let { chunk ->
                return BlockFactory.fromFullBlock(chunk.getFullBlock(x and 0xf, y, z and 0xf))
            }
        }
        return VanillaBlocks.AIR.block
    }

    override fun setBlockAt(x: Int, y: Int, z: Int, block: Block) {
        val chunk = getChunk(x shr 4, z shr 4)
        if (chunk === null) {
            throw IllegalArgumentException(
                "Cannot set block at coordinates x=$x,y=$y,z=$z," +
                    "terrain is not loaded or out of bounds"
            )
        }
        chunk.setFullBlock(x and 0xf, y, z and 0xf, block.getFullId())
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): Chunk? = chunks[World.chunkHash(chunkX, chunkZ)]

    override fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        chunks[World.chunkHash(chunkX, chunkZ)] = chunk
    }

    fun cleanChunks() {
        chunks.clear()
    }

    /**
     * Do not check x and z are within the range of Int.
     * Because the Int type guarantees it.
     */
    override fun isInWorld(x: Int, y: Int, z: Int): Boolean = y in minY..maxY
    fun isInWorld(y: Int): Boolean = y in minY..maxY
}
