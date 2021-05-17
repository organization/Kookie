package be.zvz.kookie.item

class ItemIdentifier(val id: Int, meta: Int) {
    val meta: Int

    init {
        if (id < -0x8000 || id > 0x7fff) {
            throw IllegalArgumentException("ID must be in range " + -0x8000 + " - " + 0x7fff)
        }

        this.meta = if (meta != -1) meta and 0x7FFF else -1
    }
}