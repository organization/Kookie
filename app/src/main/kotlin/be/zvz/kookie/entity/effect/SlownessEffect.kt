package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Living

class SlownessEffect(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true
) : Effect(
    internalRuntimeId, name, color,
    bad,
    hasBubbles
) {
    override fun add(entity: Living, instance: EffectInstance) {
        entity.setMovementSpeed(entity.getMovementSpeed() * (1 - 0.15F * instance.effectLevel), true)
    }

    override fun remove(entity: Living, instance: EffectInstance) {
        entity.setMovementSpeed(entity.getMovementSpeed() / (1 - 0.15F * instance.effectLevel))
    }
}
