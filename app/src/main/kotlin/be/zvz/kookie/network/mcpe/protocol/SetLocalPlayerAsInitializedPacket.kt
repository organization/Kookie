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

class SetLocalPlayerAsInitializedPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET)

	var entityRuntimeId: Int

	override fun decodePayload(input: PacketSerializer) {
		entityRuntimeId = input.getEntityRuntimeId()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putEntityRuntimeId(entityRuntimeId)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSetLocalPlayerAsInitialized(this)
	}
}
