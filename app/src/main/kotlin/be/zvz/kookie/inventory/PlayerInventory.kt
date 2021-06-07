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

import be.zvz.kookie.entity.Human
import be.zvz.kookie.item.Item

class PlayerInventory(val holder: Human) : SimpleInventory(36) {
    protected var itemInHandIndex = 0
    val heldItemIndexChangeListeners = mutableListOf<(oldIndex: Int) -> Unit>()

    fun isHotbarSlot(slot: Int): Boolean = slot >= 0 && slot <= getHotbarSize()
    fun throwIfNotHotbarSlot(slot: Int) {
        if (!isHotbarSlot(slot)) {
            throw IllegalArgumentException("$slot is not a valid hotbar slot index (expected 0 - " + (getHotbarSize() - 1) + ")")
        }
    }

    fun getHotbarSlotItem(hotbarSlot: Int): Item {
        throwIfNotHotbarSlot(hotbarSlot)
        return getItem(hotbarSlot)
    }

    fun getHeldItemIndex(): Int = itemInHandIndex

    fun setHeldItemIndex(hotbarSlot: Int) {
        throwIfNotHotbarSlot(hotbarSlot)

        val oldIndex = itemInHandIndex
        itemInHandIndex = hotbarSlot

        heldItemIndexChangeListeners.forEach {
            it(oldIndex)
        }
    }

    fun getItemInHand(): Item = getHotbarSlotItem(itemInHandIndex)
    fun setItemInHand(item: Item) {
        setItem(getHeldItemIndex(), item)
    }

    fun getHotbarSize(): Int = 9
}
