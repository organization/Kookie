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
import be.zvz.kookie.world.particle.EventParticle

abstract class StandardParticle @JvmOverloads constructor(
    id: Int,
    data: Int = 0
) : EventParticle(LevelEventPacket.EVENT_ADD_PARTICLE_MASK or id, data)
