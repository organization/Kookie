package be.zvz.kookie.network.mcpe.protocol.types.recipe

data class PotionContainerChangeRecipe(
    val inputItemId: Int,
    val ingredientItemId: Int,
    val outputItemId: Int
)
