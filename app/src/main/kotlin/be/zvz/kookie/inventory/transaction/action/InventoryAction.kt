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

import be.zvz.kookie.inventory.transaction.InventoryTransaction
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

abstract class InventoryAction(val sourceItem: Item, val targetItem: Item) {

    abstract fun validate(player: Player)

    open fun onAddToTransaction(transaction: InventoryTransaction) {
    }

    open fun onPreExecute(source: Player): Boolean {
        return true
    }

    abstract fun execute(player: Player)
}
