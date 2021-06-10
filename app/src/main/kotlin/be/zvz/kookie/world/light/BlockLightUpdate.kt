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
package be.zvz.kookie.world.light

import be.zvz.kookie.world.SubChunkExplorer
import be.zvz.kookie.world.format.LightArray
import be.zvz.kookie.world.format.LightLevel
import be.zvz.kookie.world.format.SubChunk
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import com.koloboke.collect.map.hash.HashLongObjMaps
import kotlin.math.max

class BlockLightUpdate(
    subChunkExplorer: SubChunkExplorer,
    lightFilters: MutableMap<Long, Int>,
    val lightEmitters: MutableMap<Long, Int> = HashLongObjMaps.newMutableMap()
) : LightUpdate(subChunkExplorer, lightFilters) {

    override fun recalculateNode(x: Int, y: Int, z: Int) {
        if (subChunkExplorer.moveTo(x, y, z) === SubChunkExplorer.Status.INVALID) {
            val subChunk = subChunkExplorer.currentSubChunk
                ?: throw CorruptedWorldException("SubChunkExplorer does not have a target sub-chunk")
            val source = subChunk.getFullBlock(x and 0xf, y and 0xf, z and 0xf)
            setAndUpdateLight(x, y, z, LightLevel(max(0, getHighestAdjacentLight(x, y, z) - (lightFilters[source] ?: 0))))
        }
    }

    override fun recalculateChunk(chunkX: Int, chunkZ: Int): Int {
        if (subChunkExplorer.moveToChunk(chunkX, 0, chunkZ) === SubChunkExplorer.Status.INVALID) {
            throw CorruptedWorldException("Chunk chunkX chunkZ does not exist")
        }
        val chunk = subChunkExplorer.currentChunk
            ?: throw CorruptedWorldException("SubChunkExplorer does not have a target chunk")

        var lightSources = 0
        chunk.subChunks.forEachIndexed { subChunkY, subChunk ->
            subChunk.skyLight = LightArray.fill(0)
            subChunk.blockLayers.forEach lit@{ layer ->
                layer.getPalette().forEach { state ->
                    val light = lightEmitters[state]
                        ?: throw CorruptedWorldException("SubChunk palette does not have a target light emitter")
                    if (light > 0) {
                        lightSources += scanForLightEmittingBlocks(subChunk, chunkX shl 4, subChunkY shl 4, chunkZ shl 4)
                        return@lit
                    }
                }
            }
        }

        return lightSources
    }

    private fun scanForLightEmittingBlocks(subChunk: SubChunk, baseX: Int, baseY: Int, baseZ: Int): Int {
        var lightSources = 0
        repeat(16) { x ->
            repeat(16) { y ->
                repeat(16) { z ->
                    val light = lightEmitters[subChunk.getFullBlock(x, y, z)]
                        ?: throw CorruptedWorldException("SubChunk palette does not have a target light emitter")
                    if (light > 0) {
                        setAndUpdateLight(baseX + x, baseY + y, baseZ + z, LightLevel(light))
                        lightSources++
                    }
                }
            }
        }
        return lightSources
    }
}
