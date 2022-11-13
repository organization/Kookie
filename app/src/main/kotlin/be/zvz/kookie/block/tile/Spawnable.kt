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

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt
import be.zvz.kookie.world.World

abstract class Spawnable(world: World, pos: Vector3) : Tile(world, pos) {
    private var cache: CacheableNbt? = null
    var dirty: Boolean = true
        set(value) {
            if (value) {
                cache = null
            }
            field = value
        }

    val serializedSpawnCompound: CacheableNbt
        get() = cache ?: run {
            val nbt = CompoundTag()
                .setString(TAG_ID, TileFactory.getSaveId(this::class.java))
                .setInt(TAG_X, pos.x.toInt())
                .setInt(TAG_Y, pos.y.toInt())
                .setInt(TAG_Z, pos.z.toInt())
            addAdditionalSpawnData(nbt)
            val cache = CacheableNbt(nbt)
            this.cache = cache

            cache
        }

    /** An extension to getSpawnCompound() for further modifying the generic tile NBT. */
    protected abstract fun addAdditionalSpawnData(nbt: CompoundTag)
}
