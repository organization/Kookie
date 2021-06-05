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
import com.koloboke.collect.map.hash.HashIntObjMaps
import kotlin.math.max
import kotlin.math.min

interface InventoryHelpers : Inventory {
    override fun contains(item: Item): Boolean {
        var count = max(1, item.count)
        val checkDamage = !item.hasAnyDamageValue()
        val checkTags = item.hasNamedTag()

        getContents().forEach { (_, i) ->
            if (item.equals(i, checkDamage, checkTags)) {
                count -= i.count
                if (count <= 0) {
                    return true
                }
            }
        }

        return false
    }

    override fun all(item: Item): Map<Int, Item> {
        val slots = HashIntObjMaps.newMutableMap<Item>()
        val checkDamage = !item.hasAnyDamageValue()
        val checkTags = item.hasNamedTag()

        getContents().forEach { (index, i) ->
            if (item.equals(i, checkDamage, checkTags)) {
                slots[index] = i
            }
        }

        return slots
    }

    override fun remove(item: Item) {
        val checkDamage = !item.hasAnyDamageValue()
        val checkTags = item.hasNamedTag()

        getContents().forEach { (index, i) ->
            if (item.equals(i, checkDamage, checkTags)) {
                clear(index)
            }
        }
    }

    override fun first(item: Item, exact: Boolean): Int {
        val count = if (exact) item.count else max(1, item.count)
        val checkDamage = !item.hasAnyDamageValue()
        val checkTags = item.hasNamedTag()

        getContents().forEach { (index, i) ->
            if (item.equals(i, checkDamage, checkTags) && (i.count == count || (!exact || i.count > count))) {
                return index
            }
        }

        return -1
    }

    override fun firstEmpty(): Int {
        getContents(true).forEach { (index, slot) ->
            if (slot.isNull()) {
                return index
            }
        }

        return -1
    }

    override fun isSlotEmpty(index: Int): Boolean = getItem(index).isNull()

    override fun canAddItem(item: Item): Boolean {
        var count = item.count
        for (i in 0 until size) {
            val slot = getItem(i)
            if (item.equals(slot)) {
                (min(slot.maxStackSize, item.maxStackSize) - slot.count).let {
                    if (it > 0) {
                        count -= it
                    }
                }
            } else if (slot.isNull()) {
                count -= min(maxStackSize, item.maxStackSize)
            }

            if (count <= 0) {
                return true
            }
        }

        return false
    }

    override fun addItem(vararg slots: Item): MutableList<Item> {
        val itemSlots = mutableListOf<Item>()
        slots.forEach {
            if (!it.isNull()) {
                itemSlots.add(it.clone())
            }
        }

        val emptySlots = mutableListOf<Int>()
        for (i in 0 until size) {
            val item = getItem(i)
            if (item.isNull()) {
                emptySlots.add(i)
            }

            val iterate = itemSlots.listIterator()
            var index = 0
            while (iterate.hasNext()) {
                val slot = iterate.next()
                if (slot.equals(item) && item.count < item.maxStackSize) {
                    val amount = min(item.maxStackSize - item.count, min(slot.count, maxStackSize))
                    if (amount > 0) {
                        slot.count -= amount
                        item.count += amount
                        setItem(index, item)
                        if (slot.count <= 0) {
                            iterate.remove()
                        }
                    }
                }
                index++
            }

            if (itemSlots.size == 0) {
                break
            }
        }

        if (itemSlots.size > 0 && emptySlots.size > 0) {
            emptySlots.forEach { slotIndex ->
                val iterate = itemSlots.listIterator()
                while (iterate.hasNext()) {
                    val slot = iterate.next()
                    val amount = min(slot.maxStackSize, min(slot.count, maxStackSize))
                    slot.count -= amount
                    val item = slot.clone()
                    item.count = amount
                    setItem(slotIndex, item)
                    if (slot.count <= 0) {
                        iterate.remove()
                    }
                }
            }
        }

        return itemSlots
    }

    fun removeItem(vararg slots: Item): MutableList<Item> {
        val itemSlots = mutableListOf<Item>()
        slots.forEach {
            if (!it.isNull()) {
                itemSlots.add(it.clone())
            }
        }

        for (i in 0 until size) {
            val item = getItem(i)
            if (item.isNull()) {
                continue
            }

            val iterate = itemSlots.listIterator()
            var index = 0
            while (iterate.hasNext()) {
                val slot = iterate.next()
                if (slot.equals(item, !slot.hasAnyDamageValue(), slot.hasNamedTag())) {
                    val amount = min(item.count, slot.count)
                    if (amount > 0) {
                        slot.count -= amount
                        item.count -= amount
                        setItem(index, item)
                        if (slot.count <= 0) {
                            iterate.remove()
                        }
                    }
                }
                index++
            }

            if (itemSlots.size == 0) {
                break
            }
        }

        return itemSlots
    }

    override fun clear(index: Int) {
        setItem(index, ItemFactory.air())
    }

    override fun clearAll() {
        setContents(HashIntObjMaps.newMutableMap())
    }

    override fun swap(slot1: Int, slot2: Int) {
        val i1 = getItem(slot1)
        val i2 = getItem(slot2)
        setItem(slot1, i2)
        setItem(slot2, i1)
    }
}
