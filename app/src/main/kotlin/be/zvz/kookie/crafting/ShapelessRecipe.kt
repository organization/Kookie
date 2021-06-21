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
package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item

class ShapelessRecipe(ingredients: List<Item>, results: List<Item>) : CraftingRecipe {

    var ingredients: MutableList<Item> = mutableListOf()

    var results: List<Item> = results.toList()

    init {
        ingredients.forEach {
            if (this.ingredients.size + it.count > 9) {
                throw IllegalArgumentException("Shapeless recipes cannot have more than 9 ingredients")
            }
            while (it.count > 0) {
                this.ingredients.add(it.pop())
            }
        }
    }

    override fun getIngredientList(): List<Item> {
        return ingredients.toList()
    }

    override fun getResultFor(grid: CraftingGrid): List<Item> {
        return results.toMutableList()
    }

    override fun matchesCraftingGrid(grid: CraftingGrid): Boolean {
        val input = grid.getContents().toMutableMap()

        ingredients.forEach loop@{ needItem ->
            input.apply {
                val iterator = iterator()
                while (iterator.hasNext()) {
                    val (j, haveItem) = iterator.next()
                    if (
                        haveItem.equals(
                            needItem,
                            !needItem.hasAnyDamageValue(),
                            needItem.hasNamedTag()
                        ) && haveItem.count >= needItem.count
                    ) {
                        iterator.remove()
                        return@loop
                    }
                }
            }
            return false
        }
        return input.isEmpty()
    }

    fun getIngredientCount(): Int {
        var count = 0
        ingredients.forEach { count += it.count }
        return count
    }
}
