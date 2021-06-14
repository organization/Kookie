package be.zvz.kookie.item

import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.effect.EffectInstance

abstract class Food(identifier: ItemIdentifier, name: String) : Item(identifier, name), FoodSourceItem {

    override fun requiresHunger(): Boolean = true

    override fun getResidue(): Item = ItemFactory.air()

    override fun getAdditionalEffects(): List<EffectInstance> = listOf()

    override fun onConsume(consumer: Living) {
    }
}
