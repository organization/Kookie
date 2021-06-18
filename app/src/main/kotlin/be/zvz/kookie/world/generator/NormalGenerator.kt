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
package be.zvz.kookie.world.generator

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.World
import be.zvz.kookie.world.biome.Biome
import be.zvz.kookie.world.biome.BiomeIds
import be.zvz.kookie.world.generator.biome.BiomeSelector
import be.zvz.kookie.world.generator.noise.Simplex
import be.zvz.kookie.world.generator.objects.Ore
import be.zvz.kookie.world.generator.populator.GroundCover
import be.zvz.kookie.world.generator.populator.Populator
import com.koloboke.collect.map.hash.HashLongObjMaps
import java.util.Random
import be.zvz.kookie.world.generator.populator.Ore as OrePopulator

class NormalGenerator(seed: Long, preset: String) : Generator(seed, preset) {
    val populators: List<Populator>
    val generationPopulators: MutableList<Populator> = mutableListOf()
    val waterHeight = 62

    private val noiseBase: Simplex = Simplex(random, 4, 1F / 4, 1F / 32)
    private val selector: BiomeSelector = NormalBiomeSelector(random)
    private val gaussian: Gaussian = Gaussian(2)

    init {
        random.setSeed(seed)
        selector.recalculate()

        val cover = GroundCover()
        generationPopulators.add(cover)

        val stone = VanillaBlocks.STONE.block
        populators = listOf(
            OrePopulator(
                Ore.Type(VanillaBlocks.COAL_ORE.block, stone, 20, 16, 0, 128),
                Ore.Type(VanillaBlocks.IRON_ORE.block, stone, 20, 8, 0, 64),
                Ore.Type(VanillaBlocks.REDSTONE_ORE.block, stone, 8, 7, 0, 16),
                Ore.Type(VanillaBlocks.LAPIS_LAZULI_ORE.block, stone, 1, 6, 0, 32),
                Ore.Type(VanillaBlocks.GOLD_ORE.block, stone, 2, 8, 0, 32),
                Ore.Type(VanillaBlocks.DIAMOND_ORE.block, stone, 1, 7, 0, 16),
                Ore.Type(VanillaBlocks.DIRT.block, stone, 20, 32, 0, 128),
                Ore.Type(VanillaBlocks.GRAVEL.block, stone, 10, 16, 0, 128)
            )
        )
    }

    private fun pickBiome(x: Int, z: Int): Biome {
        var hash = x * (2345803L xor z.toLong()) * (9236449L xor seed)
        hash *= hash + 223
        var xNoise = hash shr 20 and 3
        var zNoise = hash shr 22 and 3
        if (xNoise == 3L) {
            xNoise = 1
        }
        if (zNoise == 3L) {
            zNoise = 1
        }

        return selector.pickBiome((x + xNoise - 1).toFloat(), (z + zNoise - 1).toFloat())
    }

    override fun generateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int) {
        random.setSeed(0xdeadbeefL xor (chunkX.toLong() shl 8) xor chunkZ.toLong() xor seed)
        val noise = noiseBase.getFastNoise3D(chunkX * 16, 0, chunkZ * 16)
        val chunk = world.getChunk(chunkX, chunkZ) ?: throw IllegalArgumentException("Chunk $chunkX $chunkZ does not exist")
        val biomeCache = HashLongObjMaps.newMutableMap<Biome>()

        val bedrock = VanillaBlocks.BEDROCK.block.getFullId()
        val stillWater = VanillaBlocks.WATER.block.getFullId()
        val stone = VanillaBlocks.STONE.block.getFullId()

        val baseX = chunkX * 16
        val baseZ = chunkZ * 16
        repeat(16) { x ->
            val absoluteX = baseX + x
            repeat(16) { z ->
                val absoluteZ = baseZ + z
                var minSum = 0
                var maxSum = 0
                var weightSum = 0

                val biome = pickBiome(absoluteX, absoluteZ)
                chunk.setBiomeId(x, z, biome.id)

                for (sx in -gaussian.smoothSize..gaussian.smoothSize) {
                    for (sz in -gaussian.smoothSize..gaussian.smoothSize) {
                        val weight = gaussian.kernel.getValue(sx + gaussian.smoothSize).getValue(sz + gaussian.smoothSize).toInt()
                        val adjacent = if (sx == 0 && sz == 0) {
                            biome
                        } else {
                            val index = World.chunkHash(absoluteX + sx, absoluteZ + sz)
                            biomeCache.getOrPut(index) { pickBiome(absoluteX + sx, absoluteZ + sz) }
                        }

                        minSum += (adjacent.minElevation - 1) * weight
                        maxSum += adjacent.maxElevation * weight
                        weightSum += weight
                    }
                }

                minSum /= weightSum
                maxSum /= weightSum

                val smoothHeight = (maxSum - minSum) / 2

                repeat(128) { y ->
                    if (y == 0) {
                        chunk.setFullBlock(x, y, z, bedrock)
                    } else {
                        val noiseValue = noise[x][z][y] - 1 / smoothHeight * (y - smoothHeight - minSum)
                        if (noiseValue > 0) {
                            chunk.setFullBlock(x, y, z, stone)
                        } else if (y <= waterHeight) {
                            chunk.setFullBlock(x, y, z, stillWater)
                        }
                    }
                }
            }
        }

        generationPopulators.forEach { it.populate(world, chunkX, chunkZ, random) }
    }

    override fun populateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int) {
        TODO("Not yet implemented")
    }

    class NormalBiomeSelector(random: Random) : BiomeSelector(random) {
        override fun lookup(temperature: Float, rainfall: Float): BiomeIds = when {
            rainfall < 0.25 -> when {
                temperature < 0.7 -> BiomeIds.OCEAN
                temperature < 0.85 -> BiomeIds.RIVER
                else -> BiomeIds.SWAMP
            }
            rainfall < 0.60 -> when {
                temperature < 0.25 -> BiomeIds.ICE_PLAINS
                temperature < 0.75 -> BiomeIds.PLAINS
                else -> BiomeIds.DESERT
            }
            rainfall < 0.80 -> when {
                temperature < 0.25 -> BiomeIds.TAIGA
                temperature < 0.75 -> BiomeIds.FOREST
                else -> BiomeIds.BIRCH_FOREST
            }
            else -> when {
                temperature < 0.20 -> BiomeIds.MOUNTAINS
                temperature < 0.40 -> BiomeIds.SMALL_MOUNTAINS
                else -> BiomeIds.RIVER
            }
        }
    }
}
