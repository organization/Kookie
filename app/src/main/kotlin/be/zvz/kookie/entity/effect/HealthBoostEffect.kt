package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Living

class HealthBoostEffect(
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

    override fun add(entity: Living, instance: EffectInstance) {
        entity.maxHealth = entity.maxHealth + 4 * instance.effectLevel
    }

    override fun remove(entity: Living, instance: EffectInstance) {
        entity.maxHealth = entity.maxHealth - 4 * instance.effectLevel
    }
}
