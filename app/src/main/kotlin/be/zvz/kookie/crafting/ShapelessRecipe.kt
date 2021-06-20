package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item

class ShapelessRecipe(ingredients: List<Item>, results: List<Item>) : CraftingRecipe {

    var ingredients: MutableList<Item> = mutableListOf()

    var results: List<Item> = listOf()

    init {
        ingredients.forEach {
            if (this.ingredients.size + it.count > 9) {
                throw IllegalArgumentException("Shapeless recipes cannot have more than 9 ingredients")
            }
            while (it.count > 0) {
                this.ingredients.add(it.pop())
            }
        }
        this.results = results.toList()
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
