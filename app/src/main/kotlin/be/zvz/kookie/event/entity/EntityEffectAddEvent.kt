package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.effect.EffectInstance

class EntityEffectAddEvent(
    entity: Entity,
    effect: EffectInstance,
    val oldEffect: EffectInstance? = null
) : EntityEffectEvent(entity, effect) {

    fun willModify(): Boolean = hasOldEffect()

    fun hasOldEffect(): Boolean = oldEffect != null
}
