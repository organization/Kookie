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
package be.zvz.kookie.world.format

data class LightLevel(val value: Int) {
    init {
        if (value > MAX) {
            throw IllegalArgumentException("value out of range")
        }
    }

    operator fun plus(i: LightLevel): LightLevel = LightLevel(value + i.value)
    operator fun plus(i: Int): LightLevel = LightLevel(value + i)

    operator fun minus(i: LightLevel): LightLevel = LightLevel(value - i.value)
    operator fun minus(i: Int): LightLevel = LightLevel(value - i)

    operator fun compareTo(newLevel: LightLevel): Int = when {
        value < newLevel.value -> -1
        value > newLevel.value -> 1
        else -> 0
    }

    operator fun compareTo(newLevel: Int): Int = when {
        value < newLevel -> -1
        value > newLevel -> 1
        else -> 0
    }

    companion object {
        const val MAX = 15
    }
}
