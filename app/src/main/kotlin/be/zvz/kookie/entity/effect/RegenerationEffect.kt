package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class RegenerationEffect(
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
        val interval = 40 shr instance.amplifier
        return if (interval > 0) {
            instance.duration % interval == 0
        } else {
            true
        }
    }

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        /*
        if($entity->getHealth() < $entity->getMaxHealth()){
			$ev = new EntityRegainHealthEvent($entity, 1, EntityRegainHealthEvent::CAUSE_MAGIC);
			$entity->heal($ev);
		}
         */
        if (entity.getHealth() < entity.maxHealth) {
            /*
            TODO:
            val ev = EntityRegainHealthEvent(entity, 1, EntityRegainHealthEvent.CAUSE_MAGIC)
            entity.heal(ev)
             */
        }
    }
}
