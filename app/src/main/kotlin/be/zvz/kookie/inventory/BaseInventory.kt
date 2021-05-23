package be.zvz.kookie.inventory

import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

abstract class BaseInventory : Inventory, InventoryHelpers<Item> {
    override val maxStackSize: Int = Inventory.MAX_STACK
    override val viewers = mutableListOf<Player>()

    abstract fun internalSetContents(items: MutableList<Item>)
    override fun setContents(items: MutableList<Item>) {
        if (items.size > size) {
            items.subList(0, size)
        }
        val oldContents = getContents(true)
        val listeners = listeners.toTypedArray()
        this.listeners.clear()
        val viewers = viewers.toTypedArray()
        this.viewers.clear()

        internalSetContents(items)

        listeners.forEach {
            this.listeners.add(it)
        }
        viewers.forEach {
            this.viewers.add(it)
        }

        onContentChange(oldContents)
    }

    abstract fun internalSetItem(index: Int, item: Item)

    override fun setItem(index: Int, item: Item) {
        var checkedItem =
            if (item.isNull()) {
                TODO("Not yet implemented")
            } else {
                item.clone()
            }
        val oldItem = getItem(index)
        internalSetItem(index, item)
        onSlotChange(index, oldItem)
    }

    override fun onOpen(who: Player) {
        viewers.add(who)
    }

    override fun onClose(who: Player) {
        viewers.remove(who)
    }

    protected fun onSlotChange(index: Int, before: Item) {
        listeners.forEach {
            it.onSlotChange(this, index, before)
        }
        viewers.forEach {
            TODO("Not yet implemented")
        }
    }

    protected fun onContentChange(itemsBefore: MutableList<Item>) {
        listeners.forEach {
            it.onSlotChange(this, itemsBefore)
        }
        viewers.forEach {
            TODO("Not yet implemented")
        }
    }

    override fun slotExists(slot: Int) = slot in 0 until size
}
