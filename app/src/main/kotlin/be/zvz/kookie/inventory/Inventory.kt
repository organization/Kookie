package be.zvz.kookie.inventory

import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

interface Inventory {
    fun getSize(): Int
    fun getMaxStackSize(): Int
    fun getItem(index: Int): Item
    fun setItem(index: Int, item: Item)
    fun addItem(vararg slots: Item): MutableList<Item>
    fun canAddItem(item: Item): Boolean
    fun removeItem(vararg slots: Item): MutableList<Item>
    fun getContents(includeEmpty: Boolean = false): MutableMap<Int, Item>
    fun setContents(items: MutableList<Item>)
    fun contains(item: Item): Boolean
    fun all(item: Item): MutableMap<Int, Item>
    fun first(item: Item, exact: Boolean): Int
    fun firstEmpty(): Int
    fun isSlotEmpty(index: Int): Boolean
    fun remove(item: Item)
    fun clear(index: Int)
    fun clearAll()
    fun swap(slot1: Int, slot2: Int)
    fun onOpen(who: Player)
    fun onClose(who: Player)
    fun slotExists(slot: Int): Boolean
    val listeners: MutableList<InventoryListener>
    val viewers: MutableList<Player>

    companion object {
        val MAX_STACK: Int = 64
    }
}
