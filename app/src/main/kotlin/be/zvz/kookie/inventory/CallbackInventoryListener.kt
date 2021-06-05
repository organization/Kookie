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

class CallbackInventoryListener(
    private var onSlotChangeCallback: ((inventory: Inventory, slot: Int, oldItem: Item) -> Unit)? = null,
    private var onContentChangeCallback: ((inventory: Inventory, oldContents: Map<Int, Item>) -> Unit)? = null
) : InventoryListener {
    override fun onSlotChange(inventory: Inventory, slot: Int, oldItem: Item) {
        onSlotChangeCallback?.let { it(inventory, slot, oldItem) }
    }

    override fun onSlotChange(inventory: Inventory, oldContents: Map<Int, Item>) {
        onContentChangeCallback?.let { it(inventory, oldContents) }
    }

    companion object {
        fun onAnyChange(onChange: (inventory: Inventory) -> Unit): CallbackInventoryListener {
            return CallbackInventoryListener(
                fun(inventory: Inventory, _: Int, _: Item) {
                    onChange(inventory)
                },
                fun(inventory: Inventory, _: Map<Int, Item>) {
                    onChange(inventory)
                }
            )
        }
    }
}
