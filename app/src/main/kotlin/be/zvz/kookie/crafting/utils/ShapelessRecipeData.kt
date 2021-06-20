package be.zvz.kookie.crafting.utils

data class ShapelessRecipeData(
    val block: String,
    val input: List<ItemData>,
    val output: List<ItemData>,
    val priority: Int
)
