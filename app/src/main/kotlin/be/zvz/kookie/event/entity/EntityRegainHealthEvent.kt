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
import be.zvz.kookie.event.HandlerList

class EntityRegainHealthEvent(entity: Entity, var amount: Float, var regainReason: Type) : EntityEvent(entity) {
    override val handlers: HandlerList
        get() = handlerList

    enum class Type(cause: Int) {
        REGEN(0),
        EATING(1),
        MAGIC(2),
        CUSTOM(3),
        SATURATION(4)
    }

    companion object {
        private val handlerList = HandlerList(EntityRegainHealthEvent::class.java)
    }
}
