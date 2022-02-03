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

class BiomeArray(var payload: String) : Cloneable {
    init {
        if (payload.length != 256) {
            throw IllegalArgumentException("Biome array is expected to be exactly 256 bytes")
        }
    }

    operator fun get(index: Int): Int = payload[index].code
    operator fun get(x: Int, z: Int): Int = payload[idx(x, z)].code

    operator fun set(index: Int, biomeId: Int) {
        if (biomeId !in 0 until 256) {
            throw IllegalArgumentException("Biome ID must be in the range 0-255")
        }

        payload = payload.replaceRange(index..index, biomeId.toString())
    }

    operator fun set(x: Int, z: Int, biomeId: Int) {
        this[idx(x, z)] = biomeId
    }

    public override fun clone() = BiomeArray(payload)

    companion object {
        @JvmStatic
        fun fill(biomeId: Int): BiomeArray = BiomeArray(biomeId.toString().repeat(256))

        @JvmStatic
        fun idx(x: Int, z: Int): Int =
            if (x in 0..15 && z in 0..15) {
                z shl 4 or x
            } else {
                throw IllegalArgumentException("x and z must be in the range 0-15")
            }
    }
}
