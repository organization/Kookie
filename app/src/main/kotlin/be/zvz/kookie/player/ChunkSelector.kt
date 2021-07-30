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

import be.zvz.kookie.world.World
import kotlin.math.pow

class ChunkSelector {
    fun selectChunks(radius: Int, centerX: Int, centerZ: Int) = sequence {
        val radiusSquared = radius.toDouble().pow(2)

        repeat(radius) { x ->
            repeat(x) { z ->
                if (x.toDouble().pow(2) + z.toDouble().pow(2) <= radiusSquared) {
                    // If the chunk is in the radius, others at the same offsets in different quadrants are also guaranteed to be.

                    /* Top right quadrant */
                    yield(World.chunkHash(centerX + x, centerZ + z))
                    /* Top left quadrant */
                    yield(World.chunkHash(centerX - x - 1, centerZ + z))
                    /* Bottom right quadrant */
                    yield(World.chunkHash(centerX + x, centerZ - z - 1))
                    /* Bottom left quadrant */
                    yield(World.chunkHash(centerX - x - 1, centerZ - z - 1))

                    if (x != z) {
                        /* Top right quadrant mirror */
                        yield(World.chunkHash(centerX + z, centerZ + x))
                        /* Top left quadrant mirror */
                        yield(World.chunkHash(centerX - z - 1, centerZ + x))
                        /* Bottom right quadrant mirror */
                        yield(World.chunkHash(centerX + z, centerZ - x - 1))
                        /* Bottom left quadrant mirror */
                        yield(World.chunkHash(centerX - z - 1, centerZ - x - 1))
                    }
                }
            }
        }
    }
}
