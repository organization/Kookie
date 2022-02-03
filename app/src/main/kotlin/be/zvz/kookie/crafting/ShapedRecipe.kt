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
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.utils.inline.repeat2
import com.koloboke.collect.map.hash.HashIntObjMaps

class ShapedRecipe(val shape: List<String>, ingredientMap: Map<Char, Item>, results: List<Item>) : CraftingRecipe {
    private val _ingredientMap: Map<Char, Item> = ingredientMap
    override val ingredients: List<Item>
        get() = mutableListOf<Item>().apply {
            repeat2(width, height) { x, y ->
                val ingredient = getIngredient(x, y)
                if (!ingredient.isNull()) {
                    add(ingredient)
                }
            }
        }

    private val _results: List<Item> = results
    override val results: List<Item> get() = _results.map(Item::clone)

    val ingredientMap
        get() = HashIntObjMaps.newMutableMap<MutableMap<Int, Item>>().apply {
            repeat2(width, height) { x, y ->
                getOrPut(x, HashIntObjMaps::newMutableMap)[y] = getIngredient(x, y)
            }
        }

    val width: Int = shape.size
    val height: Int = shape[0].length

    init {
        if (height !in 1..3) {
            throw IllegalStateException("Shaped recipes may only have 1, 2 or 3 rows, not $height")
        } else if (width !in 1..3) {
            throw IllegalStateException("Shaped recipes may only have 1, 2 or 3 columns, not $width")
        }

        shape.forEach { row ->
            if (row.length != width) {
                throw IllegalStateException(
                    "Shaped recipe rows must all have the same length (expected $width, got ${row.length})"
                )
            }
            repeat(width) { x ->
                if (row[x] != ' ' && !_ingredientMap.containsKey(row[x])) {
                    throw IllegalArgumentException("No item specified for symbol '${row[0]}'")
                }
            }
        }

        _ingredientMap.keys.forEach { char ->
            if (!shape.joinToString("").contains(char)) {
                throw IllegalStateException("Symbol '$char' does not appear in the recipe shape")
            }
        }
    }

    fun getIngredient(x: Int, y: Int): Item {
        val exist = _ingredientMap[shape[y][x]]
        return exist?.clone() ?: ItemFactory.air()
    }

    override fun matchesCraftingGrid(grid: CraftingGrid): Boolean =
        width == grid.recipeWidth && height == grid.recipeHeight &&
            (matchInputMap(grid, false) || matchInputMap(grid, true))

    private fun matchInputMap(grid: CraftingGrid, reverse: Boolean): Boolean {
        repeat2(width, height) { x, y ->
            val given = grid.getIngredient(if (reverse) width - x - 1 else x, y)
            val required = getIngredient(x, y)
            if (!required.equals(
                    given,
                    !required.hasAnyDamageValue(),
                    required.hasNamedTag()
                ) || required.count > given.count
            ) {
                return false
            }
        }
        return true
    }
}
