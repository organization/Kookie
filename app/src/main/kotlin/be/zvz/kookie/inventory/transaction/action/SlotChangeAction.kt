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
package be.zvz.kookie.inventory.transaction.action

import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.inventory.transaction.InventoryTransaction
import be.zvz.kookie.inventory.transaction.TransactionValidationException
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class SlotChangeAction(val inventory: Inventory, val slot: Int, sourceItem: Item, targetItem: Item) :
    InventoryAction(sourceItem, targetItem) {
    override fun validate(player: Player) {
        if (!inventory.slotExists(slot)) {
            throw TransactionValidationException("Slot does not exist")
        }
        if (!inventory.getItem(slot).equalsExact(sourceItem)) {
            throw TransactionValidationException("Slot does not contain expected original item")
        }
    }

    override fun onAddToTransaction(transaction: InventoryTransaction) {
        transaction.addInventory(inventory)
    }

    override fun execute(player: Player) {
        inventory.setItem(slot, targetItem)
    }
}
