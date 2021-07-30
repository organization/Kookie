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
import kotlin.random.Random

class Ore(vararg oreTypes: Ore.Type) : Populator {
    var oreTypes: List<Ore.Type> = oreTypes.toList()

    override fun populate(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random) {
        oreTypes.forEach {
            val ore = Ore(random, it)
            repeat(ore.type.clusterCount) {
                val x = chunkX shl 4 + Random.nextInt(0, 15)
                val y = random.nextInt(ore.type.minHeight, ore.type.maxHeight)
                val z = chunkZ shl 4 + random.nextInt(0, 15)
                if (ore.canPlaceObject(world, x, y, z)) {
                    ore.placeObject(world, x, y, z)
                }
            }
        }
    }
}
