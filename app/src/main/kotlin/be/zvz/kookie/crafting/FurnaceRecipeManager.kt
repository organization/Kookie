package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item
import com.koloboke.collect.map.hash.HashObjObjMaps

class FurnaceRecipeManager {

    val furnaceRecipes: MutableMap<String, FurnaceRecipe> = HashObjObjMaps.newMutableMap()

    val recipeRegisteredCallbacks: MutableList<(FurnaceRecipe) -> Unit> = mutableListOf()

    fun register(recipe: FurnaceRecipe) {
        val input = recipe.ingredient
        val hash = "${input.getId()}:" + if (input.hasAnyDamageValue()) "?" else input.getMeta().toString()

        furnaceRecipes[hash] = recipe

        recipeRegisteredCallbacks.forEach {
            it(recipe)
        }
    }

    fun match(input: Item): FurnaceRecipe? {
        val hash = "${input.getId()}:" + if (input.hasAnyDamageValue()) "?" else input.getMeta().toString()
        return furnaceRecipes[hash]
    }
}
