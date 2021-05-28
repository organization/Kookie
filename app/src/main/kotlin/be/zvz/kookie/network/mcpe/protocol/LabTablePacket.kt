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

class LabTablePacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.LAB_TABLE_PACKET)

	 const val TYPE_START_COMBINE = 0
	 const val TYPE_START_REACTION = 1
	 const val TYPE_RESET = 2

	var type: Int

	var x: Int
	var y: Int
	var z: Int

	var reactionType: Int

	override fun decodePayload(input: PacketSerializer) {
		type = input.getByte()
		input.getSignedBlockPosition(x, y, z)
		reactionType = input.getByte()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(type)
		output.putSignedBlockPosition(x, y, z)
		output.putByte(reactionType)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleLabTable(this)
	}
}
