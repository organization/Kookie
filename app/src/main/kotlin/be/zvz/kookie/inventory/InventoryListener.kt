package be.zvz.kookie.inventory

import be.zvz.kookie.item.Item

interface InventoryListener {
    fun onSlotChange(inventory: Inventory, slot: Int, oldItem: Item)
    fun onSlotChange(inventory: Inventory, oldContents: MutableList<Item>)
}
