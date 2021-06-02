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
import be.zvz.kookie.player.Player

interface Inventory {
    val maxStackSize: Int
    val size: Int
    fun getItem(index: Int): Item
    fun setItem(index: Int, item: Item)
    fun addItem(vararg slots: Item): MutableList<Item>
    fun canAddItem(item: Item): Boolean
    fun removeItem(vararg slots: Item): MutableList<Item>
    fun getContents(includeEmpty: Boolean = false): MutableList<Item>
    fun setContents(items: MutableList<Item>)
    fun contains(item: Item): Boolean
    fun all(item: Item): MutableList<Item>
    fun first(item: Item, exact: Boolean = false): Int
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
