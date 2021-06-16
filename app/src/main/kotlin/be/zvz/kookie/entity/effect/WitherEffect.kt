package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class WitherEffect(
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
        val interval = 50 shr instance.amplifier
        return if (interval > 0) {
            instance.duration % interval == 0
        } else {
            true
        }
    }

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        /*
        TODO:
        val ev = EntityDamageEvent(entity, EntityDamageEvent.CAUSE_MAGIC, 1)
		entity.attack(ev)
         */
    }
}
