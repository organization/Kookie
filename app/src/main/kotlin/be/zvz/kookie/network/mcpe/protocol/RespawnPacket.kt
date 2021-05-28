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

class RespawnPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.RESPAWN_PACKET)

	 const val SEARCHING_FOR_SPAWN = 0
	 const val READY_TO_SPAWN = 1
	 const val CLIENT_READY_TO_SPAWN = 2

	var position: Vector3
	var respawnState: Int = SEARCHING_FOR_SPAWN
	var entityRuntimeId: Int

	 static fun create(position: Vector3, respawnStatus: Int, entityRuntimeId: Int) : self{
		result = new self
		result.position = position.asVector3()
		result.respawnState = respawnStatus
		result.entityRuntimeId = entityRuntimeId
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		position = input.getVector3()
		respawnState = input.getByte()
		entityRuntimeId = input.getEntityRuntimeId()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putVector3(position)
		output.putByte(respawnState)
		output.putEntityRuntimeId(entityRuntimeId)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleRespawn(this)
	}
}
