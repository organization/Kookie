package be.zvz.kookie.event.entity

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity
import com.koloboke.collect.map.hash.HashObjObjMaps

class EntityDamageByBlockEvent(
    val damager: Block,
    entity: Entity,
    cause: Type,
    damage: Float,
    modifiers: MutableMap<ModifierType, Float> = HashObjObjMaps.newMutableMap()
) : EntityDamageEvent(
    entity,
    cause,
    damage,
    modifiers,
)
