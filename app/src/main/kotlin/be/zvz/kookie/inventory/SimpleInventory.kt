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

open class SimpleInventory(size: Int) : BaseInventory() {
    private var slots: Array<Item?> = Array(size) { null }

    override fun getSize(): Int = slots.size
    override fun getItem(index: Int): Item = slots[index]?.clone() ?: ItemFactory.air()
    override fun getContents(includeEmpty: Boolean): Map<Int, Item> {
        val contents = HashIntObjMaps.newMutableMap<Item>()
        slots.forEachIndexed { index, item ->
            if (item != null) {
                contents[index] = item.clone()
            } else if (includeEmpty) {
                contents[index] = ItemFactory.air()
            }
        }
        return contents
    }

    protected override fun internalSetContents(items: Map<Int, Item>) {
        for (i in 0..getSize()) {
            if (items.containsKey(i)) {
                clear(i)
            } else {
                setItem(i, items.getValue(i))
            }
        }
    }

    protected override fun internalSetItem(index: Int, item: Item) {
        slots[index] = if (item.isNull()) null else item
    }
}
