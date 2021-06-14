package be.zvz.kookie.entity

interface FoodSource : Consumable {

    val foodRestore: Int
    val saturationRestore: Float

    fun requiresHunger(): Boolean
}
