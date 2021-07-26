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

import be.zvz.kookie.block.BlockLegacyIds
import be.zvz.kookie.block.util.TreeType
import be.zvz.kookie.world.ChunkManager
import kotlin.random.Random
import be.zvz.kookie.world.generator.objects.Tree as ObjectTree

class Tree @JvmOverloads constructor(
    var type: TreeType = TreeType.OAK,
    var baseAmount: Int = 1,
    var randomAmount: Int = 1
) : Populator {
    override fun populate(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random) {
        val amount = random.nextInt(0, randomAmount) + baseAmount

        repeat(amount) { i ->
            val x = chunkX * 16 + random.nextInt(0, 15)
            val z = chunkX * 16 + random.nextInt(0, 15)
            val y = getHighestWorkableBlock(world, x, z)

            if (y != -1) {
                ObjectTree.growTree(world, x, y, z, random, type)
            }
        }
    }

    private fun getHighestWorkableBlock(world: ChunkManager, x: Int, z: Int): Int {
        for (y in 127 downTo 0) {
            val id = world.getBlockAt(x, y, z).getId()
            when {
                id != BlockLegacyIds.AIR.id &&
                    id != BlockLegacyIds.LEAVES.id &&
                    id != BlockLegacyIds.LEAVES2.id &&
                    id != BlockLegacyIds.SNOW_LAYER.id -> {
                    return y + 1
                }
            }
        }

        return -1
    }
}
