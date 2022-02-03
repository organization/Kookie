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
package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item

class ShapelessRecipe(ingredients: List<Item>, results: List<Item>) : CraftingRecipe {
    private val _ingredients: List<Item> = ingredients
    override val ingredients: List<Item> get() = _ingredients.map(Item::clone)
    val ingredientCount: Int get() = ingredients.sumOf(Item::count)

    private val _results: List<Item> = results
    override val results: List<Item> get() = _results.map(Item::clone)

    init {
        if (ingredients.size > 9) {
            throw IllegalArgumentException("Shapeless recipes cannot have more than 9 ingredients")
        }
    }

    override fun matchesCraftingGrid(grid: CraftingGrid): Boolean {
        val input = grid.getContents().values.toMutableList()

        ingredients.forEach loop@{ needItem ->
            val iterator = input.iterator()
            while (iterator.hasNext()) {
                val haveItem = iterator.next()
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
            return false
        }
        return input.isEmpty()
    }
}
