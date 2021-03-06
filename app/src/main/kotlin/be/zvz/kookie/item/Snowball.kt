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
package be.zvz.kookie.item

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Location
import be.zvz.kookie.player.Player

class Snowball(identifier: ItemIdentifier, vanillaName: String = "Unknown") : ProjectileItem(identifier, vanillaName) {
    override val maxStackSize: Int = 16
    override val throwForce: Double = 1.5

    override fun createEntity(location: Location, thrower: Player): Entity {
        TODO("Not yet implemented")
    }
}
