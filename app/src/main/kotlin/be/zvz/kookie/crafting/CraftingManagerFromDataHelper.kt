package be.zvz.kookie.crafting

import be.zvz.kookie.crafting.utils.FurnaceRecipeData
import be.zvz.kookie.crafting.utils.ShapedRecipeData
import be.zvz.kookie.crafting.utils.ShapelessRecipeData
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.utils.Json
import com.fasterxml.jackson.module.kotlin.convertValue
import java.io.BufferedInputStream
import java.io.File

object CraftingManagerFromDataHelper {
    @JvmStatic
    fun make(file: File): CraftingManager {
        if (!file.exists()) {
            throw IllegalStateException("Expected file at ${file.path}, but none found")
        }
        val result = CraftingManager()

        val recipes = file.inputStream().use {
            BufferedInputStream(it).use(Json.jsonMapper::readTree)
        }
        recipes["shapeless"].let {
            Json.jsonMapper.convertValue<List<ShapelessRecipeData>>(it).forEach { recipe ->
                if (recipe.block != "crafting_table") {
                    return@forEach
                }
                val inputs = mutableListOf<Item>().apply {
                    recipe.input.forEach { item ->
                        add(ItemFactory.get(item.id, item.damage, 1))
                    }
                }
                val outputs = mutableListOf<Item>().apply {
                    recipe.output.forEach { item ->
                        add(ItemFactory.get(item.id, item.damage, 1))
                    }
                }
                result.registerShapelessRecipe(
                    ShapelessRecipe(
                        inputs,
                        outputs
                    )
                )
            }
        }
        recipes["shaped"].let {
            Json.jsonMapper.convertValue<List<ShapedRecipeData>>(it).forEach { recipe ->
                if (recipe.block != "crafting_table") {
                    return@forEach
                }
                val input = mutableMapOf<String, Item>().apply {
                    recipe.input.forEach { (regex, data) ->
                        put(regex, ItemFactory.get(data.id, data.damage, 1))
                    }
                }
                val output = mutableListOf<Item>().apply {
                    recipe.output.forEach { data ->
                        add(ItemFactory.get(data.id, data.damage, 1))
                    }
                }
                result.registerShapedRecipe(
                    ShapedRecipe(recipe.shape, input, output)
                )
            }
        }
        recipes["smelting"].let {
            Json.jsonMapper.convertValue<List<FurnaceRecipeData>>(it).forEach { recipe ->
                if (recipe.block != "furnace") {
                    return@forEach
                }
                result.furnaceRecipeManager.register(FurnaceRecipe(
                    ItemFactory.get(recipe.input.id, recipe.input.damage, 1),
                    ItemFactory.get(recipe.output.id, recipe.output.damage, 1)
                ))
            }
        }
        return result
    }
}
