package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Human
import be.zvz.kookie.entity.Living

class SaturationEffect(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = false
) : Effect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {
    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        if (entity is Human) {
            /*
            TODO:
            entity.hungerManager.addFood(instance.effectLevel)
            entity.hungerManager.addSaturation(instance.effectLevel * 2)
             */
        }
    }
}
