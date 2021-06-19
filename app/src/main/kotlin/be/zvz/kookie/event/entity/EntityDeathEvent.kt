package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Living
import be.zvz.kookie.item.Item

open class EntityDeathEvent(entity: Living, var drops: MutableList<Item> = mutableListOf(), var xp: Int = 0) : EntityEvent(entity)
