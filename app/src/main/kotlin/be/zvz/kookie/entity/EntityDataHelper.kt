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
package be.zvz.kookie.entity

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.DoubleTag
import be.zvz.kookie.nbt.tag.FloatTag
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.world.World

object EntityDataHelper {

    @JvmStatic
    fun parseLocation(nbt: CompoundTag, world: World): Location {
        val pos = parseVec3(nbt, "pos", false)

        val yawPitch = nbt.getTag("Rotation")
        if (yawPitch !is ListTag<*> || yawPitch.getTagType() != NBT.TagType.FLOAT) {
            throw IllegalArgumentException("'Rotation' should be List<Float>")
        }
        val values = yawPitch.value
        if (values.size != 2) {
            throw IllegalArgumentException("Expected exactly 2 entries for 'Rotation'")
        }
        return Location.fromObject(pos, world, (values[0] as FloatTag).value, (values[1] as FloatTag).value)
    }

    @JvmStatic
    fun parseVec3(nbt: CompoundTag, tagName: String, optional: Boolean): Vector3 {
        val pos = nbt.getTag(tagName)
        if (pos === null && optional) {
            return Vector3(0, 0, 0)
        }
        if (pos !is ListTag<*> || pos.getTagType() != NBT.TagType.DOUBLE) {
            throw IllegalArgumentException("$tagName should be List<Double>")
        }
        val values = pos.value
        if (values.size != 3) {
            throw IllegalArgumentException("Expected exactly 3 entries in '$tagName' tag")
        }
        return Vector3((values[0] as DoubleTag).value, (values[1] as DoubleTag).value, (values[2] as DoubleTag).value)
    }

    @JvmStatic
    fun createBaseNBT(pos: Vector3, motion: Vector3? = null, yaw: Double = 0.0, pitch: Double = 0.0): CompoundTag {
        val nbt = CompoundTag.create()
        nbt.setTag(
            "Pos",
            ListTag(
                listOf(
                    DoubleTag(pos.x),
                    DoubleTag(pos.y),
                    DoubleTag(pos.z)
                )
            )
        )
        nbt.setTag(
            "Motion",
            ListTag(
                listOf(
                    DoubleTag(motion?.x ?: 0.0),
                    DoubleTag(motion?.y ?: 0.0),
                    DoubleTag(motion?.z ?: 0.0)
                )
            )
        )
        nbt.setTag(
            "Rotation",
            ListTag(
                listOf(
                    DoubleTag(yaw),
                    DoubleTag(pitch)
                )
            )
        )
        return nbt
    }

    fun createBaseNBT(location: Location, motion: Vector3? = null): CompoundTag =
        createBaseNBT(location.asVector3(), motion, location.yaw, location.pitch)

    /**
     * Used to initialize an entity.
     * Calling Entity.spawnTo() without calling
     * this method will result in an AssertionError.
     *
     * @see Entity.spawnTo
     */
    fun initDefaults(entity: Entity, nbt: CompoundTag? = null) {
        entity.recalculateBoundingBox()
        entity.resetLastMovement()
        entity.addAttributes()
        entity.initEntity(nbt ?: CompoundTag.create())
        entity.initialized = true
    }
}
