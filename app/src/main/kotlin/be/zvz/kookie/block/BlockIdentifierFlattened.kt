package be.zvz.kookie.block

class BlockIdentifierFlattened(blockId: Int, val additionalIds: List<Int>, variant: Int, itemId: Int? = null, tileClass: String? = null) : BlockIdentifier(blockId, variant, itemId, tileClass) {

    init {
        if (additionalIds.isEmpty()) {
            throw IllegalArgumentException("Expected at least 1 additional ID")
        }
    }

    fun getSecondId(): Int = additionalIds[0]

    override fun getAllBlockIds(): List<Int> {
        val list: MutableList<Int> = mutableListOf(blockId)
        additionalIds.forEach {
            list.add(it)
        }
        return list.toList()
    }
}