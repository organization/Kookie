package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class Poison(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true,
    var fatal: Boolean = false
) : Effect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {
    override fun canTick(instance: EffectInstance): Boolean {
        val interval = 25 shr instance.amplifier
        return if (interval > 0) {
            instance.duration % interval == 0
        } else {
            true
        }
    }

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        /*
        if($entity->getHealth() > 1 or $this->fatal){
			$ev = new EntityDamageEvent($entity, EntityDamageEvent::CAUSE_MAGIC, 1);
			$entity->attack($ev);
		}
         */
        if (entity.getHealth() > 1 || fatal) {
            /*
            TODO:
            val ev = EntityDamageEvent(entity, EntityDamageEvent.CAUSE_MAGIC, 1)
            entity.attack(ev)
             */
        }
    }
}
