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

import com.koloboke.collect.map.hash.HashObjObjMaps
import be.zvz.kookie.entity.Attribute.Identifier as AttributeId

object AttributeFactory {
    private val attributes = HashObjObjMaps.newMutableMap<AttributeId, Attribute>()

    init {
        register(AttributeId.ABSORPTION, 0F, Float.MAX_VALUE, 0F, true)
        register(AttributeId.SATURATION, 0F, 20F, 20F, true)
        register(AttributeId.EXHAUSTION, 0F, 5F, 0F, false)
        register(AttributeId.KNOCKBACK_RESISTANCE, 0F, 1F, 0F, true)
        register(AttributeId.HEALTH, 0F, 20F, 20F, true)
        register(AttributeId.MOVEMENT_SPEED, 0F, Float.MAX_VALUE, 0.1F, true)
        register(AttributeId.FOLLOW_RANGE, 0F, 2048F, 16F, false)
        register(AttributeId.HUNGER, 0F, 20F, 20F, true)
        register(AttributeId.ATTACK_DAMAGE, 0F, Float.MAX_VALUE, 1F, false)
        register(AttributeId.EXPERIENCE_LEVEL, 0F, 24791F, 0F, true)
        register(AttributeId.EXPERIENCE, 0F, 1F, 0F, true)
        register(AttributeId.UNDERWATER_MOVEMENT, 0F, Float.MAX_VALUE, 0.02F, true)
        register(AttributeId.LUCK, -1024F, 1024F, 0F, true)
        register(AttributeId.FALL_DAMAGE, 0F, Float.MAX_VALUE, 1F, true)
        register(AttributeId.HORSE_JUMP_STRENGTH, 0F, 2F, 0.7F, true)
        register(AttributeId.ZOMBIE_SPAWN_REINFORCEMENTS, 0F, 1F, 0F, true)
        register(AttributeId.LAVA_MOVEMENT, 0F, Float.MAX_VALUE, 0.02F, true)
    }

    @JvmStatic
    fun get(id: AttributeId): Attribute? = attributes[id]

    @JvmStatic
    fun mustGet(id: AttributeId): Attribute = attributes[id]!!

    @JvmStatic
    fun register(id: AttributeId, minValue: Float, maxValue: Float, defaultValue: Float, shouldSend: Boolean) {
        attributes[id] = Attribute(id, minValue, maxValue, defaultValue, shouldSend)
    }
}
