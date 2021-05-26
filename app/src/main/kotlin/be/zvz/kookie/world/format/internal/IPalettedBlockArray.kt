package be.zvz.kookie.world.format.internal

interface IPalettedBlockArray<Block> : Cloneable {
    fun getWordArray(): CharArray
    fun getPalette(): List<Block>
    fun getPaletteSize(): Int
    fun getMaxPaletteSize(): Int
    fun getBitsPerBlock(): Int
    fun get(x: Int, y: Int, z: Int): Block
    fun set(x: Int, y: Int, z: Int, v: Block): Boolean
    fun getPaletteOffset(x: Int, y: Int, z: Int): Int
    fun replace(offset: Int, value: Block)
    fun replaceAll(from: Block, to: Block)
    fun convertFrom(otherArray: IPalettedBlockArray<Block>)
    fun fastUpsize(otherArray: IPalettedBlockArray<Block>)
    public override fun clone(): IPalettedBlockArray<*> {
        return super.clone() as IPalettedBlockArray<*>
    }

    companion object {
        const val COORD_BIT_SIZE = 4
        const val ARRAY_DIM = 1 shl COORD_BIT_SIZE
        const val ARRAY_CAPACITY = ARRAY_DIM * ARRAY_DIM * ARRAY_DIM
    }
}
