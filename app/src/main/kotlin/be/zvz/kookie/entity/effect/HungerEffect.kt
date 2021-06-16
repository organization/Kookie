package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Human
import be.zvz.kookie.entity.Living

class HungerEffect(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true
) : Effect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {
    override fun canTick(instance: EffectInstance): Boolean {
        return true
    }

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        if (entity is Human) {
            // TODO: entity.hungerManager.exhaust(0.025 * instance.effectLevel, PlayerExhaustEvent.CAUSE_POTION)
        }
    }
}
