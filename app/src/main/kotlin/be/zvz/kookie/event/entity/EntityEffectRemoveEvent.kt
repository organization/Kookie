package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.effect.EffectInstance

class EntityEffectRemoveEvent(entity: Entity, effect: EffectInstance) : EntityEffectEvent(entity, effect) {
    override var isCancelled: Boolean = false
        get() = super.isCancelled
        set(value) {
            if (value) {
                if (effect.duration <= 0) {
                    throw IllegalStateException("Removal of expired effects cannot be cancelled")
                }
            }
            field = value
        }
}
