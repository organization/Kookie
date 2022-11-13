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
package be.zvz.kookie.world

import be.zvz.kookie.block.Block
import be.zvz.kookie.math.Vector3
import com.koloboke.collect.map.hash.HashIntObjMaps

class BlockTransaction(val world: ChunkManager) {
    private val blocks: MutableMap<Int, MutableMap<Int, MutableMap<Int, Block>>> = HashIntObjMaps.newMutableMap()
    private val validators: MutableList<(world: ChunkManager, x: Int, y: Int, z: Int) -> Boolean> = mutableListOf()

    init {
        addValidator { world, x, y, z -> world.isInWorld(x, y, z) }
    }

    fun addBlock(pos: Vector3, state: Block): BlockTransaction =
        addBlockAt(pos.floorX, pos.floorY, pos.floorZ, state)

    fun addBlockAt(x: Int, y: Int, z: Int, state: Block): BlockTransaction = this.apply {
        blocks
            .getOrPut(x, HashIntObjMaps::newMutableMap)
            .getOrPut(y, HashIntObjMaps::newMutableMap)[z] = state
    }

    fun fetchBlock(pos: Vector3): Block = fetchBlockAt(pos.floorX, pos.floorY, pos.floorZ)

    fun fetchBlockAt(x: Int, y: Int, z: Int): Block = blocks[x]?.get(y)?.get(z) ?: world.getBlockAt(x, y, z)

    fun apply(): Boolean {
        getBlocks().forEach { data ->
            validators.forEach { callback ->
                if (!callback.invoke(world, data.first.floorX, data.first.floorY, data.first.floorZ)) {
                    return false
                }
            }
        }
        getBlocks().forEach {
            world.setBlockAt(it.first.floorX, it.first.floorY, it.first.floorZ, it.second)
        }
        return true
    }

    fun getBlocks() = sequence {
        blocks.forEach { (x, yLine) ->
            yLine.forEach { (y, zLine) ->
                zLine.forEach { (z, block) ->
                    yield(Pair(Vector3(x, y, z), block))
                }
            }
        }
    }

    fun addValidator(validator: (world: ChunkManager, x: Int, y: Int, z: Int) -> Boolean) {
        validators.add(validator)
    }
}
