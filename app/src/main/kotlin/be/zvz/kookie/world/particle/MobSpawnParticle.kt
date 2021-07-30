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

import be.zvz.kookie.network.mcpe.protocol.LevelEventPacket

class MobSpawnParticle(val width: Int = 0, val height: Int = 0) : EventParticle(LevelEventPacket.EVENT_PARTICLE_SPAWN) {
    override val data: Int get() = width and 0xff or (height and 0xff shl 8)
}
