package be.zvz.kookie.network.mcpe.protocol

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

class SpawnParticleEffectPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SPAWN_PARTICLE_EFFECT_PACKET)

	var dimensionId: Int = DimensionIds::OVERWORLD //wtf mojang
	var entityUniqueId: Int = -1 //default none
	var position: Vector3
	var particleName: string

	override fun decodePayload(input: PacketSerializer) {
		dimensionId = input.getByte()
		entityUniqueId = input.getEntityUniqueId()
		position = input.getVector3()
		particleName = input.getString()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(dimensionId)
		output.putEntityUniqueId(entityUniqueId)
		output.putVector3(position)
		output.putString(particleName)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSpawnParticleEffect(this)
	}
}
