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
package be.zvz.kookie.world.generator.biome

import be.zvz.kookie.world.biome.Biome
import be.zvz.kookie.world.biome.BiomeIds
import be.zvz.kookie.world.biome.BiomeRegistry
import be.zvz.kookie.world.biome.UnknownBiome
import be.zvz.kookie.world.generator.noise.Simplex
import com.koloboke.collect.map.hash.HashIntObjMaps
import kotlin.random.Random

abstract class BiomeSelector(random: Random) {
    private val temperature: Simplex = Simplex(random, 2, 1F / 16, 1F / 512)
    private val rainfall: Simplex = Simplex(random, 2, 1F / 16, 1F / 512)

    private val map = HashIntObjMaps.newMutableMap<Biome>()

    /**
     * Lookup function called by recalculate() to determine the biome to use for this temperature and rainfall.
     *
     * @return int biome ID 0-255
     */
    protected abstract fun lookup(temperature: Float, rainfall: Float): BiomeIds

    fun getTemperature(x: Float, z: Float) = (temperature.noise2D(x, z, true) + 1) / 2
    fun getRainfall(x: Float, z: Float) = (rainfall.noise2D(x, z, true) + 1) / 2

    fun recalculate() {
        val biomeRegistry = BiomeRegistry
        repeat(64) { i ->
            repeat(64) { j ->
                val biome = biomeRegistry.get(this.lookup(i.toFloat() / 63, j.toFloat() / 63).id)
                if (biome is UnknownBiome) {
                    throw RuntimeException("Unknown biome returned by selector with ID ${biome.id}")
                }
                map[i + (j shl 6)] = biome
            }
        }
    }

    fun pickBiome(x: Float, z: Float): Biome {
        val temperature = (getTemperature(x, z) * 63).toInt()
        val rainfall = (getRainfall(x, z) * 63).toInt()

        return map[temperature + (rainfall shl 6)]!!
    }
}
