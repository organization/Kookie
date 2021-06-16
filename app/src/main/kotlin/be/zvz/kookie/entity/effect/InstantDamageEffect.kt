package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class InstantDamageEffect(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true
) : InstantEffect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {
    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        val damage = (4 shl instance.amplifier) * potency
        if (source != null) {
            val sourceOwner = source.getOwningEntity()
            if (sourceOwner != null) {
                // TODO: EntityDamageByChildEntityEvent(sourceOwner, source, entity, EntityDamageEvent.CAUSE_MAGIC, damage)
            } else {
                // TODO: EntityDamageByEntityEvent(source, entity, EntityDamageEvent.CAUSE_MAGIC, damage)
            }
        } else {
            // TODO: EntityDamageEvent(entity, EntityDamageEvent.CAUSE_MAGIC, damage)
        }
        // TODO: entity.attack(ev)
    }
}
