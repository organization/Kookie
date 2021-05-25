package be.zvz.kookie.world.format

class PalettedBlockArray private constructor(private val blockArray: BlockArrayContainer<Long>) {
    constructor(fillEntry: Long) : this(BlockArrayContainer(fillEntry)) {
        checkPaletteEntrySize(fillEntry)
    }

    private fun checkPaletteEntrySize(v: Long) {
        if (v != v.toUInt().toLong()) {
            throw IllegalArgumentException("value $v is too large to be used as a palette entry")
        }
    }

    fun getWordArray(): String = String(blockArray.getWordArray())
    fun getPalette(): List<Long> = blockArray.getPalette()
    fun getMaxPaletteSize(): Int = blockArray.getMaxPaletteSize()
    fun getBitsPerBlock(): Int = blockArray.getBitsPerBlock()
    fun get(x: Int, y: Int, z: Int): Long = blockArray.get(x, y, z)

    fun set(x: Int, y: Int, z: Int, v: Long) {
        blockArray.set(x, y, z, v)
    }

    fun replace(offset: Int, v: Long) {
        blockArray.replace(offset, v)
    }

    fun replaceAll(oldVal: Long, newVal: Long) {
        blockArray.replaceAll(oldVal, newVal)
    }

    companion object {
        fun fromData(bitsPerBlock: Int, wordArray: String, palette: List<Long>): BlockArrayContainer<Long> =
            BlockArrayContainer(bitsPerBlock, wordArray, palette)

        fun getExpectedWordArraySize(bitsPerBlock: Long): Int {
            if (bitsPerBlock != bitsPerBlock.toUInt().toLong()) {
                throw IllegalArgumentException("invalid bits-per-block: $bitsPerBlock")
            }
            return BlockArrayContainer.getExpectedPayloadSize(bitsPerBlock.toInt())
        }
    }
}
