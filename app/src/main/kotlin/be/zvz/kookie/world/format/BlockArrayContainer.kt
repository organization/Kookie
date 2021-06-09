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
import be.zvz.kookie.world.format.internal.InternalPalettedBlockArray

class BlockArrayContainer<Block> private constructor(var blockArray: IPalettedBlockArray<Block>) {

    constructor(block: Block) : this(InternalPalettedBlockArray(1, block))
    constructor(capacity: Int) : this(blockArrayFromCapacity(capacity))
    constructor(
        bitsPerBlock: Int,
        wordArray: String,
        paletteEntries: List<Block>
    ) : this(
        blockArrayFromData(
            bitsPerBlock,
            wordArray,
            paletteEntries
        )
    )
    constructor(other: BlockArrayContainer<Block>) : this(other.blockArray.clone() as IPalettedBlockArray<Block>)

    fun getWordArray(): CharArray = blockArray.getWordArray()
    fun getPalette(): List<Block> = blockArray.getPalette()
    fun getMaxPaletteSize(): Int = blockArray.getMaxPaletteSize()
    fun getBitsPerBlock(): Int = blockArray.getBitsPerBlock()
    fun get(x: Int, y: Int, z: Int): Block = blockArray.get(x, y, z)
    fun set(x: Int, y: Int, z: Int, v: Block) {
        if (!blockArray.set(x, y, z, v)) {
            val count = blockArray.getPaletteSize()
            if (count < IPalettedBlockArray.ARRAY_CAPACITY) {
                val newArray = blockArrayFromCapacity<Block>(count + 1)
                newArray.fastUpsize(blockArray)
                newArray.set(x, y, z, v)
                blockArray = newArray
            } else {
                blockArray.replace(blockArray.getPaletteOffset(x, y, z), v)
            }
        }
    }

    fun replace(offset: Int, v: Block) {
        blockArray.replace(offset, v)
    }

    fun replaceAll(oldVal: Block, newVal: Block) {
        blockArray.replaceAll(oldVal, newVal)
    }

    companion object {
        private val cache1 = InternalPalettedBlockArray<Long>(1)
        private val cache2 = InternalPalettedBlockArray<Long>(2)
        private val cache3 = InternalPalettedBlockArray<Long>(3)
        private val cache4 = InternalPalettedBlockArray<Long>(4)
        private val cache5 = InternalPalettedBlockArray<Long>(5)
        private val cache6 = InternalPalettedBlockArray<Long>(6)
        private val cache8 = InternalPalettedBlockArray<Long>(8)
        private val cache16 = InternalPalettedBlockArray<Long>(16)

        private fun <Block> blockArrayFromData(
            bitsPerBlock: Int,
            wordArray: String,
            paletteEntries: List<Block>
        ): IPalettedBlockArray<Block> =
            when (bitsPerBlock) {
                1 -> InternalPalettedBlockArray(1, wordArray, paletteEntries)
                2 -> InternalPalettedBlockArray(2, wordArray, paletteEntries)
                3 -> InternalPalettedBlockArray(3, wordArray, paletteEntries)
                4 -> InternalPalettedBlockArray(4, wordArray, paletteEntries)
                5 -> InternalPalettedBlockArray(5, wordArray, paletteEntries)
                6 -> InternalPalettedBlockArray(6, wordArray, paletteEntries)
                8 -> InternalPalettedBlockArray(8, wordArray, paletteEntries)
                16 -> InternalPalettedBlockArray(16, wordArray, paletteEntries)
                else -> throw IllegalArgumentException("invalid bits-per-block: $bitsPerBlock")
            }
        private fun <Block> blockArrayFromCapacity(capacity: Int): IPalettedBlockArray<Block> = when {
            capacity <= cache1.maxPaletteSize -> InternalPalettedBlockArray(1)
            capacity <= cache2.maxPaletteSize -> InternalPalettedBlockArray(2)
            capacity <= cache3.maxPaletteSize -> InternalPalettedBlockArray(3)
            capacity <= cache4.maxPaletteSize -> InternalPalettedBlockArray(4)
            capacity <= cache5.maxPaletteSize -> InternalPalettedBlockArray(5)
            capacity <= cache6.maxPaletteSize -> InternalPalettedBlockArray(6)
            capacity <= cache8.maxPaletteSize -> InternalPalettedBlockArray(8)
            capacity <= cache16.maxPaletteSize -> InternalPalettedBlockArray(16)
            else -> throw IllegalArgumentException("invalid capacity specified: $capacity")
        }

        fun getExpectedPayloadSize(bitsPerBlock: Int): Int =
            when (bitsPerBlock) {
                1 -> cache1.payloadSize
                2 -> cache2.payloadSize
                3 -> cache3.payloadSize
                4 -> cache4.payloadSize
                5 -> cache5.payloadSize
                6 -> cache6.payloadSize
                8 -> cache8.payloadSize
                16 -> cache16.payloadSize
                else -> throw IllegalArgumentException("invalid bits-per-block: $bitsPerBlock")
            }
    }
}
