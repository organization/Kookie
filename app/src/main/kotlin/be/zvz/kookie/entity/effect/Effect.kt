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

open class Effect @JvmOverloads constructor(
    val internalRuntimeId: Int,
    val name: String,
    val color: Color,
    val bad: Boolean = false,
    val hasBubbles: Boolean = true
) {
    open val defaultDuration: Int = 600

    open fun canTick(instance: EffectInstance): Boolean = false

    @JvmOverloads
    open fun applyEffect(entity: Living, instance: EffectInstance, potency: Float = 1.0F, source: Entity? = null) {
    }

    open fun add(entity: Living, instance: EffectInstance) {
    }

    open fun remove(entity: Living, instance: EffectInstance) {
    }
}
