package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import com.koloboke.collect.map.hash.HashObjObjMaps

class EntityDamageByChildEntityEvent(
    damagerEntity: Entity,
    entity: Entity,
    cause: Type,
    damage: Float,
    modifiers: MutableMap<ModifierType, Float> = HashObjObjMaps.newMutableMap(),
    knockBack: Float
) : EntityDamageByEntityEvent(
    damagerEntity,
    entity,
    cause,
    damage,
    modifiers,
    knockBack
)
