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
package be.zvz.kookie.item

import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.nbt.tag.CompoundTag
import com.koloboke.collect.map.hash.HashIntObjMaps

object ItemFactory {
    val list: MutableMap<Int, Item> = HashIntObjMaps.newMutableMap()

    init {
        TODO("Add Items")
    }

    fun register(item: Item, override: Boolean = false) {
        val id: Int = item.getId()
        val variant = item.getMeta()

        if (!override && isRegistered(id, variant)) {
            throw RuntimeException("Trying to overwrite an already registered item")
        }
        list[getListOffset(id, variant)] = item.clone()
    }

    fun remap(identifier: ItemIdentifier, item: Item, override: Boolean = false) {
        if (!override && isRegistered(identifier.id, identifier.meta)) {
            throw RuntimeException("Trying to overwrite an already registered item")
        }

        list[getListOffset(identifier.id, identifier.meta)] = item.clone()
    }

    fun get(id: Int, meta: Int = 0, count: Int = 1, tags: CompoundTag? = null): Item {
        val offset = getListOffset(id, meta)
        val zero = getListOffset(id, 0)

        var item: Item? = when {
            meta != -1 -> when {
                list.containsKey(offset) -> list[offset]
                list.containsKey(zero) -> {
                    val durableItem = list[zero]
                    if (durableItem is Durable) {
                        if (meta <= durableItem.getMaxDurability()) {
                            val clone = durableItem.clone()
                            clone.damage = meta
                            clone
                        } else {
                            Item(ItemIdentifier(id, meta))
                        }
                    } else
                        null
                }
                id < 256 -> ItemBlock(ItemIdentifier(id, meta), BlockFactory.get(if (id < 0) -id else id, meta and 0xf))
                else -> null
            }
            else -> null
        }

        if (item === null) {
            item = Item(ItemIdentifier(id, meta))
        }

        item.count = count
        if (tags != null) {
            item.setNamedTag(tags)
        }

        return item
    }

    fun air(): Item {
        return get(ItemIds.AIR.id, 0, 0)
    }

    fun isRegistered(id: Int, variant: Int = 0): Boolean {
        if (id < 256) {
            return BlockFactory.isRegistered(id)
        }

        return list.containsKey(getListOffset(id, variant))
    }

    private fun getListOffset(id: Int, variant: Int): Int {
        if (id !in -0x8000..0x7fff) {
            throw IllegalArgumentException("ID must be in range ${0x8000} - ${0x7fff}")
        }

        return ((id and 0xffff) shl 16) or (variant and 0xffff)
    }
}
