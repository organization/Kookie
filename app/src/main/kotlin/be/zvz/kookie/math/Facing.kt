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

    val axis: Axis = Axis.fromInt(value shr 1)
    val opposite: Facing by lazy { fromInt(value xor FLAG_AXIS_POSITIVE) }
    val isPositive: Boolean by lazy { value and FLAG_AXIS_POSITIVE == FLAG_AXIS_POSITIVE }

    fun rotate(axis: Axis, clockWise: Boolean): Facing =
        CLOCKWISE[axis]?.let { map ->
            map[this]?.let { rotated ->
                return if (clockWise) {
                    rotated
                } else {
                    rotated.opposite
                }
            } ?: throw IllegalArgumentException("Cannot rotate direction $value around axis ${axis.value}")
        } ?: throw IllegalArgumentException("Invalid axis $value")

    companion object {
        private const val FLAG_AXIS_POSITIVE = 1
        private val VALUES = values()

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

        @Deprecated("Incorrect usage", ReplaceWith("direction.axis"))
        @JvmStatic fun axis(direction: Facing): Axis = direction.axis
        @JvmStatic fun axis(direction: Int): Axis = fromInt(direction).axis

        @Deprecated("Incorrect usage", ReplaceWith("direction.opposite"))
        @JvmStatic fun opposite(direction: Facing): Facing = direction.opposite
        @JvmStatic fun opposite(direction: Int): Facing = fromInt(direction).opposite

        @Deprecated("Incorrect usage", ReplaceWith("direction.isPositive"))
        @JvmStatic fun isPositive(direction: Facing): Boolean = direction.isPositive
        @JvmStatic fun isPositive(direction: Int): Boolean = fromInt(direction).isPositive

        @Deprecated("Incorrect usage", ReplaceWith("direction.rotate(axis, clockWise)"))
        @JvmStatic fun rotate(direction: Facing, axis: Axis, clockWise: Boolean): Facing = direction.rotate(axis, clockWise)
        @JvmStatic fun rotate(direction: Int, axis: Axis, clockWise: Boolean): Facing = fromInt(direction).rotate(axis, clockWise)

        @JvmStatic
        fun fromInt(value: Int) = VALUES.find { it.value == value } ?: UP
    }
}
