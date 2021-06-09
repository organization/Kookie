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

import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.generator.objects.Ore
import java.util.Random

class Ore(vararg val oreTypes: Ore.Type) : Populator {
    override fun populate(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random) {
        oreTypes.forEach {
            val ore = Ore(random, it)
            repeat(ore.type.clusterCount) {
                val x = random.nextRange(chunkX shl 4, (chunkX shl 4) + 15)
                val y = random.nextRange(ore.type.minHeight, ore.type.maxHeight)
                val z = random.nextRange(chunkZ shl 4, (chunkZ shl 4) + 15)
                if (ore.canPlaceObject(world, x, y, z)) {
                    ore.placeObject(world, x, y, z)
                }
            }
        }
    }
}

fun Random.nextRange(start: Int, end: Int) = start + (nextInt() % (end + 1 - start))
