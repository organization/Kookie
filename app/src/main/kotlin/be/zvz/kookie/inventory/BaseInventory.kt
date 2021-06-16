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

abstract class BaseInventory : InventoryHelpers {
    override val maxStackSize: Int = Inventory.MAX_STACK
    private val viewers = mutableListOf<Player>()
    private val listeners = mutableListOf<InventoryListener>()

    protected abstract fun internalSetContents(items: Map<Int, Item>)
    override fun setContents(items: Map<Int, Item>) {
        // TODO: items.size > getSize()
        val oldContents = getContents(true)

        val listeners = listeners.toTypedArray()
        this.listeners.clear()
        listeners.forEach(this.listeners::add)

        val viewers = viewers.toTypedArray()
        this.viewers.clear()
        viewers.forEach(this.viewers::add)

        internalSetContents(items)

        onContentChange(oldContents)
    }

    protected abstract fun internalSetItem(index: Int, item: Item)

    override fun setItem(index: Int, item: Item) {
        val newItem =
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

    protected fun onContentChange(itemsBefore: Map<Int, Item>) {
        listeners.forEach {
            it.onSlotChange(this, itemsBefore)
        }
        viewers.forEach {
            TODO("Not yet implemented")
        }
    }

    override fun slotExists(slot: Int) = slot in 0 until size
    override fun getViewers(): MutableList<Player> = viewers
    override fun getListeners(): MutableList<InventoryListener> = listeners
}
