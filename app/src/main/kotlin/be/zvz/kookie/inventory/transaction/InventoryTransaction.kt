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
package be.zvz.kookie.inventory.transaction

import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.inventory.transaction.action.InventoryAction
import be.zvz.kookie.inventory.transaction.action.SlotChangeAction
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player
import com.koloboke.collect.map.hash.HashObjIntMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import kotlin.math.min

open class InventoryTransaction(val source: Player, actions: MutableList<InventoryAction>) {
    var actions: MutableList<InventoryAction> = mutableListOf<InventoryAction>().apply {
        actions.forEach {
            addAction(it)
        }
    }
        private set

    var inventories: MutableList<Inventory> = mutableListOf()
    var hasExecuted: Boolean = false
        private set

    fun addAction(action: InventoryAction) {
        if (!actions.contains(action)) {
            actions.add(action)
            action.onAddToTransaction(this)
        } else {
            throw IllegalArgumentException("Tried to add the same action to a transaction twice")
        }
    }

    private fun shuffleActions() {
        actions.shuffle()
    }

    fun addInventory(inventory: Inventory) {
        if (!inventories.contains(inventory)) {
            inventories.add(inventory)
        }
    }

    fun matchItems(needItems: MutableList<Item>, haveItems: MutableList<Item>) {
        actions.forEach {
            if (!it.targetItem.isNull()) {
                needItems.add(it.targetItem)
            }
            try {
                it.validate(source)
            } catch (e: TransactionValidationException) {
                throw TransactionValidationException(it::class.java.simpleName, e)
            }
            if (!it.sourceItem.isNull()) {
                haveItems.add(it.sourceItem)
            }
        }
        needItems.apply {
            val iterator1 = iterator()
            while (iterator1.hasNext()) {
                val needItem = iterator1.next()
                haveItems.apply {
                    val iterator2 = iterator()
                    while (iterator2.hasNext()) {
                        val haveItem = iterator2.next()
                        if (needItem.equals(haveItem)) {
                            val amount = min(needItem.count, haveItem.count)
                            needItem.count = needItem.count - amount
                            haveItem.count = haveItem.count - amount
                            if (haveItem.count == 0) {
                                iterator1.remove()
                            }
                            if (needItem.count == 0) {
                                iterator1.remove()
                                return@apply
                            }
                        }
                    }
                }
            }
        }
    }

    protected fun squashDuplicateSlotChanges() {
        val slotChanges: MutableMap<String, MutableList<InventoryAction>> = HashObjObjMaps.newMutableMap()
        val inventories: MutableMap<String, Inventory> = HashObjObjMaps.newMutableMap()
        val slots: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()

        actions.forEach {
            if (it is SlotChangeAction) {
                val h = it.inventory.hashCode().toString() + "@" + it.slot
                slotChanges.getOrPut(h) { mutableListOf() }.add(it)
                inventories[h] = it.inventory
                slots[h] = it.slot
            }
        }

        slotChanges.apply {
            val iterator1 = iterator()
            while (iterator1.hasNext()) {
                val (hash, list) = iterator1.next()
                if (list.isEmpty()) {
                    return@apply
                }
                val inventory = inventories[hash]!! // should never null
                val slot = slots[hash]!! // too
                if (!inventory.slotExists(slot)) {
                    throw TransactionValidationException(
                        "Slot $slot does not exist in inventory ${inventory::class.java.simpleName}"
                    )
                }
                val sourceItem = inventory.getItem(slot)
                val targetItem = findResultItem(sourceItem, list)
                    ?: throw TransactionValidationException("Failed to compact ${list.size} duplicate actions")
                list.forEach {
                    actions.remove(it)
                }
                if (!targetItem.equalsExact(sourceItem)) {
                    addAction(SlotChangeAction(inventory, slot, sourceItem, targetItem))
                }
            }
        }
    }

    private fun findResultItem(needOrigin: Item, possibleActions: MutableList<InventoryAction>): Item? {
        assert(possibleActions.size > 0)

        var candidate: InventoryAction? = null
        val newList = possibleActions.toMutableList()

        possibleActions.apply {
            val iterator = iterator()
            while (iterator.hasNext()) {
                val action = iterator.next()
                if (action.sourceItem.equalsExact(needOrigin)) {
                    if (candidate != null) {
                        return null
                    }
                    candidate = action
                    iterator.remove()
                }
            }
        }
        if (candidate == null) {
            return null
        }
        if (newList.size > 0) {
            return candidate!!.targetItem
        }
        return findResultItem(candidate!!.targetItem, newList)
    }

    open suspend fun validate() {
        squashDuplicateSlotChanges()

        val haveItems: MutableList<Item> = mutableListOf()
        val needItems: MutableList<Item> = mutableListOf()
        matchItems(needItems, haveItems)
        if (actions.isEmpty()) {
            throw TransactionValidationException("Inventory transaction must have at least one action to be executable")
        }
        if (haveItems.isNotEmpty()) {
            throw TransactionValidationException("Transaction does not balance (tried to destroy some items)")
        }
        if (needItems.isNotEmpty()) {
            throw TransactionValidationException("Transaction does not balance (tried to create some items)")
        }
    }

    open fun callExecuteEvent(): Boolean {
        /*
        TODO:
        val ev = InventoryTransactionEvent(this)
        ev.call()
        return !ev.isCancelled()
         */
        return false // FIXME
    }

    suspend fun execute() {
        if (hasExecuted) {
            throw TransactionValidationException("Transaction has already been executed")
        }
        shuffleActions()
        validate()
        if (!callExecuteEvent()) {
            throw TransactionValidationException("Transaction event cancelled")
        }
        actions.forEach {
            if (!it.onPreExecute(source)) {
                throw TransactionValidationException("One of the actions in this transaction was cancelled")
            }
        }
        actions.forEach {
            it.execute(source)
        }
        hasExecuted = true
    }
}
