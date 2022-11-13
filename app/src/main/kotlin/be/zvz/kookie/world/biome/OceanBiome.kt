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
package be.zvz.kookie.world.biome

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.world.generator.populator.TallGrass

@BiomeIdentify(id = BiomeIds.OCEAN)
class OceanBiome : Biome() {
    init {
        groundCover.apply {
            add(VanillaBlocks.GRAVEL.block)
            add(VanillaBlocks.GRAVEL.block)
            add(VanillaBlocks.GRAVEL.block)
            add(VanillaBlocks.GRAVEL.block)
            add(VanillaBlocks.GRAVEL.block)
        }

        addPopulator(TallGrass(5))

        setElevation(46, 58)
        temperature = 0.5F
        rainfall = 0.5F
    }
}
