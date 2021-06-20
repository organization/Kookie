package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item

interface CraftingRecipe {

    fun getIngredientList(): List<Item>

    fun getResultFor(grid: CraftingGrid): List<Item>

    fun matchesCraftingGrid(grid: CraftingGrid): Boolean
}
