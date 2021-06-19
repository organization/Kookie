package be.zvz.kookie.event.entity

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.world.Position

class EntityExplodeEvent(
    entity: Entity,
    val position: Position,
    var blocks: MutableList<Block>,
    yield: Float
) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
