package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class LevitationEffect(
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

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        entity.addMotion(0.0, (instance.effectLevel / 20 - entity.motion.y) / 5, 0.0)
    }

    override fun add(entity: Living, instance: EffectInstance) {
        entity.setHasGravityEnabled(false)
    }

    override fun remove(entity: Living, instance: EffectInstance) {
        entity.setHasGravityEnabled(true)
    }
}
