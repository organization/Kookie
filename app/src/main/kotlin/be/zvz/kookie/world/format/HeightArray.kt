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

class HeightArray(val values: MutableList<Int> = mutableListOf()) {
    init {
        if (values.size != 256) {
            throw IllegalArgumentException("Expected exactly 256 values")
        }
    }

    operator fun get(index: Int): Int = values[index]
    operator fun get(x: Int, z: Int): Int = values[idx(x, z)]

    operator fun set(index: Int, height: Int) {
        values[index] = height
    }

    operator fun set(x: Int, z: Int, height: Int) {
        values[idx(x, z)] = height
    }

    fun clone(): HeightArray {
        return HeightArray(values)
    }

    companion object {
        fun fill(value: Int): HeightArray {
            val list = MutableList(255) { value }
            return HeightArray(list)
        }

        private fun idx(x: Int, z: Int): Int {
            if (x !in 0 until 16 || z !in 0 until 16) {
                throw IllegalArgumentException("x and z must be in the range 0-15")
            }

            return (z shl 4) or x
        }
    }
}
