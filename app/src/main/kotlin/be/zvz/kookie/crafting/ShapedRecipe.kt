package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import com.koloboke.collect.map.hash.HashObjObjMaps

class ShapedRecipe(shape: List<String>, ingredientList: MutableMap<String, Item>, results: MutableList<Item>) : CraftingRecipe {

    var shape: List<String> = listOf()
        private set

    val ingredientList: MutableMap<String, Item> = HashObjObjMaps.newMutableMap()

    var results: MutableList<Item> = mutableListOf()
        private set

    var width: Int = 0
        private set

    var height: Int = 0
        private set

    init {
        height = shape.size
        if (height > 3 || height <= 0) {
            throw IllegalStateException("Shaped recipes may only have 1, 2 or 3 rows, not $height")
        }

        width = shape[0].length
        if (width > 3 || width <= 0) {
            throw IllegalStateException("Shaped recipes may only have 1, 2 or 3 columns, not $width")
        }

        shape.forEachIndexed { y, row ->
            if (row.length != width) {
                throw IllegalStateException(
                    "Shaped recipe rows must all have the same length (expected $width, got ${row.length})"
                )
            }
            for (x in 0..width) {
                if (row[x] != ' ' && !ingredientList.containsKey(row[x].toString())) {
                    throw IllegalArgumentException("No item specified for symbol '${row[0]}'")
                }
            }
        }
        this.shape = shape

        ingredientList.forEach { (char, i) ->
            if (!shape.joinToString("").contains(char)) {
                throw IllegalStateException("Symbol '$char' does not appear in the recipe shape")
            }
            this.ingredientList[char] = i.clone()
        }
        this.results = results.toMutableList()
    }

    override fun getIngredientList(): List<Item> {
        val result = mutableListOf<Item>()

        for (y in 0..height) {
            for (x in 0..width) {
                val ingredient = getIngredient(x, y)
                if (!ingredient.isNull()) {
                    result.add(ingredient)
                }
            }
        }
        return result
    }

    override fun getResultFor(grid: CraftingGrid): List<Item> {
        return results.toList()
    }

    override fun matchesCraftingGrid(grid: CraftingGrid): Boolean {
        if (width != grid.gridWidth || height != grid.getRecipeHeight()) {
            return false
        }
        return matchInputMap(grid, false) || matchInputMap(grid, true)
    }

    private fun matchInputMap(grid: CraftingGrid, reverse: Boolean): Boolean {
        for (y in 0..height) {
            for (x in 0..width) {
                val given = getIngredient(if (reverse) width - x - 1 else x, y)
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
        }
        return true
    }

    fun getIngredient(x: Int, y: Int): Item {
        val exist = ingredientList[shape[y][x].toString()]
        return exist?.clone() ?: ItemFactory.air()
    }
}
