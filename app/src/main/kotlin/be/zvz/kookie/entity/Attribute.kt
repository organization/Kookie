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

class Attribute @JvmOverloads constructor(
    val id: Identifier,
    minValue: Float,
    maxValue: Float,
    defaultValue: Float,
    private val shouldSend: Boolean = false
) {
    private var desynchronized: Boolean = false
    var minValue: Float = minValue
        set(value) {
            val max = maxValue
            if (value > max) {
                throw IllegalArgumentException("Minimum $value is greater than the maximum $max")
            }
            if (field != value) {
                desynchronized = true
                field = value
            }
        }
    var maxValue: Float = maxValue
        set(value) {
            val min = minValue
            if (value < min) {
                throw IllegalArgumentException("Maximum $value is less than the minimum $min")
            }
            if (field != value) {
                desynchronized = true
                field = value
            }
        }
    var defaultValue: Float = defaultValue
        set(value) {
            if (value > maxValue || value < minValue) {
                throw IllegalArgumentException("Default $defaultValue is outside the range $minValue - $maxValue")
            }
            if (field != defaultValue) {
                desynchronized = true
                field = value
            }
        }
    var currentValue: Float

    init {
        if (minValue > maxValue || defaultValue > maxValue || defaultValue < minValue) {
            throw IllegalArgumentException("Invalid ranges: min value: $minValue, max value: $maxValue, $defaultValue: $defaultValue")
        }
        currentValue = defaultValue
    }

    fun isSyncable(): Boolean = shouldSend
    fun isDesynchronized(): Boolean = shouldSend && desynchronized

    @JvmOverloads
    fun markSynchronized(synced: Boolean = true) {
        desynchronized = !synced
    }

    enum class Identifier(val id: String) {
        ABSORPTION("absorption"),
        SATURATION("player.saturation"),
        EXHAUSTION("player.exhaustion"),
        KNOCKBACK_RESISTANCE("knockback_resistance"),
        HEALTH("health"),
        MOVEMENT_SPEED("movement"),
        FOLLOW_RANGE("follow_range"),
        HUNGER("player.hunger"),
        ATTACK_DAMAGE("attack_damage"),
        EXPERIENCE_LEVEL("player.level"),
        EXPERIENCE("player.experience"),
        UNDERWATER_MOVEMENT("underwater_movement"),
        LUCK("luck"),
        FALL_DAMAGE("fall_damage"),
        HORSE_JUMP_STRENGTH("horse.jump_strength"),
        ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawn_reinforcements"),
        LAVA_MOVEMENT("lava_movement");

        val fullId = "minecraft:$id"

        companion object {
            private val VALUES = values()
            fun from(value: String) = VALUES.firstOrNull { it.id == value || it.fullId == value }
        }
    }
}
