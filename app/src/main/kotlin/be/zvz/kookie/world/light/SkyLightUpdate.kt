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
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.HeightArray
import be.zvz.kookie.world.format.LightArray
import be.zvz.kookie.world.format.LightLevel
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import com.koloboke.collect.map.hash.HashLongObjMaps
import kotlin.math.max

class SkyLightUpdate(
    subChunkExplorer: SubChunkExplorer,
    lightFilters: MutableMap<Long, Int>,
    val directSkyLightBlockers: MutableMap<Long, Boolean> = HashLongObjMaps.newMutableMap()
) : LightUpdate(subChunkExplorer, lightFilters) {

    override fun getEffectiveLight(x: Int, y: Int, z: Int): Int {
        if (y >= World.Y_MAX) {
            subChunkExplorer.invalidate()
            return 15
        }
        return super.getEffectiveLight(x, y, z)
    }

    override fun recalculateNode(x: Int, y: Int, z: Int) {
        if (subChunkExplorer.moveTo(x, y, z) === SubChunkExplorer.Status.INVALID) {
            return
        }
        val chunk = subChunkExplorer.currentChunk
            ?: throw CorruptedWorldException("SubChunkExplorer does not have a target chunk")
        val subChunk = subChunkExplorer.currentSubChunk
            ?: throw CorruptedWorldException("SubChunkExplorer does not have a target sub-chunk")
        val oldHeightMap = chunk.getHeightMap(x and 0xf, z and 0xf)
        val source = subChunk.getFullBlock(x and 0xf, y and 0xf, z and 0xf)

        val yPlusOne = y + 1
        val newHeightMap: Int
        // Block changed directly beneath the heightmap. Check if a block was removed or changed to a different light-filter.
        if (yPlusOne == oldHeightMap) {
            newHeightMap = recalculateHeightMapColumn(chunk, x and 0x0f, z and 0x0f, directSkyLightBlockers)
            chunk.setHeightMap(x and 0xf, z and 0xf, newHeightMap)
        } else if (yPlusOne > oldHeightMap) { // Block changed above the heightmap.
            if (directSkyLightBlockers[source] == true) {
                chunk.setHeightMap(x and 0xf, z and 0xf, yPlusOne)
                newHeightMap = yPlusOne
            } else { // Block changed which has no effect on direct sky light, for example placing or removing glass.
                return
            }
        } else { // Block changed below heightmap
            newHeightMap = oldHeightMap
        }

        when {
            newHeightMap > oldHeightMap -> { // Heightmap increase, block placed, remove sky light
                for (i in y downTo oldHeightMap) {
                    // Remove all light beneath, adjacent recalculation will handle the rest.
                    setAndUpdateLight(x, i, z, LightLevel(0))
                }
            }
            newHeightMap < oldHeightMap -> { // Heightmap decrease, block changed or removed, add sky light
                for (i in y downTo newHeightMap) {
                    setAndUpdateLight(x, i, z, LightLevel(15))
                }
            }
            else -> { // No heightmap change, block changed "underground"
                setAndUpdateLight(x, y, z, LightLevel(max(0, getHighestAdjacentLight(x, y, z) - (lightFilters[source] ?: 0))))
            }
        }
    }

    override fun recalculateChunk(chunkX: Int, chunkZ: Int): Int {
        if (subChunkExplorer.moveToChunk(chunkX, 0, chunkZ) === SubChunkExplorer.Status.INVALID) {
            throw CorruptedWorldException("Chunk chunkX chunkZ does not exist")
        }
        val chunk = subChunkExplorer.currentChunk
            ?: throw CorruptedWorldException("SubChunkExplorer does not have a target chunk")

        val newHeightMap = recalculateHeightMap(chunk, directSkyLightBlockers)
        chunk.heightMap.values = newHeightMap.values

        // setAndUpdateLight() won't bother propagating from nodes that are already what we want to change them to, so we
        // have to avoid filling full light for any subchunk that contains a heightmap Y coordinate
        val highestHeightMapPlusOne = (chunk.heightMap.values.maxOrNull() ?: 0) + 1
        val lowestClearSubChunk = (highestHeightMapPlusOne shr 4) + if (highestHeightMapPlusOne and 0xf != 0) 1 else 0
        val chunkHeight = chunk.subChunks.size

        var y1 = 0
        while (y1 < lowestClearSubChunk && y1 < chunkHeight) {
            chunk.getSubChunk(y1++).skyLight = LightArray.fill(0)
        }
        for (y2 in lowestClearSubChunk until chunkHeight) {
            chunk.getSubChunk(y2).skyLight = LightArray.fill(15)
        }

        val baseX = chunkX shl 4
        val baseZ = chunkZ shl 4
        var lightSources = 0
        repeat(16) { x ->
            repeat(16) { z ->

                val currentHeight = chunk.getHeightMap(x, z)
                var maxAdjacentHeight = 0
                if (x != 0) {
                    maxAdjacentHeight = max(maxAdjacentHeight, chunk.getHeightMap(x - 1, z))
                }
                if (x != 15) {
                    maxAdjacentHeight = max(maxAdjacentHeight, chunk.getHeightMap(x + 1, z))
                }
                if (z != 0) {
                    maxAdjacentHeight = max(maxAdjacentHeight, chunk.getHeightMap(x, z - 1))
                }
                if (z != 15) {
                    maxAdjacentHeight = max(maxAdjacentHeight, chunk.getHeightMap(x, z + 1))
                }

                /*
                 * We skip the top two blocks between current height and max adjacent (if there's a difference) because:
                 * - the block next to the highest adjacent will do nothing during propagation (it's surrounded by 15s)
                 * - the block below that block will do the same as the node in the highest adjacent
                 * NOTE: If block opacity becomes direction-aware in the future, the second point will become invalid.
                 */
                val nodeColumnEnd = max(currentHeight, maxAdjacentHeight - 2)
                for (y in currentHeight..nodeColumnEnd) {
                    setAndUpdateLight(x + baseX, y1, z + baseZ, LightLevel(15))
                    lightSources++
                }
                val yMax = lowestClearSubChunk * 16
                for (y in nodeColumnEnd + 1 until yMax) {
                    if (subChunkExplorer.moveTo(x + baseX, y1, z + baseZ) !== SubChunkExplorer.Status.INVALID) {
                        getCurrentLightArray().set(x, y1 and 0xf, z, LightLevel(15))
                    }
                }
            }
        }

        return lightSources
    }

    companion object {
        /**
         * Recalculates the heightmap for the whole chunk.
         *
         * @param \SplFixedArray|bool[] directSkyLightBlockers
         * @phpstan-param \SplFixedArray<bool> directSkyLightBlockers
         */
        private fun recalculateHeightMap(chunk: Chunk, directSkyLightBlockers: MutableMap<Long, Boolean>): HeightArray {
            var maxSubChunkY = chunk.subChunks.size - 1
            while (maxSubChunkY >= 0) {
                if (!chunk.getSubChunk(maxSubChunkY--).isEmptyFast()) {
                    break
                }
            }
            val result = HeightArray.fill(World.Y_MIN)
            if (maxSubChunkY == -1) { // whole column is definitely empty
                return result
            }

            repeat(16) { x ->
                repeat(16) { z ->
                    var y: Int? = null
                    for (subChunkY in maxSubChunkY downTo 0) {
                        val subHighestBlockY = chunk.getSubChunk(subChunkY).getHighestBlockAt(x, z)
                        if (subHighestBlockY !== null) {
                            y = subChunkY * 16 + subHighestBlockY
                            break
                        }
                    }

                    if (y === null) { // no blocks in the column
                        result[x, z] = World.Y_MIN
                    } else {
                        while (y >= World.Y_MIN) {
                            if (directSkyLightBlockers[chunk.getFullBlock(x, y, z)] == true) {
                                result[x, z] = y + 1
                                break
                            }
                            --y
                        }
                    }
                }
            }
            return result
        }

        /**
         * Recalculates the heightmap for the block column at the specified X/Z chunk coordinates
         *
         * @param x Int 0-15
         * @param z Int 0-15
         * @return Int New calculated heightmap value (0-256 inclusive)
         */
        private fun recalculateHeightMapColumn(
            chunk: Chunk,
            x: Int,
            z: Int,
            directSkyLightBlockers: MutableMap<Long, Boolean>
        ): Int {
            val startY = chunk.getHighestBlockAt(x, z)
            if (startY === null) {
                return World.Y_MIN
            }
            for (y in startY downTo World.Y_MIN) {
                if (directSkyLightBlockers[chunk.getFullBlock(x, y, z)] == true) {
                    return y + 1
                }
            }

            return startY + 1
        }
    }
}
