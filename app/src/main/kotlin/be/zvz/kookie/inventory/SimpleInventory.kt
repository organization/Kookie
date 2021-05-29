package be.zvz.kookie.inventory

import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory

class SimpleInventory : BaseInventory() {
    private lateinit var slots: Array<Item?>
    fun SimpleInventory(size: Int) {
        slots = Array(size) { null }
    }

    override fun getSize(): Int = slots.size
    override fun getItem(index: Int): Item = slots[index]?.clone() ?: ItemFactory.air()
    override fun getContents(includeEmpty: Boolean): MutableMap<Int, Item> {
        val contents = mutableMapOf<Int, Item>()
        slots.forEachIndexed { index, item ->
            if (item != null) {
                contents[index] = item.clone()
            } else if (includeEmpty) {
                contents[index] = ItemFactory.air()
            }
        }
        return contents
    }

    protected override fun internalSetContents(items: MutableMap<Int, Item>) {
        for (i in 0..getSize()) {
            if (items.containsKey(i)) {
                clear(i)
            } else {
                setItem(i, items[i]!!)
            }
        }
    }

    protected override fun internalSetItem(index: Int, item: Item) {
        slots[index] = if (item.isNull()) null else item
    }
}
