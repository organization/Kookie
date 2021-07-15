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

import be.zvz.kookie.world.format.internal.IPalettedBlockArray

class PalettedBlockArray internal constructor(blockArray: BlockArrayContainer<Long>) : IPalettedBlockArray<Long> by blockArray {
    constructor(fillEntry: Long) : this(BlockArrayContainer(fillEntry)) {
        if (fillEntry != fillEntry.toUInt().toLong()) {
            throw IllegalArgumentException("value $fillEntry is too large to be used as a palette entry")
        }
    }

    companion object {
        @JvmStatic
        fun fromData(bitsPerBlock: Int, wordArray: String, palette: List<Long>): BlockArrayContainer<Long> =
            BlockArrayContainer(bitsPerBlock, wordArray, palette)

        @JvmStatic
        fun getExpectedWordArraySize(bitsPerBlock: Long): Int =
            if (bitsPerBlock == bitsPerBlock.toUInt().toLong()) {
                BlockArrayContainer.getExpectedPayloadSize(bitsPerBlock.toInt())
            } else {
                throw IllegalArgumentException("invalid bits-per-block: $bitsPerBlock")
            }
    }
}
