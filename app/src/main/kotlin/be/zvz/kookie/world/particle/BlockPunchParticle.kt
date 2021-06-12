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

import be.zvz.kookie.block.Block
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket

class BlockPunchParticle(val block: Block, val face: Int) : Particle {
    override fun encode(pos: Vector3): List<ClientboundPacket> {
        TODO("Implements after implemented RuntimeBlockMapping")
        // val data = RuntimeBlockMapping.totoRuntimeId(block.getFullId()) or (face shl 24)
        // listOf(LevelEventPacket.create(LevelEventPacket.EVENT_PARTICLE_PUNCH_BLOCK, data, pos))
    }
}
