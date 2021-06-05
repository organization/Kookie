package be.zvz.kookie.entity

import be.zvz.kookie.entity.effect.EffectInstance

interface Consumable {

    fun getAdditionalEffects(): List<EffectInstance>

    fun onConsume(consumer: Living)
}
