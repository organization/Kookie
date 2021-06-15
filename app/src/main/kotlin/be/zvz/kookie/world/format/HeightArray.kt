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
package be.zvz.kookie.world.format

class HeightArray @JvmOverloads constructor(val values: MutableList<Int> = mutableListOf()) {
    init {
        if (values.size != 256) {
            throw IllegalArgumentException("Expected exactly 256 values")
        }
    }

    fun get(x: Int, z: Int): Int = values[idx(x, z)]

    fun set(x: Int, z: Int, height: Int) {
        values[idx(x, z)] = height
    }

    fun clone(): HeightArray = HeightArray(values)

    companion object {
        @JvmStatic
        fun fill(value: Int): HeightArray = HeightArray(MutableList(255) { value })

        private fun idx(x: Int, z: Int): Int =
            if (x in 0 until 16 && z in 0 until 16) {
                (z shl 4) or x
            } else {
                throw IllegalArgumentException("x and z must be in the range 0-15")
            }
    }
}
