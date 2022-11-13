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
package be.zvz.kookie.block.tile

import be.zvz.kookie.block.Block
import be.zvz.kookie.item.Item
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World

abstract class Tile(world: World, pos: Vector3) {
    protected val timings by lazy { Timings.getTileEntityTimings(this) }

    val pos = Position.fromObject(pos, world)
    val closed: Boolean = false

    abstract fun readSaveData(nbt: CompoundTag)

    protected abstract fun writeSaveData(nbt: CompoundTag)

    fun saveNBT(): CompoundTag {
        val saveId = TileFactory.getSaveId(this.javaClass)
        return CompoundTag.create().apply {
            setString(TAG_ID, saveId)
            setInt(TAG_X, pos.floor().x.toInt())
            setInt(TAG_Y, pos.floor().y.toInt())
            setInt(TAG_Z, pos.floor().z.toInt())

            writeSaveData(this)
        }
    }

    fun getCleanedNBT(): CompoundTag? = CompoundTag().apply(this::writeSaveData).takeIf { it.count() > 0 }

    fun copyDataFromItem(item: Item) = item.getCustomBlockData()?.let(this::readSaveData)

    fun getBlock(): Block = TODO("get block from world")

    fun onBlockDestroyed(): Any = TODO()

    fun close() {
        TODO("Close Tile")
    }

    companion object {
        const val TAG_ID = "id"
        const val TAG_X = "x"
        const val TAG_Z = "z"
        const val TAG_Y = "y"
    }
}
