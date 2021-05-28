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

class PlayerInputPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_INPUT_PACKET)

	var motionX: Float
	var motionY: Float
	var jumping: Boolean
	var sneaking: Boolean

	override fun decodePayload(input: PacketSerializer) {
		motionX = input.getLFloat()
		motionY = input.getLFloat()
		jumping = input.getBool()
		sneaking = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putLFloat(motionX)
		output.putLFloat(motionY)
		output.putBool(jumping)
		output.putBool(sneaking)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePlayerInput(this)
	}
}
