package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class InstantHealthEffect(
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
        if (entity.getHealth() < entity.maxHealth) {
            // TODO: entity.heal(EntityRegainHealthEvent(entity, (4 shl instance.amplifier) * potency, EntityRegainHealthEvent.CAUSE_MAGIC)
        }
    }
}
