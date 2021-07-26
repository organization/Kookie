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

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.world.generator.populator.Ore
import be.zvz.kookie.world.generator.populator.TallGrass
import be.zvz.kookie.world.generator.populator.Tree
import be.zvz.kookie.world.generator.objects.Ore.Type as OreType

@BiomeIdentify(id = BiomeIds.MOUNTAINS)
open class MountainsBiome : GrassyBiome() {
    init {
        addPopulator(Tree(baseAmount = 1))
        addPopulator(TallGrass(1))
        addPopulator(Ore(OreType(VanillaBlocks.EMERALD_ORE.block, VanillaBlocks.STONE.block, 11, 1, 0, 32)))

        setElevation(63, 127)
        temperature = 0.4F
        rainfall = 0.5F
    }
}
