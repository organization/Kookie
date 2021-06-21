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

import be.zvz.kookie.crafting.CraftingManager
import be.zvz.kookie.crafting.CraftingRecipe
import be.zvz.kookie.inventory.transaction.action.InventoryAction
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class CraftingTransaction(
    source: Player,
    val craftingManager: CraftingManager,
    actions: MutableList<InventoryAction>
) : InventoryTransaction(source, actions) {

    val recipe: CraftingRecipe? = null
    var repetitions: Int = 0

    val inputs: MutableList<Item> = mutableListOf()
    val outputs: MutableList<Item> = mutableListOf()

    @JvmOverloads
    fun matchRecipeItems(
        txItems: MutableList<Item>,
        recipeItems: MutableList<Item>,
        wildcards: Boolean,
        iterations: Int = 0
    ): Int {
        var iterations = iterations
        if (recipeItems.isEmpty()) {
            throw TransactionValidationException("No recipe items given")
        }
        if (txItems.isEmpty()) {
            throw TransactionValidationException("No transaction items given")
        }
        recipeItems.apply {
            val recipeIterator = iterator()

            while (recipeIterator.hasNext()) {
                val recipeItem = recipeIterator.next()
                var needCount = recipeItem.count

                recipeItems.apply {
                    val iterator1 = iterator()
                    while (iterator1.hasNext()) {
                        val otherRecipeItem = iterator1.next()
                        if (otherRecipeItem.equals(recipeItem)) {
                            needCount += otherRecipeItem.count
                            iterator1.remove()
                        }
                    }
                }
                var haveCount = 0
                txItems.apply {
                    val txIterator = iterator()
                    while (txIterator.hasNext()) {
                        val txItem = txIterator.next()
                        if (txItem.equals(
                                recipeItem,
                                !wildcards || !txItem.hasAnyDamageValue(),
                                !wildcards || txItem.hasNamedTag()
                            )
                        ) {
                            haveCount += txItem.count
                            txIterator.remove()
                        }
                    }
                }
                if (haveCount % needCount != 0) {
                    throw TransactionValidationException(
                        "Expected an exact multiple of required $recipeItem (given: $haveCount, needed: $needCount)"
                    )
                }
                val multiplier = haveCount.div(needCount)
                if (multiplier < 1) {
                    throw TransactionValidationException(
                        "Expected more than zero items matching $recipeItem (given: $haveCount, needed: $needCount)"
                    )
                }
                if (iterations == 0) {
                    iterations = multiplier
                } else if (iterations != multiplier) {
                    throw TransactionValidationException("Expected $recipeItem x$iterations, but found x$multiplier")
                }
            }
        }
        if (txItems.isNotEmpty()) {
            throw TransactionValidationException("Expected 0 ingredients left over, have ${txItems.size}")
        }
        return iterations
    }

    override suspend fun validate() {
        squashDuplicateSlotChanges()
        if (actions.isEmpty()) {
            throw TransactionValidationException("Transaction must have at least one action to be executable")
        }

        matchItems(outputs, inputs)

        var failed = 0
        craftingManager.matchRecipeByOutputs(outputs).forEach {
            try {
                repetitions = matchRecipeItems(outputs, it.getResultFor(source.craftingGrid).toMutableList(), false)
                matchRecipeItems(inputs, it.getIngredientList().toMutableList(), true, repetitions)
            } catch (_: TransactionValidationException) {
                ++failed
            }
        }
        if (recipe != null) {
            throw TransactionValidationException(
                "Unable to match a recipe to transaction (tried to match against $failed recipes)"
            )
        }
    }

    override fun callExecuteEvent(): Boolean {
        /*
        TODO:
        val ev = CraftingItemEVent(this, recipe, repetitions, inputs, outputs)
        ev.call()
        return !ev.isCancelled()
         */
        return super.callExecuteEvent()
    }
}
