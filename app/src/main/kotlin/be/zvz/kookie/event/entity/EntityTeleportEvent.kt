package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.world.Position

class EntityTeleportEvent(entity: Entity, val from: Position, var to: Position) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
