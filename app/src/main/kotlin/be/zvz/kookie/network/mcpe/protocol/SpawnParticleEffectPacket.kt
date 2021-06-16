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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.DimensionIds

@ProtocolIdentify(ProtocolInfo.IDS.SPAWN_PARTICLE_EFFECT_PACKET)
class SpawnParticleEffectPacket : DataPacket(), ClientboundPacket {

    var dimensionId: DimensionIds = DimensionIds.OVERWORLD // wtf mojang
    var entityUniqueId: Long = -1 // default none
    lateinit var position: Vector3
    lateinit var particleName: String

    override fun decodePayload(input: PacketSerializer) {
        dimensionId = DimensionIds.fromInt(input.getByte())
        entityUniqueId = input.getEntityUniqueId()
        position = input.getVector3()
        particleName = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(dimensionId.id)
        output.putEntityUniqueId(entityUniqueId)
        output.putVector3(position)
        output.putString(particleName)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleSpawnParticleEffect(this)
}
