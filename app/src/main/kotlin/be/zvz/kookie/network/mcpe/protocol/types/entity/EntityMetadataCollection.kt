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
package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import com.koloboke.collect.map.hash.HashIntObjMaps

class EntityMetadataCollection {

    private val properties: MutableMap<Int, MetadataProperty> = HashIntObjMaps.newMutableMap()

    private val dirtyProperties: MutableMap<Int, MetadataProperty> = HashIntObjMaps.newMutableMap()

    fun set(key: Int, value: MetadataProperty, force: Boolean = false) {
        val v = properties[key]
        if (!force && v === null && !(value::class.java.isInstance(properties[key]))) {
            throw AssertionError("Can't overwrite property with mismatching type (have ${value::class.java.simpleName})")
        }
        if (v == null || v != value) {
            properties[key] = value
            dirtyProperties[key] = value
        }
    }

    fun setByte(key: Int, value: Int, force: Boolean = false) {
        set(key, ByteMetadataProperty(value), force)
    }

    fun setShort(key: Int, value: Int, force: Boolean = false) {
        set(key, ShortMetadataProperty(value), force)
    }

    fun setInt(key: Int, value: Int, force: Boolean = false) {
        set(key, IntMetadataProperty(value), force)
    }

    fun setFloat(key: Int, value: Float, force: Boolean = false) {
        set(key, FloatMetadataProperty(value), force)
    }

    fun setString(key: Int, value: String, force: Boolean = false) {
        set(key, StringMetadataProperty(value), force)
    }

    fun setCompoundTag(key: Int, value: CompoundTag, force: Boolean = false) {
        set(key, CompoundMetadataProperty(value), force)
    }

    fun setBlockPos(key: Int, value: PacketSerializer.BlockPosition?, force: Boolean = false) {
        set(key, BlockPosMetadataProperty(value ?: PacketSerializer.BlockPosition()), force)
    }

    fun setLong(key: Int, value: Int, force: Boolean = false) {
        set(key, LongMetadataProperty(value), force)
    }

    fun setVector3(key: Int, value: Vector3?, force: Boolean = false) {
        set(key, Vec3MetadataProperty(value ?: Vector3()), force)
    }

    fun setGenericFlag(flagId: Int, value: Boolean) {
        val propertyId = if (flagId >= 64) {
            EntityMetadataProperties.FLAGS2
        } else {
            EntityMetadataProperties.FLAGS
        }
        val realFlagId = flagId % 64
        var flagSet = when (val flagSetProp = properties[propertyId]) {
            null -> {
                0
            }
            is LongMetadataProperty -> {
                flagSetProp.value
            }
            else -> {
                throw AssertionError("Wrong type found for flags, want long, but have ${flagSetProp::class.java.simpleName}")
            }
        }
        if ((flagSet shr realFlagId) and 1 != (if (value) 1 else 0)) {
            flagSet = flagSet xor (1 shl realFlagId)
            setLong(propertyId, flagSet)
        }
    }

    fun setPlayerFlag(flagId: Int, value: Boolean) {
        var flagSet = when (val flagSetProp = properties[EntityMetadataProperties.PLAYER_FLAGS]) {
            null -> {
                0
            }
            is ByteMetadataProperty -> {
                flagSetProp.value
            }
            else -> {
                throw AssertionError(
                    "Wrong type found for flags, want byte, but have ${flagSetProp::class.java.simpleName}"
                )
            }
        }
        if ((flagSet shl flagId) and 1 != (if (value) 1 else 0)) {
            flagSet = flagSet xor (1 shl flagId)
            setByte(EntityMetadataProperties.PLAYER_FLAGS, flagSet)
        }
    }

    fun getAll(): MutableMap<Int, MetadataProperty> = properties

    fun getDirty(): MutableMap<Int, MetadataProperty> = dirtyProperties

    fun clearDirtyProperties() = dirtyProperties.clear()
}
