package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item

class FurnaceRecipe(result: Item, ingredient: Item) {

    val item: Item = result.clone()
    val ingredient: Item = ingredient.clone()
}
