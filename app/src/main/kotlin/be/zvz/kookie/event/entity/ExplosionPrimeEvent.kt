package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable

class ExplosionPrimeEvent(entity: Entity, var force: Float) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false

    var isBlockBreaking: Boolean = false
}
