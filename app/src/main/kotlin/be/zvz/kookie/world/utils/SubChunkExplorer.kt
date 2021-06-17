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
package be.zvz.kookie.world.utils

import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.SubChunk

class SubChunkExplorer(val world: ChunkManager) {
    var currentChunk: Chunk? = null
    var currentSubChunk: SubChunk? = null

    var currentX: Int = 0
    var currentY: Int = 0
    var currentZ: Int = 0

    fun moveTo(x: Int, y: Int, z: Int): Status = moveToChunk(x shr 4, y shr 4, z shr 4)

    fun moveToChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Status {
        var chunk = currentChunk
        if (currentChunk == null || currentX != chunkX || currentZ != chunkZ) {
            currentX = chunkX
            currentZ = chunkZ
            currentSubChunk = null
            chunk = world.getChunk(currentX, currentZ)
            currentChunk = chunk
        }
        return chunk?.let {
            if (currentSubChunk == null || currentY != chunkY) {
                currentY = chunkY
                if (currentY in 0..chunk.height) {
                    currentSubChunk = null
                    Status.INVALID
                } else {
                    currentSubChunk = chunk.getSubChunk(currentY)
                    Status.MOVED
                }
            }
            Status.OK
        } ?: Status.INVALID
    }

    fun isValid() = currentSubChunk != null

    fun invalidate() {
        currentChunk = null
        currentSubChunk = null
    }

    enum class Status(val value: Int) {
        INVALID(0), // We encountered terrain not accessible by the current terrain provider
        OK(1), // We remained inside the same (sub)chunk
        MOVED(2), // We moved to a different (sub)chunk
    }
}
