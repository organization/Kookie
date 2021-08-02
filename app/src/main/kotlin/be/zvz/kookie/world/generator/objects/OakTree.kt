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
package be.zvz.kookie.world.generator.objects

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.world.ChunkManager
import kotlin.random.Random

class OakTree @JvmOverloads constructor(
    val superBirch: Boolean = false
) : Tree(
    VanillaBlocks.OAK_LOG.block,
    VanillaBlocks.OAK_LEAVES.block
) {
    override fun placeObject(world: ChunkManager, x: Int, y: Int, z: Int, random: Random) {
        treeHeight = random.nextInt(3) + 4
        super.placeObject(world, x, y, z, random)
    }
}
