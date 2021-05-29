/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.inventory

import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.player.Player

abstract class BaseInventory : InventoryHelpers, Inventory {
    private val maxStackSize: Int = Inventory.MAX_STACK
    override val viewers = mutableListOf<Player>()

    override fun getMaxStackSize(): Int = maxStackSize
    protected abstract fun internalSetContents(items: MutableMap<Int, Item>)
    override fun setContents(items: MutableMap<Int, Item>) {
        // TODO: items.size > getSize()
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

    protected abstract fun internalSetItem(index: Int, item: Item)

    override fun setItem(index: Int, item: Item) {
        var newItem =
            if (item.isNull()) {
                ItemFactory.air()
            } else {
                item.clone()
            }
        val oldItem = getItem(index)
        internalSetItem(index, newItem)
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

    protected fun onContentChange(itemsBefore: MutableMap<Int, Item>) {
        listeners.forEach {
            it.onSlotChange(this, itemsBefore)
        }
        viewers.forEach {
            TODO("Not yet implemented")
        }
    }

    override fun slotExists(slot: Int) = slot in 0 until getSize()
}
