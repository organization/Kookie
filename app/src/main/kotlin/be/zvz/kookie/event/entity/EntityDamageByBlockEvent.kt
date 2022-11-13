/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.event.entity

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity
import com.koloboke.collect.map.hash.HashObjObjMaps

class EntityDamageByBlockEvent @JvmOverloads constructor(
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
