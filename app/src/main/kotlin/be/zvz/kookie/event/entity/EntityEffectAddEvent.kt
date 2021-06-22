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
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.event.HandlerList

class EntityEffectAddEvent @JvmOverloads constructor(
    entity: Entity,
    effect: EffectInstance,
    val oldEffect: EffectInstance? = null
) : EntityEffectEvent(entity, effect) {
    override val handlers: HandlerList
        get() = handlerList

    fun willModify(): Boolean = hasOldEffect()

    fun hasOldEffect(): Boolean = oldEffect != null

    companion object {
        private val handlerList = HandlerList(EntityEffectAddEvent::class.java)
    }
}
