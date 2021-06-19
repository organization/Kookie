package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity

class EntityCombustByEntityEvent(val combuster: Entity, combustee: Entity, duration: Int) : EntityCombustEvent(
    combustee,
    duration
)
