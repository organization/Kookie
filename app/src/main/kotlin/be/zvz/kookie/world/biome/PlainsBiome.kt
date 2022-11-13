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

import be.zvz.kookie.world.generator.populator.TallGrass

@BiomeIdentify(id = BiomeIds.PLAINS)
class PlainsBiome : GrassyBiome() {
    init {
        addPopulator(TallGrass(12))

        setElevation(63, 68)
        temperature = 0.48F
        rainfall = 0.4F
    }
}
