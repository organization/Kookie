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

import com.koloboke.collect.map.hash.HashIntObjMaps

object BiomeRegistry {
    private val biomes: MutableMap<Int, Biome> = HashIntObjMaps.newMutableMap()

    init {
        register(OceanBiome())
        register(PlainsBiome())
        register(DesertBiome())
        register(MountainsBiome())
        register(ForestBiome())
        register(TaigaBiome())
        register(SwampBiome())
        register(RiverBiome())
        register(HellBiome())
        register(IcePlainsBiome())
        register(SmallMountainsBiome())
        /**
         * TODO: Implements after implemented TreeType
         * val birchForestIdentifier = BiomeIds.BIRCH_FOREST
         * register(ForestBiome(TreeType::BIRCH()).apply{
         *     id = birchForestIdentifier.id
         *     name = birchForestIdentifier.biomeName
         * });
         */
    }

    fun register(biome: Biome) = biomes.put(biome.id, biome)
    fun get(id: Int) = biomes[id] ?: UnknownBiome(id)
}
