package be.zvz.kookie.block

object BlockFactory {
    private val fullList = mutableListOf<Block>()

    fun fromFullBlock(fullState: Int): Block = get(fullState shr 4, fullState and 0xf)

    fun get(id: Int, meta: Int): Block {
        if (meta !in 0..0xf) {
            throw IllegalArgumentException("Block meta value $meta is out of bounds")
        }

        return try {
            val index = (id shl 4) or meta

            fullList[index].clone()
        } catch (err: RuntimeException) {
            throw IllegalArgumentException("Block ID $id is out of bounds")
        }
    }
}
