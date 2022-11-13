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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.world.generator.objects

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.world.BlockTransaction
import be.zvz.kookie.world.ChunkManager
import kotlin.math.abs
import kotlin.random.Random

class SpruceTree @JvmOverloads constructor(
    val superBirch: Boolean = false
) : Tree(
    VanillaBlocks.SPRUCE_LOG.block,
    VanillaBlocks.SPRUCE_LEAVES.block,
    10
) {
    override fun generateChunkHeight(random: Random): Int =
        treeHeight - random.nextInt(3)

    override fun placeObject(world: ChunkManager, x: Int, y: Int, z: Int, random: Random) {
        treeHeight = random.nextInt(4) + 6
        super.placeObject(world, x, y, z, random)
    }

    override fun placeCanopy(x: Int, y: Int, z: Int, random: Random, transaction: BlockTransaction) {
        val topSize = treeHeight - (1 + random.nextInt(2))
        val lRadius = 2 + random.nextInt(2)
        var radius = random.nextInt(2)
        var maxR = 1
        var minR = 0

        repeat(topSize) { yy ->
            val yyy = y + treeHeight - yy
            for (xx in x - radius..x + radius) {
                val xOff = abs(xx - x)
                for (zz in z - radius..z + radius) {
                    val zOff = abs(zz - z)
                    if (xOff == radius && zOff == radius && radius > 0) {
                        continue
                    }

                    if (!transaction.fetchBlockAt(xx, yyy, zz).isSolid()) {
                        transaction.addBlockAt(xx, yyy, zz, leafBlock)
                    }
                }
            }

            if (radius >= maxR) {
                radius = minR
                minR = 1
                if (++maxR > lRadius) {
                    maxR = lRadius
                }
            } else {
                ++radius
            }
        }
    }
}
