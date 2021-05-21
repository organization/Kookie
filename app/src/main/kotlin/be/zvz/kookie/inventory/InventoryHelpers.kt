package be.zvz.kookie.inventory

import be.zvz.kookie.item.Item

interface InventoryHelpers<T : Item> {
    fun getMaxStackSize(): Int
    fun getSize(): Int
    fun getItem(index: Int): T
    fun setItem(index: Int, item: T)
    fun getContents(includeEmpty: Boolean): MutableList<T>
}
