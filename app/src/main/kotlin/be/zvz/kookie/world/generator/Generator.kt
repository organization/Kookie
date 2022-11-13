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
package be.zvz.kookie.world.generator

import be.zvz.kookie.world.ChunkManager
import kotlin.random.Random

abstract class Generator(
    protected val seed: Long,
    protected val preset: String
) {
    protected var random = Random(seed)

    abstract fun generateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int)

    abstract fun populateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int)

    companion object {
        fun convertSeed(seed: String): Long? = when {
            seed == "" -> null
            seed.matches(Regex("^-?\\d+\$")) -> seed.toLong()
            else -> javaStringHash(seed)
        }

        private fun javaStringHash(seed: String): Long {
            var hash = 0L
            for (i in seed.indices) {
                var ord = seed[i].code
                if (ord and 0x80 != 0) {
                    ord -= 0x100
                }
                hash = hash * 31 + ord
                while (hash > 0x7FFFFFFF) {
                    hash -= 0x100000000
                }
                while (hash < -0x80000000) {
                    hash += 0x100000000
                }
                hash = hash and 0xFFFFFFFF
            }
            return hash
        }
    }
}
