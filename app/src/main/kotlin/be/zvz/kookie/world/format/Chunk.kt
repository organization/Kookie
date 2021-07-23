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
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.player.Player
import be.zvz.kookie.world.biome.BiomeIds
import com.koloboke.collect.map.hash.HashIntObjMaps

class Chunk @JvmOverloads constructor(
    val subChunks: MutableList<SubChunk> = MutableList(MAX_SUBCHUNKS) {
        SubChunk(BlockLegacyIds.AIR.id.toLong() shl 4, mutableListOf())
    },
    _NBTentities: List<CompoundTag>? = null,
    _NBTtiles: List<CompoundTag>? = null,
    val biomeIds: BiomeArray = BiomeArray.fill(BiomeIds.OCEAN.id),
    var heightMap: HeightArray = HeightArray.fill(subChunks.size * 16)
) : Cloneable {
    var dirtyFlags: Int = 0
    val entities: MutableMap<Int, Entity> = HashIntObjMaps.newMutableMap()
    val tiles: MutableMap<Int, Tile> = HashIntObjMaps.newMutableMap()

    var lightPopulated: Boolean? = false
    var terrainPopulated: Boolean = false

    val height: Int get() = subChunks.size

    private var _NBTentities: List<CompoundTag>? = _NBTentities
    val NBTentities: List<CompoundTag> get() = _NBTentities ?: savableEntities.values.map { it.saveNBT() }

    private var _NBTtiles: List<CompoundTag>? = _NBTtiles
    val NBTtiles: List<CompoundTag> get() = _NBTtiles ?: tiles.values.map { it.saveNBT() }

    val savableEntities
        get() = entities.filter {
            TODO("Implements after implemented Entity::canSaveWithChunk()")
        }

    val isDirty: Boolean get() = dirtyFlags != 0 || tiles.isNotEmpty() or savableEntities.isNotEmpty()

    fun getFullBlock(x: Int, y: Int, z: Int): Long {
        return getSubChunk(y shr 4).getFullBlock(x, y and 0xf, z)
    }

    fun setFullBlock(x: Int, y: Int, z: Int, block: Long) {
        getSubChunk(y shr 4).setFullBlock(x, y and 0xf, z, block)
        dirtyFlags = dirtyFlags or DIRTY_FLAG_TERRAIN
    }

    fun getHighestBlockAt(x: Int, z: Int): Int? {
        for (y in subChunks.size - 1 downTo 0) {
            val height = getSubChunk(y).getHighestBlockAt(x, z)
            if (height != null) {
                return height or (y shl 4)
            }
        }

        return null
    }

    fun getHeightMap(x: Int, z: Int): Int = heightMap[x, z]
    fun setHeightMap(x: Int, z: Int, value: Int) {
        heightMap[x, z] = value
    }

    fun getBiomeId(x: Int, z: Int): Int = biomeIds[x, z]
    fun setBiomeId(x: Int, z: Int, value: Int) {
        biomeIds[x, z] = value
    }

    fun addEntity(entity: Entity) {
        TODO("Implements after implemented Entity::isClosed()")
    }

    fun removeEntity(entity: Entity) {
        TODO("Implements after implemented Entity::isClosed()")
    }

    fun addTile(tile: Tile) {
        if (tile.closed) {
            throw IllegalArgumentException("Attempted to add a garbage closed Tile to a chunk")
        }

        val index = blockHash(tile.pos)
        if (tiles[index] != null && tiles[index] !== tile) {
            throw IllegalArgumentException("Another tile is already at this location")
        }

        tiles[index] = tile
        setDirtyFlag(DIRTY_FLAG_TILES, true)
    }

    fun removeTile(tile: Tile) {
        val index = blockHash(tile.pos)
        if (tiles[index] != null) {
            tiles.remove(index)
            setDirtyFlag(DIRTY_FLAG_TILES, true)
        }
    }

    fun getTile(pos: Vector3) = getTile(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
    fun getTile(x: Int, y: Int, z: Int) = tiles[blockHash(x, y, z)]

    fun onUnload() {
        entities.values.forEach { entity ->
            if (entity !is Player) {
                TODO("Implements after implemented Entity::close()")
            }
        }
        tiles.values.forEach(Tile::close)
    }

    fun getDirtyFlag(flag: Int) = dirtyFlags and flag != 0
    fun setDirtyFlag(flag: Int, value: Boolean) {
        dirtyFlags = if (value) {
            dirtyFlags or flag
        } else {
            dirtyFlags and flag.inv()
        }
    }

    fun getSubChunk(y: Int) =
        if (y !in 0 until subChunks.size) {
            throw IllegalArgumentException("Invalid subchunk Y coordinate $y")
        } else {
            subChunks[y]
        }

    fun setSubChunk(y: Int, subChunk: SubChunk?) {
        if (y !in 0 until subChunks.size) {
            throw IllegalArgumentException("Invalid subchunk Y coordinate $y")
        } else {
            subChunks[y] = subChunk ?: SubChunk(BlockLegacyIds.AIR.id.toLong() shl 4, mutableListOf())
            setDirtyFlag(DIRTY_FLAG_TERRAIN, true)
        }
    }

    fun clearDirtyFlags() {
        dirtyFlags = 0
    }

    public override fun clone() = Chunk(
        subChunks = subChunks.map(SubChunk::clone).toMutableList(),
        // we don't bother cloning entities or tiles since it's impractical to do so (too many dependencies)
        biomeIds = biomeIds.clone(),
        heightMap = heightMap.clone()
    )

    companion object {
        const val DIRTY_FLAG_TERRAIN = 1 shl 0
        const val DIRTY_FLAG_ENTITIES = 1 shl 1
        const val DIRTY_FLAG_TILES = 1 shl 2
        const val DIRTY_FLAG_BIOMES = 1 shl 3

        const val MAX_SUBCHUNKS = 16

        fun blockHash(pos: Vector3) = blockHash(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
        fun blockHash(x: Int, y: Int, z: Int) = y shl 8 or (z and 0x0f shl 4) or (x and 0x0f)
    }
}
