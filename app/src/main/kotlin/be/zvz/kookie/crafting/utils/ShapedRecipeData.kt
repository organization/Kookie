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
package be.zvz.kookie.crafting.utils

import be.zvz.kookie.crafting.ShapedRecipe
import be.zvz.kookie.item.Item
import com.koloboke.collect.map.hash.HashCharObjMaps

data class ShapedRecipeData(
    val block: String,
    val input: Map<String, ItemData>,
    val output: List<ItemData>,
    val priority: Int,
    val shape: List<String>
) {
    fun toRecipe() = ShapedRecipe(
        shape = shape,
        ingredientMap = HashCharObjMaps.newMutableMap<Item>().apply {
            input.forEach { (symbol, itemData) ->
                put(symbol[0], itemData.toItem())
            }
        },
        results = output.map(ItemData::toItem)
    )
}
