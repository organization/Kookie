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

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.world.Position

class EntityExplodeEvent(
    entity: Entity,
    val position: Position,
    var blocks: MutableList<Block>,
    val yield: Float
) : EntityEvent(entity), Cancellable {
    override var isCancelled: Boolean = false
}
