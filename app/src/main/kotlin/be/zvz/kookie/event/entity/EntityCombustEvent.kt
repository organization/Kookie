package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable

open class EntityCombustEvent(entity: Entity, var duration: Int) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
