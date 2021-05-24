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

object ItemFactory {
    val list = mutableListOf<Item>()

    init {
    }

    fun register(item: Item, override: Boolean = false) {
        val id: Int = item.getId()
    }

    fun remap(identifier: ItemIdentifier, item: Item, override: Boolean = false) {
        if (!override && isRegistered(identifier.id, identifier.meta)) {
            throw RuntimeException("")
        }

        list[getListOffset(identifier.id, identifier.meta)] = item.clone()
    }

    fun get(id: Int, meta: Int = 0, count: Int = 1, tags: CompoundTag? = null): Item {
        val item: Item = if (meta != -1) {
            val offset = getListOffset(id, meta)
            val zero = getListOffset(id, 0)
            val durable = list.getOrNull(zero)

            if (list.getOrNull(offset) != null) {
                list[offset].clone()
            } else if (durable is Durable) {
                if (meta <= durable.getMaxDurability()) {
                    val item = durable.clone()
                    item.damage = meta
                    item
                } else {
                    Item(ItemIdentifier(id, meta))
                }
            } else if (id < 256) {
                ItemBlock(
                    ItemIdentifier(id, meta),
                    BlockFactory.get(
                        if (id < 0) {
                            255 - id
                        } else {
                            id
                        },
                        meta and 0xf
                    )
                )
            } else {
                Item(ItemIdentifier(id, meta))
            }
        } else {
            Item(ItemIdentifier(id, meta))
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
            TODO("return BlockFactory.getInstance().isRegistered(id)")
        }

        return list.getOrNull(getListOffset(id, variant)) != null
    }

    private fun getListOffset(id: Int, variant: Int): Int {
        if (id !in -0x8000..0x7fff) {
            throw IllegalArgumentException("ID must be in range " + -0x8000 + " - " + 0x7fff)
        }

        return ((id and 0xffff) shl 16) or (variant and 0xffff)
    }
}
