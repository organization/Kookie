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
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.koloboke.collect.map.hash.HashObjObjMaps

class CraftingManager {

    val shapedRecipes: MutableMap<String, MutableList<ShapedRecipe>> = HashObjObjMaps.newMutableMap()

    val shapelessRecipes: MutableMap<String, MutableList<ShapelessRecipe>> = HashObjObjMaps.newMutableMap()

    val furnaceRecipeManager: FurnaceRecipeManager = FurnaceRecipeManager()

    val recipeRegisteredCallback: MutableList<() -> Unit> = mutableListOf()

    init {
        furnaceRecipeManager.recipeRegisteredCallbacks.add {
            recipeRegisteredCallback.forEach {
                it()
            }
        }
    }

    fun registerShapedRecipe(recipe: ShapedRecipe) {
        shapedRecipes.getOrPut(hashOutputs(recipe.results)) {
            mutableListOf()
        }.add(recipe)
    }

    fun registerShapelessRecipe(recipe: ShapelessRecipe) {
        shapelessRecipes.getOrPut(hashOutputs(recipe.results)) {
            mutableListOf()
        }.add(recipe)
    }

    fun matchRecipe(grid: CraftingGrid, outputs: List<Item>): CraftingRecipe? {
        val outputHash = hashOutputs(outputs)

        if (shapedRecipes.containsKey(outputHash)) {
            shapelessRecipes.getValue(outputHash).forEach {
                if (it.matchesCraftingGrid(grid)) {
                    return it
                }
            }
        }
        if (shapelessRecipes.containsKey(outputHash)) {
            shapelessRecipes.getValue(outputHash).forEach {
                if (it.matchesCraftingGrid(grid)) {
                    return it
                }
            }
        }
        return null
    }

    suspend fun matchRecipeByOutputs(outputs: List<Item>) = sequence<CraftingRecipe> {
        val outputHash = hashOutputs(outputs)
        if (shapedRecipes.containsKey(outputHash)) {
            shapelessRecipes.getValue(outputHash).forEach {
                yield(it)
            }
        }
        if (shapelessRecipes.containsKey(outputHash)) {
            shapelessRecipes.getValue(outputHash).forEach {
                yield(it)
            }
        }
    }

    companion object {
        private val mapper = ObjectMapper(
            JsonFactory().apply {
                enable(JsonParser.Feature.ALLOW_COMMENTS)
                enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            }
        )

        @JvmStatic
        fun sort(i1: Item, i2: Item): Int {
            var retval = i1.getId().compareTo(i2.getId())
            if (retval == 0) {
                retval = i1.getMeta().compareTo(i2.getMeta())
                if (retval == 0) {
                    retval = i1.count.compareTo(i2.count)
                }
            }
            return retval
        }

        @JvmStatic
        private fun pack(items: List<Item>): MutableList<Item> {
            val result = mutableListOf<Item>()
            items.forEachIndexed index@{ index, item ->
                result.forEach {
                    if (item.equals(it)) {
                        it.count = it.count + item.count
                        return@index
                    }
                }
                result.add(item.clone())
            }
            return result
        }

        @JvmStatic
        private fun hashOutputs(outputs: List<Item>): String {
            val outputs = pack(outputs)
            outputs.sortWith(
                Comparator { item, item2 ->
                    sort(item, item2)
                }
            )
            outputs.forEach { o ->
                o.count = 1
            }
            return mapper.writeValueAsString(outputs)
        }
    }
}
