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
package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class InstantDamageEffect @JvmOverloads constructor(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true
) : InstantEffect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {
    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        // TODO: val damage = (4 shl instance.amplifier) * potency
        if (source != null) {
            val sourceOwner = source.owningEntity
            if (sourceOwner != null) {
                // TODO: EntityDamageByChildEntityEvent(sourceOwner, source, entity, EntityDamageEvent.CAUSE_MAGIC, damage)
            } else {
                // TODO: EntityDamageByEntityEvent(source, entity, EntityDamageEvent.CAUSE_MAGIC, damage)
            }
        } else {
            // TODO: EntityDamageEvent(entity, EntityDamageEvent.CAUSE_MAGIC, damage)
        }
        // TODO: entity.attack(ev)
    }
}
