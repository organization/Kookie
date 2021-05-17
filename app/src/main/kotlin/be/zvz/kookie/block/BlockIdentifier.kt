package be.zvz.kookie.block

class BlockIdentifier(val blockId: Int, val variant: Int, itemId: Int?, val tileClass: String?) {
    val itemId: Int? = itemId
        get() = field ?: if (blockId > 255) 255 - blockId else blockId

    fun getAllBlockIds(): List<Int> = listOf(blockId)
}
