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

import be.zvz.kookie.crafting.utils.FurnaceRecipeData
import be.zvz.kookie.crafting.utils.ShapedRecipeData
import be.zvz.kookie.crafting.utils.ShapelessRecipeData
import be.zvz.kookie.item.Item
import be.zvz.kookie.utils.Json
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.io.BufferedInputStream
import java.io.InputStream

class CraftingManager {
    val shapedRecipes: MutableMap<String, MutableList<ShapedRecipe>> = HashObjObjMaps.newMutableMap()
    val shapelessRecipes: MutableMap<String, MutableList<ShapelessRecipe>> = HashObjObjMaps.newMutableMap()

    val furnaceRecipeManager: FurnaceRecipeManager = FurnaceRecipeManager()

    val destructorCallbacks: MutableList<() -> Unit> = mutableListOf()
    val recipeRegisteredCallback: MutableList<() -> Unit> = mutableListOf()

    init {
        furnaceRecipeManager.recipeRegisteredCallbacks.add {
            recipeRegisteredCallback.forEach { it() }
        }
    }

    fun registerShapedRecipe(recipe: ShapedRecipe) {
        shapedRecipes
            .getOrPut(hashOutputs(recipe.results), ::mutableListOf)
            .add(recipe)

        recipeRegisteredCallback.forEach { it() }
    }

    fun registerShapelessRecipe(recipe: ShapelessRecipe) {
        shapelessRecipes
            .getOrPut(hashOutputs(recipe.results), ::mutableListOf)
            .add(recipe)

        recipeRegisteredCallback.forEach { it() }
    }

    fun matchRecipe(grid: CraftingGrid, outputs: List<Item>): CraftingRecipe? {
        val outputHash = hashOutputs(outputs)
        return shapedRecipes[outputHash]?.find { it.matchesCraftingGrid(grid) }
            ?: shapelessRecipes[outputHash]?.find { it.matchesCraftingGrid(grid) }
    }

    suspend fun matchRecipeByOutputs(outputs: List<Item>) = sequence {
        val outputHash = hashOutputs(outputs)
        shapedRecipes[outputHash]?.forEach { yield(it) }
        shapelessRecipes[outputHash]?.forEach { yield(it) }
    }

    fun finalize() {
        destructorCallbacks.forEach { it() }
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
            items.forEach items@{ item ->
                result.forEach {
                    if (item.equals(it)) {
                        it.count = it.count + item.count
                        return@items
                    }
                }
                result.add(item.clone())
            }
            return result
        }

        @JvmStatic
        private fun hashOutputs(outputs: List<Item>): String =
            mapper.writeValueAsString(
                pack(outputs).apply {
                    sortWith(::sort)
                    forEach { it.count = 1 }
                }
            )

        @JvmStatic
        fun fromDataHelper(stream: InputStream) = CraftingManager().apply {
            val recipes = stream.use {
                BufferedInputStream(it).use(Json.jsonMapper::readTree)
            }
            Json.jsonMapper.convertValue<List<ShapelessRecipeData>>(recipes["shapeless"]).forEach {
                if (it.block == "crafting_table") {
                    registerShapelessRecipe(it.toRecipe())
                }
            }
            Json.jsonMapper.convertValue<List<ShapedRecipeData>>(recipes["shaped"]).forEach {
                if (it.block == "crafting_table") {
                    registerShapedRecipe(it.toRecipe())
                }
            }
            Json.jsonMapper.convertValue<List<FurnaceRecipeData>>(recipes["smelting"]).forEach {
                if (it.block == "furnace") {
                    furnaceRecipeManager.register(it.toRecipe())
                }
            }
        }
    }
}
