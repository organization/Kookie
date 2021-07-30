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
package be.zvz.kookie.world.biome

import be.zvz.kookie.block.util.TreeType
import be.zvz.kookie.world.generator.populator.TallGrass
import be.zvz.kookie.world.generator.populator.Tree

@BiomeIdentify(id = BiomeIds.FOREST)
open class ForestBiome(private val type: TreeType = TreeType.OAK) : GrassyBiome() {
    init {
        addPopulator(Tree(type, 5))
        addPopulator(TallGrass(3))
        setElevation(63, 81)

        if (type == TreeType.BIRCH) {
            temperature = 0.6F
            rainfall = 0.5F
        } else {
            temperature = 0.7F
            rainfall = 0.8F
        }

        id = BiomeIds.BIRCH_FOREST.id
        name = BiomeIds.BIRCH_FOREST.biomeName
    }
}
