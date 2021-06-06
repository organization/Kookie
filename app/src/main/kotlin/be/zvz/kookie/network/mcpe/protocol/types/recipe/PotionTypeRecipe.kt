package be.zvz.kookie.network.mcpe.protocol.types.recipe

data class PotionTypeRecipe(
    val inputItemId: Int,
    val inputItemMeta: Int,
    val ingredientItemId: Int,
    val ingredientItemMeta: Int,
    val outputItemId: Int,
    val outputItemMeta: Int
)
