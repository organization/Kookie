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

import be.zvz.kookie.entity.Location
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player

abstract class ProjectileItem(identifier: ItemIdentifier, vanillaName: String = "Unknown") : Item(identifier, vanillaName) {
    abstract val throwForce: Float

    protected abstract fun createEntity(location: Location, thrower: Player): Throwable

    override fun onClickAir(player: Player, directionVector: Vector3): ItemUseResult {
        val location = player.location

        val projectile = createEntity(Location.fromObject(player.getEyePos(), player.world, location.yaw, location.pitch), player)
        // TODO: setMotion of projectile

        // TODO: Call ProjectileLaunchEvent

        // TODO: spawnToAll

        // TODO: addSound

        pop()

        return ItemUseResult.SUCCESS
    }
}
