package be.zvz.kookie.event.entity

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity

class EntityCombustByBlockEvent(
    val combuster: Block,
    combustee: Entity,
    duration: Int
) : EntityCombustEvent(combustee, duration)
