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
package be.zvz.kookie.world.particle

import be.zvz.kookie.item.Item
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.LevelEventPacket
import be.zvz.kookie.network.mcpe.protocol.types.ParticleIds

class ItemBreakParticle(val item: Item) : Particle {
    override fun encode(pos: Vector3) =
        listOf(LevelEventPacket.standardParticle(ParticleIds.ITEM_BREAK, item.getId() shl 16 or item.getMeta(), pos))
}
