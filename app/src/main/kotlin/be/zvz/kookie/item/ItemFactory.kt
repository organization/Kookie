package be.zvz.kookie.item

object ItemFactory {
    val list: MutableList<Item> = mutableListOf()
    init {
        // TODO
    }
    fun register(item: Item, override: Boolean = false) {
        val id: Int = item.getId()
    }
}
