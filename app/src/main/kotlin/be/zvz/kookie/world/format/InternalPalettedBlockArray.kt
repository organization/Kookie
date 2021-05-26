package be.zvz.kookie.world.format

import com.koloboke.collect.set.hash.HashObjSets
import kotlin.collections.ArrayList

class InternalPalettedBlockArray<Block> internal constructor(private val bitsPerBlock: Int) : IPalettedBlockArray<Block> {
    internal val blocksPerWord = Int.SIZE_BITS / bitsPerBlock
    internal val blockMask = (1 shl bitsPerBlock) - 1
    internal val wordCount = IPalettedBlockArray.ARRAY_CAPACITY / blocksPerWord + if (IPalettedBlockArray.ARRAY_CAPACITY % blocksPerWord > 0) 1 else 0
    internal val payloadSize = wordCount * Int.SIZE_BYTES
    internal val maxPaletteSize = if (1 shl bitsPerBlock < IPalettedBlockArray.ARRAY_CAPACITY) 1 shl bitsPerBlock else IPalettedBlockArray.ARRAY_CAPACITY
    private val words = IntArray(wordCount)
    private val palette = ArrayList<Block>(maxPaletteSize)
    private var nextPaletteIndex = 0

    internal constructor(bitsPerBlock: Int, block: Block) : this(bitsPerBlock) {
        palette[nextPaletteIndex++] = block
    }

    internal constructor(bitsPerBlock: Int, wordArray: String, paletteEntries: List<Block>) : this(bitsPerBlock) {
        if (wordArray.length != words.size) {
            throw IndexOutOfBoundsException("word array size should be exactly ${words.size} bytes for a ${bitsPerBlock}bpb block array, got ${wordArray.length} bytes")
        }
        if (paletteEntries.size > maxPaletteSize) {
            throw IndexOutOfBoundsException("palette size should be at most $maxPaletteSize entries for a ${bitsPerBlock}bpb block array, got ${paletteEntries.size} entries")
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

    private fun _getPaletteOffset(x: Int, y: Int, z: Int): Int {
        val (wordIdx, shift) = find(x, y, z)
        return (words[wordIdx] shr shift) * bitsPerBlock
    }

    private fun _setPaletteOffset(x: Int, y: Int, z: Int, offset: Int) {
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
        for (x in 0..IPalettedBlockArray.ARRAY_DIM) {
            for (z in 0..IPalettedBlockArray.ARRAY_DIM) {
                for (y in 0..IPalettedBlockArray.ARRAY_DIM) {
                    val inserted = hasFound.add(palette[_getPaletteOffset(x, y, z)])
                    if (inserted && hasFound.size == getPaletteSize()) {
                        break
                    }
                }
            }
        }

        return hasFound.size
    }

    override fun get(x: Int, y: Int, z: Int): Block {
        val offset = _getPaletteOffset(x, y, z)
        return palette[offset]
    }

    override fun set(x: Int, y: Int, z: Int, v: Block): Boolean {
        var offset = -1
        for (i in 0..nextPaletteIndex) {
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

        _setPaletteOffset(x, y, z, offset)
        return true
    }

    override fun getPaletteOffset(x: Int, y: Int, z: Int): Int {
        return _getPaletteOffset(x, y, z)
    }

    override fun replace(offset: Int, value: Block) {
        if (offset >= nextPaletteIndex) {
            throw IllegalArgumentException("offset must be less than palette size $nextPaletteIndex")
        }
        palette[offset] = value
    }

    override fun replaceAll(from: Block, to: Block) {
        for (i in 0..nextPaletteIndex) {
            if (palette[i] == from) {
                palette[i] = to
            }
        }
    }

    override fun convertFrom(otherArray: IPalettedBlockArray<Block>) {
        for (x in 0..IPalettedBlockArray.ARRAY_DIM) {
            for (z in 0..IPalettedBlockArray.ARRAY_DIM) {
                for (y in 0..IPalettedBlockArray.ARRAY_DIM) {
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
