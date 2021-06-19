package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.math.Vector3

class EntityMotionEvent(entity: Entity, var motion: Vector3) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
