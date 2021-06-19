package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Event

abstract class EntityEvent(val entity: Entity) : Event()
