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
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.world.format.Chunk

class World(val server: Server, val folderName: String) {
    var closed: Boolean = false
        private set

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
    }

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
}
