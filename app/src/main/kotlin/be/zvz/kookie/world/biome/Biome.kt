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

import be.zvz.kookie.block.Block
import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.generator.populator.Populator
import kotlin.random.Random

@BiomeIdentify(id = BiomeIds.UNKNOWN)
abstract class Biome {
    val identifier = this::class.java.getAnnotation(BiomeIdentify::class.java)!!.id

    var id: Int = identifier.id
        protected set
    var name: String = identifier.biomeName
        protected set

    var populators: MutableList<Populator> = mutableListOf()
        private set

    var minElevation: Int = 0
        private set
    var maxElevation: Int = 0
        private set

    var groundCover: MutableList<Block> = mutableListOf()

    var rainfall: Float = 0.5f
        protected set
    var temperature: Float = 0.5f
        protected set

    fun clearPopulators() = populators.clear()

    fun addPopulator(populator: Populator) = populators.add(populator)

    fun populateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random) {
        populators.forEach {
            it.populate(world, chunkX, chunkZ, random)
        }
    }

    fun setElevation(min: Int, max: Int) {
        minElevation = min
        maxElevation = max
    }

    companion object {
        const val MAX_BIOMES = 256
    }
}
