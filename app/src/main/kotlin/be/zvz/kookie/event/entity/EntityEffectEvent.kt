package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.event.Cancellable

open class EntityEffectEvent(entity: Entity, val effect: EffectInstance) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
