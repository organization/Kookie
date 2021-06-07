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
package be.zvz.kookie.world.format

import be.zvz.kookie.block.BlockLegacyIds
import be.zvz.kookie.block.tile.Tile
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.world.biome.BiomeIds

class Chunk(
    val subChunks: MutableList<SubChunk> = MutableList(MAX_SUBCHUNKS) { SubChunk(BlockLegacyIds.AIR.id.toLong() shl 4, mutableListOf()) },
    val NBTentities: MutableList<Entity> = mutableListOf(),
    val NBTtiles: MutableList<Tile> = mutableListOf(),
    val biomeIds: BiomeArray = BiomeArray.fill(BiomeIds.OCEAN.id),
    val heightMap: HeightArray = HeightArray.fill(subChunks.size * 16)
) {
    private var dirtyFlags: Int = 0

    fun getHeight(): Int = subChunks.size

    fun getFullBlock(x: Int, y: Int, z: Int): Long {
        return getSubChunk(y shr 4).getFullBlock(x, y and 0xf, z)
    }

    fun getSubChunk(y: Int): SubChunk {
        if (y !in 0 until subChunks.size) {
            throw IllegalArgumentException("Invalid subchunk Y coordinate $y")
        }

        return subChunks[y]
    }

    fun setFullBlock(x: Int, y: Int, z: Int, block: Long) {
        getSubChunk(y shr 4).setFullBlock(x, y and 0xf, z, block)
        dirtyFlags = dirtyFlags or DIRTY_FLAG_TERRAIN
    }

    companion object {
        const val DIRTY_FLAG_TERRAIN = 1 shl 0
        const val DIRTY_FLAG_ENTITIES = 1 shl 1
        const val DIRTY_FLAG_TILES = 1 shl 2
        const val DIRTY_FLAG_BIOMES = 1 shl 3

        const val MAX_SUBCHUNKS = 16
    }
}
