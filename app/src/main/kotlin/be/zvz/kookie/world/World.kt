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

import be.zvz.kookie.Server
import be.zvz.kookie.block.Block
import be.zvz.kookie.block.tile.Tile
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.math.Morton2D
import be.zvz.kookie.math.Morton3D
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player
import be.zvz.kookie.world.format.Chunk
import com.koloboke.collect.map.hash.HashIntObjMaps

class World(val server: Server, val folderName: String) : ChunkManager {
    val players: MutableList<Player> = mutableListOf()
    val entities: MutableList<Entity> = mutableListOf()
    private val entityLastKnownPositions: MutableMap<Int, Vector3> = HashIntObjMaps.newMutableMap()
    private val updateEntities: MutableList<Entity> = mutableListOf()
    private var blockCache: MutableMap<Int, MutableMap<Int, Block>> = HashIntObjMaps.newMutableMap()
    private var sendTimeTicker: Int = 0
    val worldId: Int = worldIdCounter++
    var autoSave: Boolean = true

    var closed: Boolean = false
        private set

    fun getOrLoadChunkAtPosition(pos: Vector3): Chunk? {
        TODO("Chunk not yet implemented")
    }

    fun getTile(pos: Vector3): Tile? {
        TODO("World not yet implemented")
    }

    fun addTile(tile: Tile) {
        TODO("World not yet implemented")
    }

    fun getBlock(pos: Vector3): Block {
        TODO("Chunk not yet implemented")
    }

    fun setBlock(pos: Vector3, block: Block): Any {
        TODO("Chunk not yet implemented")
    }

    override fun getBlockAt(x: Int, y: Int, z: Int): Block {
        TODO("Not yet implemented")
    }

    override fun setBlockAt(x: Int, y: Int, z: Int, block: Block) {
        TODO("Not yet implemented")
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): Chunk? {
        TODO("Not yet implemented")
    }

    override fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        TODO("Not yet implemented")
    }

    override fun getMinY(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxY(): Int {
        TODO("Not yet implemented")
    }

    override fun isInWorld(x: Int, y: Int, z: Int): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        const val DIFFICULTY_PEACEFUL = 0
        const val DIFFICULTY_EASY = 1
        const val DIFFICULTY_NORMAL = 2
        const val DIFFICULTY_HARD = 3

        const val Y_MAX = 256
        const val Y_MIN = 0

        const val TIME_DAY = 1000
        const val TIME_NOON = 6000
        const val TIME_SUNSET = 12000
        const val TIME_NIGHT = 13000
        const val TIME_MIDNIGHT = 18000
        const val TIME_SUNRISE = 23000

        private var worldIdCounter = 0

        private const val MORTON3D_BIT_SIZE = 21
        private const val BLOCKHASH_Y_BITS = 9
        private const val BLOCKHASH_Y_PADDING = 128 // size (in blocks) of padding after both boundaries of the Y axis
        private const val BLOCKHASH_Y_OFFSET = BLOCKHASH_Y_PADDING - Y_MIN
        private const val BLOCKHASH_Y_MASK = (1 shl BLOCKHASH_Y_BITS) - 1
        private const val BLOCKHASH_XZ_MASK = (1 shl MORTON3D_BIT_SIZE) - 1
        private const val BLOCKHASH_XZ_EXTRA_BITS = 6
        private const val BLOCKHASH_XZ_EXTRA_MASK = (1 shl BLOCKHASH_XZ_EXTRA_BITS) - 1
        private const val BLOCKHASH_XZ_SIGN_SHIFT = 64 - MORTON3D_BIT_SIZE - BLOCKHASH_XZ_EXTRA_BITS
        private const val BLOCKHASH_X_SHIFT = BLOCKHASH_Y_BITS
        private const val BLOCKHASH_Z_SHIFT = BLOCKHASH_X_SHIFT + BLOCKHASH_XZ_EXTRA_BITS

        fun blockHash(x: Int, y: Int, z: Int): Long {
            val shiftedY = y + BLOCKHASH_Y_OFFSET
            if (shiftedY and (0.inv() shl BLOCKHASH_Y_BITS) != 0) {
                throw IllegalArgumentException("Y coordinate $y is out of range!")
            }

            // morton3d gives us 21 bits on each axis, but the Y axis only requires 9
            // so we use the extra space on Y (12 bits) and add 6 extra bits from X and Z instead.
            // if we ever need more space for Y (e.g. due to expansion), take bits from X/Z to compensate.
            return Morton3D.encode(
                x = x and BLOCKHASH_XZ_MASK,
                y = shiftedY or
                    x shr MORTON3D_BIT_SIZE and BLOCKHASH_XZ_EXTRA_MASK shl BLOCKHASH_X_SHIFT or
                    z shr MORTON3D_BIT_SIZE and BLOCKHASH_XZ_EXTRA_MASK shl BLOCKHASH_Z_SHIFT,
                z = z and BLOCKHASH_XZ_MASK
            )
        }

        fun chunkHash(x: Int, z: Int): Long = Morton2D.encode(x, z)

        /** Computes a small index relative to chunk base from the given coordinates. */
        fun chunkBlockHash(x: Int, y: Int, z: Int): Long = Morton3D.encode(x, y, z)

        fun getBlockXYZ(hash: Long): Triple<Int, Int, Int> {
            val (baseX, baseY, baseZ) = Morton3D.decode(hash)

            val extraX = baseY shr BLOCKHASH_X_SHIFT and BLOCKHASH_XZ_EXTRA_MASK shl MORTON3D_BIT_SIZE
            val extraZ = baseY shr BLOCKHASH_Z_SHIFT and BLOCKHASH_XZ_EXTRA_MASK shl MORTON3D_BIT_SIZE

            return Triple(
                baseX and BLOCKHASH_XZ_MASK or extraX shl BLOCKHASH_XZ_SIGN_SHIFT shr BLOCKHASH_XZ_SIGN_SHIFT,
                baseY and BLOCKHASH_Y_MASK - BLOCKHASH_Y_OFFSET,
                baseZ and BLOCKHASH_XZ_MASK or extraZ shl BLOCKHASH_XZ_SIGN_SHIFT shr BLOCKHASH_XZ_SIGN_SHIFT
            )
        }

        fun getXZ(hash: Long): Pair<Int, Int> = Morton2D.decode(hash)

        fun getDifficultyFromString(str: String): Int = when (str.trim().lowercase()) {
            "0", "peaceful", "p" -> DIFFICULTY_PEACEFUL
            "1", "easy", "e" -> DIFFICULTY_EASY
            "2", "normal", "n" -> DIFFICULTY_NORMAL
            "3", "hard", "h" -> DIFFICULTY_HARD
            else -> -1
        }
    }
}
