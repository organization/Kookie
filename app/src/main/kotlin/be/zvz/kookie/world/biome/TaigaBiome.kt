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

@BiomeIdentify(id = BiomeIds.TAIGA)
class TaigaBiome : SnowyBiome() {
    init {
        addPopulator(Tree(TreeType.SPRUCE, 10))
        addPopulator(TallGrass(1))

        setElevation(63, 81)
        temperature = 0.05F
        rainfall = 0.8F
    }
}
