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
package be.zvz.kookie.event.player

import be.zvz.kookie.entity.Human
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.event.entity.EntityEvent

class PlayerExhaustEvent(human: Human, var amount: Float, var cause: Type) : EntityEvent(human), Cancellable {
    override var isCancelled: Boolean = false

    override val handlers: HandlerList
        get() = handlerList

    enum class Type(cause: Int) {
        ATTACK(1),
        DAMAGE(2),
        MINING(3),
        HEALTH_REGEN(4),
        POTION(5),
        WALKING(6),
        SPRINTING(7),
        SWIMMING(8),
        JUMPING(9),
        SPRINT_JUMPING(10),
        CUSTOM(11)
    }

    companion object {
        private val handlerList = HandlerList(PlayerExhaustEvent::class.java)
    }
}
