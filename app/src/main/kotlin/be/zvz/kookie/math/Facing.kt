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
package be.zvz.kookie.math

enum class Facing(val value: Int) {

    DOWN(Axis.Y.value shl 1),
    UP(Axis.Y.value shl 1 or Facing.FLAG_AXIS_POSITIVE),
    NORTH(Axis.Z.value shl 1),
    SOUTH(Axis.Z.value shl 1 or Facing.FLAG_AXIS_POSITIVE),
    WEST(Axis.X.value shl 1),
    EAST(Axis.X.value shl 1 or Facing.FLAG_AXIS_POSITIVE),
    CENTER(-1);

    companion object {
        private const val FLAG_AXIS_POSITIVE = 1
        val ALL = arrayOf(
            DOWN,
            UP,
            NORTH,
            SOUTH,
            WEST,
            EAST
        )

        val HORIZONTAL = arrayOf(
            NORTH,
            SOUTH,
            WEST,
            EAST
        )

        val CLOCKWISE = mapOf(
            Axis.Y to mapOf(
                NORTH to EAST,
                EAST to SOUTH,
                SOUTH to WEST,
                WEST to NORTH
            ),
            Axis.Z to mapOf(
                UP to EAST,
                EAST to DOWN,
                DOWN to WEST,
                WEST to UP
            ),
            Axis.X to mapOf(
                UP to NORTH,
                NORTH to DOWN,
                DOWN to SOUTH,
                SOUTH to UP
            )
        )

        @JvmStatic fun axis(direction: Facing): Int = axis(direction.value)
        @JvmStatic fun axis(direction: Int): Int = direction shr 1

        @JvmStatic fun isPositive(direction: Facing): Boolean = isPositive(direction.value)
        @JvmStatic fun isPositive(direction: Int): Boolean = (direction and FLAG_AXIS_POSITIVE) == FLAG_AXIS_POSITIVE

        @JvmStatic fun opposite(direction: Facing): Int = opposite(direction.value)
        @JvmStatic fun opposite(direction: Int): Int = direction xor FLAG_AXIS_POSITIVE

        @JvmStatic
        fun rotate(direction: Facing, axis: Axis, clockWise: Boolean): Int =
            CLOCKWISE[axis]?.let { map ->
                map[direction]?.let { rotated ->
                    return if (clockWise) {
                        rotated.value
                    } else {
                        opposite(rotated.value)
                    }
                } ?: throw IllegalArgumentException("Cannot rotate direction ${direction.value} around axis ${axis.value}")
            } ?: throw IllegalArgumentException("Invalid axis ${direction.value}")

        @JvmStatic
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: UP
    }
}
