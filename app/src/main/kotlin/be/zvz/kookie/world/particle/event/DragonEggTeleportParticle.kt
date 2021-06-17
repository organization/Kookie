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
package be.zvz.kookie.world.particle.event

import be.zvz.kookie.network.mcpe.protocol.LevelEventPacket
import kotlin.math.abs

class DragonEggTeleportParticle(val xDiff: Int, val yDiff: Int, val zDiff: Int) :
    EventParticle(LevelEventPacket.EVENT_PARTICLE_DRAGON_EGG_TELEPORT) {
    init {
        listOf(xDiff, yDiff, zDiff).forEach {
            if (it !in -255 until 255) {
                throw IllegalArgumentException("Value must be between -255 and 255, $it given.")
            }
        }
    }

    override val data: Int
        get() = (if (zDiff < 0) 1 shl 26 else 0) or
            (if (yDiff < 0) 1 shl 25 else 0) or
            (if (xDiff < 0) 1 shl 24 else 0) or
            (abs(xDiff) shl 16) or
            (abs(yDiff) shl 8) or
            abs(zDiff)
}
