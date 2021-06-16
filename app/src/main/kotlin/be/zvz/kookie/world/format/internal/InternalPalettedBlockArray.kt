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
package be.zvz.kookie.world.format.internal

import com.koloboke.collect.set.hash.HashObjSets

class InternalPalettedBlockArray<Block> internal constructor(private val bitsPerBlock: Int) :
    IPalettedBlockArray<Block> {
    internal val blocksPerWord = Int.SIZE_BITS / bitsPerBlock
    internal val blockMask = (1 shl bitsPerBlock) - 1
    internal val wordCount =
        IPalettedBlockArray.ARRAY_CAPACITY / blocksPerWord +
            if (IPalettedBlockArray.ARRAY_CAPACITY % blocksPerWord > 0) 1 else 0
    internal val payloadSize = wordCount * Int.SIZE_BYTES
    internal val maxPaletteSize = if (1 shl bitsPerBlock < IPalettedBlockArray.ARRAY_CAPACITY) {
        1 shl bitsPerBlock
    } else {
        IPalettedBlockArray.ARRAY_CAPACITY
    }
    private val words = IntArray(wordCount)
    private val palette = ArrayList<Block>(maxPaletteSize)
    private var nextPaletteIndex = 0

    internal constructor(bitsPerBlock: Int, block: Block) : this(bitsPerBlock) {
        palette[nextPaletteIndex++] = block
    }

    internal constructor(bitsPerBlock: Int, wordArray: String, paletteEntries: List<Block>) : this(bitsPerBlock) {
        if (wordArray.length != words.size) {
            throw IndexOutOfBoundsException(
                "word array size should be exactly ${words.size} bytes for " +
                    "a ${bitsPerBlock}bpb block array, got ${wordArray.length} bytes"
            )
        }
        if (paletteEntries.size > maxPaletteSize) {
            throw IndexOutOfBoundsException(
                "palette size should be at most $maxPaletteSize entries for " +
                    "a ${bitsPerBlock}bpb block array, got ${paletteEntries.size} entries"
            )
        }
        if (paletteEntries.isEmpty()) {
            throw IndexOutOfBoundsException("palette cannot have a zero size")
        }
        wordArray.forEachIndexed { index, value ->
            words[index] = value.code
        }
        paletteEntries.forEachIndexed { index, value ->
            palette[index] = value
        }
        nextPaletteIndex = paletteEntries.size
    }

    private fun getArrayOffset(x: Int, y: Int, z: Int): Int =
        x shl IPalettedBlockArray.COORD_BIT_SIZE * 2 or (z shl IPalettedBlockArray.COORD_BIT_SIZE) or y

    private fun find(x: Int, y: Int, z: Int): Pair<Int, Int> {
        val idx = getArrayOffset(x, y, z)
        return Pair(idx / blocksPerWord, idx % blocksPerWord * bitsPerBlock)
    }

    private fun internalGetPaletteOffset(x: Int, y: Int, z: Int): Int {
        val (wordIdx, shift) = find(x, y, z)
        return (words[wordIdx] shr shift) * bitsPerBlock
    }

    private fun internalSetPaletteOffset(x: Int, y: Int, z: Int, offset: Int) {
        val (wordIdx, shift) = find(x, y, z)
        words[wordIdx] = words[wordIdx] and (blockMask shl shift).inv() or offset shl shift
    }

    override fun getWordArray(): CharArray = CharArray(wordCount).apply {
        words.forEachIndexed { index, value ->
            this[index] = value.toChar()
        }
    }

    override fun getPalette(): List<Block> = palette

    override fun getPaletteSize(): Int = nextPaletteIndex

    override fun getMaxPaletteSize(): Int = maxPaletteSize

    override fun getBitsPerBlock(): Int = bitsPerBlock

    fun countUniqueBlocks(): Int {
        val hasFound: MutableSet<Block> = HashObjSets.newMutableSet()
        repeat(IPalettedBlockArray.ARRAY_DIM) { x ->
            repeat(IPalettedBlockArray.ARRAY_DIM) { z ->
                repeat(IPalettedBlockArray.ARRAY_DIM) { y ->
                    val inserted = hasFound.add(palette[internalGetPaletteOffset(x, y, z)])
                    if (inserted && hasFound.size == getPaletteSize()) {
                        return hasFound.size
                    }
                }
            }
        }

        return hasFound.size
    }

    override fun get(x: Int, y: Int, z: Int): Block = palette[internalGetPaletteOffset(x, y, z)]

    override fun set(x: Int, y: Int, z: Int, v: Block): Boolean {
        var offset = -1
        for (i in 0 until nextPaletteIndex) {
            if (palette[i] == v) {
                offset = i
                break
            }
        }

        if (offset == -1) {
            if (nextPaletteIndex >= maxPaletteSize) {
                return false
            }
            offset = nextPaletteIndex++
            palette[offset] = v
        }

        internalSetPaletteOffset(x, y, z, offset)
        return true
    }

    override fun getPaletteOffset(x: Int, y: Int, z: Int): Int = internalGetPaletteOffset(x, y, z)

    override fun replace(offset: Int, value: Block) {
        if (offset >= nextPaletteIndex) {
            throw IllegalArgumentException("offset must be less than palette size $nextPaletteIndex")
        }
        palette[offset] = value
    }

    override fun replaceAll(from: Block, to: Block) {
        repeat(nextPaletteIndex) {
            if (palette[it] == from) {
                palette[it] = to
            }
        }
    }

    override fun convertFrom(otherArray: IPalettedBlockArray<Block>) {
        repeat(IPalettedBlockArray.ARRAY_DIM) { x ->
            repeat(IPalettedBlockArray.ARRAY_DIM) { z ->
                repeat(IPalettedBlockArray.ARRAY_DIM) { y ->
                    if (!set(x, y, z, otherArray.get(x, y, z))) {
                        throw IndexOutOfBoundsException("out of palette space")
                    }
                }
            }
        }
    }

    override fun fastUpsize(otherArray: IPalettedBlockArray<Block>) {
        val otherPalette = otherArray.getPalette()
        nextPaletteIndex = otherPalette.size
        otherPalette.forEachIndexed { index, value ->
            palette[index] = value
        }
    }

    override fun clone(): InternalPalettedBlockArray<*> = super.clone() as InternalPalettedBlockArray<*>
}
