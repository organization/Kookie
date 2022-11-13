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

class LevitationEffect @JvmOverloads constructor(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true
) : Effect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        entity.addMotion(0.0, (instance.effectLevel / 20 - entity.motion.y) / 5, 0.0)
    }

    override fun add(entity: Living, instance: EffectInstance) {
        entity.setHasGravityEnabled(false)
    }

    override fun remove(entity: Living, instance: EffectInstance) {
        entity.setHasGravityEnabled(true)
    }
}
