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

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.item.Item
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player

class PlayerInteractEvent @JvmOverloads constructor(
    player: Player,
    val item: Item,
    val block: Block,
    val touchVector: Vector3 = Vector3(),
    val face: Action = Action.RIGHT_CLICK_BLOCK
) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
    override val handlers: HandlerList
        get() = handlerList

    enum class Action(id: Int) {
        LEFT_CLICK_BLOCK(0),
        RIGHT_CLICK_BLOCK(1)
    }

    companion object {
        private val handlerList = HandlerList(PlayerInteractEvent::class.java)
    }
}
