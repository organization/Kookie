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
package be.zvz.kookie.entity.utils

import be.zvz.kookie.math.Math
import kotlin.math.pow

object ExperienceUtils {

    @JvmStatic
    fun getXpToReachLevel(level: Int): Int = when {
        level <= 16 -> {
            level.toDouble().pow(2).toInt() + level * 6
        }
        level <= 31 -> {
            (level.toDouble().pow(2).toInt() * 2 * 2.5 - 40.5 * level + 360).toInt()
        }
        else -> {
            (level.toDouble().pow(2) * 4.5 - 162.5 * level + 2220).toInt()
        }
    }

    @JvmStatic
    fun getXpToCompleteLevel(level: Int): Int = when {
        level <= 15 -> {
            2 * level + 7
        }
        level <= 30 -> {
            5 * level - 38
        }
        else -> {
            9 * level - 158
        }
    }

    @JvmStatic
    fun getLevelFromXp(xp: Int): Float {
        if (xp < 0) {
            throw IllegalArgumentException("XP must be at least 0")
        }
        var a: Float
        var b: Float
        var c: Float
        when {
            xp <= getXpToReachLevel(16) -> {
                a = 1F
                b = 6F
                c = 0F
            }
            xp <= getXpToReachLevel(31) -> {
                a = 2.5F
                b = -40.5F
                c = 360F
            }
            else -> {
                a = 4.5F
                b = -162.5F
                c = 2220F
            }
        }
        return Math.solveQuadratic(a, b, c - xp).maxOrNull()
            ?: throw AssertionError("Expected at least 1 solution")
    }
}
