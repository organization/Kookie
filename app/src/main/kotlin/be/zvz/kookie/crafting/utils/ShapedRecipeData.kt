package be.zvz.kookie.crafting.utils

data class ShapedRecipeData(
    val block: String,
    val input: Map<String, ItemData>,
    val output: List<ItemData>,
    val priority: Int,
    val shape: List<String>
)
