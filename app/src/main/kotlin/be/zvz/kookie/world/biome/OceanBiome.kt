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

@BiomeIdentify(id = BiomeIds.OCEAN)
class OceanBiome() : Biome() {
    init {
        groundCover.apply {
            /**
             * TODO: Implements after implemented VanillaBlocks
             * add(VanillaBlocks.GRAVEL())
             * add(VanillaBlocks.GRAVEL())
             * add(VanillaBlocks.GRAVEL())
             * add(VanillaBlocks.GRAVEL())
             * add(VanillaBlocks.GRAVEL())
             */
        }
        /**
         * TODO: Implements after implemented populator/TallGrass
         * addPopulator(TallGrass().apply{ baseAmount = 5 });
         */

        setElevation(46, 58)
        temperature = 0.5F
        rainfall = 0.5F
    }
}