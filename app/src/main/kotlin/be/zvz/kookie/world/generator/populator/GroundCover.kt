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
package be.zvz.kookie.world.generator.populator

import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.block.BlockLegacyIds
import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.biome.BiomeRegistry
import java.util.Random
import kotlin.math.min

class GroundCover : Populator {
    override fun populate(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random) {
        val chunk = world.getChunk(chunkX, chunkZ) ?: throw IllegalArgumentException("Chunk $chunkX $chunkZ does not exist")
        repeat(16) { x ->
            repeat(16) { z ->
                val biome = BiomeRegistry.get(chunk.getBiomeId(x, z))
                if (biome.groundCover.isNotEmpty()) {
                    val diffY = if (!biome.groundCover[0].isSolid()) {
                        1
                    } else {
                        0
                    }

                    var startY = 127
                    while (startY > 0 && !BlockFactory.fromFullBlock(chunk.getFullBlock(x, startY, z)).isTransparent()) {
                        --startY
                    }
                    startY = min(127, startY + diffY)
                    val endY = startY - biome.groundCover.size

                    var y = startY
                    while ((y > endY) and (y >= 0)) {
                        --y
                        val coverBlock = biome.groundCover[startY - y]
                        val beforeBlock = BlockFactory.fromFullBlock(chunk.getFullBlock(x, y, z))
                        if ((beforeBlock.getId() == BlockLegacyIds.AIR.id) and coverBlock.isSolid()) {
                            break
                        }
                        if (coverBlock.canBeFlowedInto()) {
                            TODO("Implements after implemented Liquid")
                            // if(beforeBlock is Liquid) continue
                        }

                        chunk.setFullBlock(x, y, z, coverBlock.getFullId())
                    }
                }
            }
        }
    }
}
