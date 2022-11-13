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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.inventory.transaction.action

import be.zvz.kookie.inventory.transaction.TransactionValidationException
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.player.Player

class DropItemAction(targetItem: Item) : InventoryAction(ItemFactory.air(), targetItem) {
    override fun validate(player: Player) {
        if (targetItem.isNull()) {
            throw TransactionValidationException("Cannot drop an empty itemstack")
        }
    }

    override fun onPreExecute(source: Player): Boolean {
        /*
        val ev = PlayerDropItemEvent(source, targetItem)
        ev.call()
        return !ev.isCancelled()
         */
        return true // FIXME
    }

    override fun execute(player: Player) {
        // TODO: player.dropItem(targetItem)
    }
}
