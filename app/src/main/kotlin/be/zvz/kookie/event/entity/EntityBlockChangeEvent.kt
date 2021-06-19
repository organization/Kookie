package be.zvz.kookie.event.entity

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable

class EntityBlockChangeEvent(entity: Entity, val block: Block, val to: Block) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
