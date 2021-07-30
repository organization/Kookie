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

import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.LightArray
import be.zvz.kookie.world.format.LightLevel
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import be.zvz.kookie.world.utils.SubChunkExplorer
import com.koloboke.collect.map.hash.HashLongObjMaps
import kotlin.math.max

abstract class LightUpdate(
    val subChunkExplorer: SubChunkExplorer,
    val lightFilters: MutableMap<Long, Int>
) {
    val updateNodes: MutableMap<Long, LightNode> = HashLongObjMaps.newMutableMap()

    protected fun getCurrentLightArray(): LightArray = subChunkExplorer.currentSubChunk?.skyLight
        ?: throw CorruptedWorldException("SubChunkExplorer does not have a target sub-chunk")

    abstract fun recalculateNode(x: Int, y: Int, z: Int)

    /**
     * Scans for all light sources in the target chunk and adds them to the propagation queue.
     * This erases preexisting light in the chunk.
     */
    abstract fun recalculateChunk(chunkX: Int, chunkZ: Int): Int

    protected open fun getEffectiveLight(x: Int, y: Int, z: Int): Int =
        if (subChunkExplorer.moveTo(x, y, z) !== SubChunkExplorer.Status.INVALID) {
            getCurrentLightArray().get(x and 0xf, y and 0xf, z and 0xf)
        } else {
            0
        }

    protected fun getHighestAdjacentLight(x: Int, y: Int, z: Int): Int {
        var adjacent = 0
        arrayOf(
            Triple(x + 1, y, z),
            Triple(x - 1, y, z),
            Triple(x, y + 1, z),
            Triple(x, y - 1, z),
            Triple(x, y, z + 1),
            Triple(x, y, z - 1),
        ).forEach { (x1, y1, z1) ->
            adjacent = max(adjacent, getEffectiveLight(x1, y1, z1))
            if (adjacent == 15) {
                return@forEach
            }
        }
        return adjacent
    }

    fun setAndUpdateLight(x: Int, y: Int, z: Int, newLevel: LightLevel) {
        updateNodes[World.blockHash(x, y, z)] = LightNode(x, y, z, newLevel)
    }

    private fun prepareNodes(): LightPropagationContext {
        val context = LightPropagationContext()
        updateNodes.forEach { blockHash, (x, y, z, newLevel) ->
            if (subChunkExplorer.moveTo(x, y, z) !== SubChunkExplorer.Status.INVALID) {
                val lightArray = getCurrentLightArray()
                val oldLevel = LightLevel(lightArray.get(x and 0xf, y and 0xf, z and 0xf))
                if (oldLevel != newLevel) {
                    lightArray.set(x and 0xf, y and 0xf, z and 0xf, newLevel)
                    if (oldLevel < newLevel) { // light increased
                        context.spreadVisited[blockHash] = true
                        context.spreadQueue.add(Triple(x, y, z))
                    } else { // light removed
                        context.removalVisited[blockHash] = true
                        context.removalQueue.add(LightNode(x, y, z, oldLevel))
                    }
                }
            }
        }
        return context
    }

    fun execute(): Int {
        val context = prepareNodes()
        var touched = 0
        while (context.removalQueue.isNotEmpty()) {
            ++touched
            val (x, y, z, oldAdjacentLight) = context.removalQueue.remove()

            arrayOf(
                Triple(x + 1, y, z),
                Triple(x - 1, y, z),
                Triple(x, y + 1, z),
                Triple(x, y - 1, z),
                Triple(x, y, z + 1),
                Triple(x, y, z - 1)
            ).forEach { (cx, cy, cz) ->
                if (subChunkExplorer.moveTo(cx, cy, cz) != SubChunkExplorer.Status.INVALID) {
                    computeRemoveLight(cx, cy, cz, oldAdjacentLight, context)
                } else {
                    val blockHash = World.blockHash(cx, cy, cz)
                    if (getEffectiveLight(cx, cy, cz) > 0 && context.spreadVisited[blockHash] == true) {
                        context.spreadVisited[blockHash] = true
                        context.spreadQueue.add(Triple(cx, cy, cz))
                    }
                }
            }
        }

        while (!context.spreadQueue.isEmpty()) {
            ++touched
            val (x, y, z) = context.spreadQueue.remove()
            context.spreadVisited.remove(World.blockHash(x, y, z))

            val newAdjacentLight = LightLevel(getEffectiveLight(x, y, z))
            if (newAdjacentLight <= 0) {
                continue
            }
            arrayOf(
                Triple(x + 1, y, z),
                Triple(x - 1, y, z),
                Triple(x, y + 1, z),
                Triple(x, y - 1, z),
                Triple(x, y, z + 1),
                Triple(x, y, z - 1)
            ).forEach { (cx, cy, cz) ->
                if (subChunkExplorer.moveTo(cx, cy, cz) != SubChunkExplorer.Status.INVALID) {
                    computeSpreadLight(cx, cy, cz, newAdjacentLight, context)
                }
            }
        }

        return touched
    }

    protected fun computeRemoveLight(x: Int, y: Int, z: Int, oldAdjacentLevel: LightLevel, context: LightPropagationContext) {
        val lightArray = getCurrentLightArray()
        val current = LightLevel(lightArray.get(x and 0xf, y and 0xf, z and 0xf))
        if (current.value != 0 && current < oldAdjacentLevel) {
            lightArray.set(x and 0xf, y and 0xf, z and 0xf, LightLevel(0))

            val blockHash = World.blockHash(x, y, z)
            if (context.removalVisited[blockHash] != true) {
                context.removalVisited[blockHash] = true
                if (current > 1) {
                    context.removalQueue.add(LightNode(x, y, z, current))
                }
            }
        } else if (current >= oldAdjacentLevel) {
            val blockHash = World.blockHash(x, y, z)
            if (context.spreadVisited[blockHash] != true) {
                context.spreadVisited[blockHash] = true
                context.spreadQueue.add(Triple(x, y, z))
            }
        }
    }

    protected fun computeSpreadLight(x: Int, y: Int, z: Int, newAdjacentLevel: LightLevel, context: LightPropagationContext) {
        val lightArray = getCurrentLightArray()
        val current = LightLevel(lightArray.get(x and 0xf, y and 0xf, z and 0xf))
        val lightFilter = lightFilters[subChunkExplorer.currentSubChunk?.getFullBlock(x and 0x0f, y and 0x0f, z and 0x0f)]
        val potentialLight = newAdjacentLevel - (lightFilter ?: 0)

        if (current < potentialLight) {
            lightArray.set(x and 0xf, y and 0xf, z and 0xf, potentialLight)

            val blockHash = World.blockHash(x, y, z)
            if (context.spreadVisited[blockHash] != true && potentialLight > 1) {
                context.spreadVisited[blockHash] = true
                context.spreadQueue.add(Triple(x, y, z))
            }
        }
    }
}
