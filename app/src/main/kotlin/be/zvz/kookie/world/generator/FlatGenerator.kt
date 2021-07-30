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
import be.zvz.kookie.item.LegacyStringToItemParser
import be.zvz.kookie.utils.inline.repeat2
import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.generator.objects.Ore
import be.zvz.kookie.world.generator.populator.Populator
import com.koloboke.collect.map.hash.HashObjObjMaps
import kotlin.random.Random
import be.zvz.kookie.world.generator.populator.Ore as OrePopulator

open class FlatGenerator(seed: Long, preset: String) :
    Generator(seed, preset.takeIf(String::isNotEmpty) ?: "2;bedrock,2xdirt,grass;1;") {

    private lateinit var chunk: Chunk
    private var populators: MutableList<Populator> = mutableListOf()

    private var layers: List<Long> = listOf()
    private var biomeId: Int = 0
    private var options: Map<String, Map<String, String>> = HashObjObjMaps.newMutableMap()

    init {
        parsePreset()

        if (options.containsKey("decoration")) {
            val stone = VanillaBlocks.STONE.block
            populators.add(
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

        generateBaseChunk()
    }

    protected fun parsePreset() {
        Regex("^[0-9]+;([0-9a-z_:*,]+);([0-9]+);?([0-9a-z_:*,=]+)?").matchEntire(preset)?.let {
            var (_, layers, biomeId, options) = it.groupValues

            this.layers = parseLayers(layers)
            this.biomeId = biomeId.toInt()
            this.options = parseOptions(options)
        }
    }

    protected fun generateBaseChunk() {
        chunk = Chunk().apply {
            repeat2(16) { x, z ->
                setBiomeId(x, z, biomeId)
            }

            val count = layers.size
            for (sy in 0 until count step 16) {
                val subChunk = getSubChunk(sy shr 4)
                repeat(16) let@{ y ->
                    val fullId = layers.getOrNull(y or sy) ?: return@let
                    repeat2(16) { x, z ->
                        subChunk.setFullBlock(x, y, z, fullId)
                    }
                }
            }
        }
    }

    override fun generateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int) {
        world.setChunk(chunkX, chunkZ, chunk.clone())
    }

    override fun populateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int) {
        random = Random(0xdeadbeef xor (chunkX.toLong() shl 8) xor chunkZ.toLong() xor seed)
        populators.forEach { populator ->
            populator.populate(world, chunkX, chunkZ, random)
        }
    }

    companion object {
        @JvmStatic
        fun parseLayers(layers: String): List<Long> {
            val result: MutableList<Long> = mutableListOf()
            Regex("(?:([0-9]+)[x|*])?([0-9a-z_:]+)").findAll(layers).forEach {
                val (_, countString, blockName) = it.groupValues

                val fullId = try {
                    LegacyStringToItemParser.parse(blockName).getBlock().getFullId()
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid preset layer block name \"$blockName\": ${e.message}", e)
                }

                repeat(if (countString.isNotEmpty()) countString.toInt() else 1) {
                    result.add(fullId)
                }
            }
            return result
        }

        @JvmStatic
        fun parseOptions(options: String): Map<String, Map<String, String>> {
            val result: MutableMap<String, Map<String, String>> = HashObjObjMaps.newMutableMap()
            Regex("([0-9a-z_]+)\\(?((?:[0-9a-z_]+[=:][0-9a-z_:]+ ?)*)\\)?,?").findAll(options).forEach {
                val (_, optionName, optionString) = it.groupValues

                /**
                 * HACKS: Boolean option is checked for existence with containsKey.
                 * So this empty array means 'true'.
                 */
                val params: MutableMap<String, String> = HashObjObjMaps.newMutableMap()
                optionString.split(" ").forEach { k ->
                    val (key, value) = k.split("=")
                    params[key] = value
                }

                result[optionName] = params
            }
            return result
        }
    }
}
