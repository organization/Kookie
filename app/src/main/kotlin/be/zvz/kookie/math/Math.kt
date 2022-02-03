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
package be.zvz.kookie.math

import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

object Math {

    @JvmStatic
    fun solveQuadratic(a: Float, b: Float, c: Float): List<Float> {
        val discriminant = (b.toDouble().pow(2) - 4 * a * c).toFloat()
        return if (discriminant > 0) {
            val sqrtDiscriminant = sqrt(discriminant)
            listOf(
                (-b + sqrtDiscriminant) / (2 * a),
                (-b - sqrtDiscriminant) / (2 * a)
            )
        } else if (discriminant == 0F) {
            listOf(
                -b / (2 * a)
            )
        } else {
            listOf()
        }
    }

    @JvmStatic
    fun random(min: Int, max: Int): Int {
        val rand = Random()
        return rand.nextInt(max - min + 1) + min
    }
}
