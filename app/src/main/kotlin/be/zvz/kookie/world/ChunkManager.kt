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
import be.zvz.kookie.world.format.Chunk

interface ChunkManager {
    val minY: Int
    val maxY: Int

    fun getBlockAt(x: Int, y: Int, z: Int): Block
    fun setBlockAt(x: Int, y: Int, z: Int, block: Block)

    fun getChunk(chunkX: Int, chunkZ: Int): Chunk?
    fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk)

    fun isInWorld(x: Int, y: Int, z: Int): Boolean
}
