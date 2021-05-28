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

class LecternUpdatePacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.LECTERN_UPDATE_PACKET)

	var page: Int
	var totalPages: Int
	var x: Int
	var y: Int
	var z: Int
	var dropBook: Boolean

	override fun decodePayload(input: PacketSerializer) {
		page = input.getByte()
		totalPages = input.getByte()
		input.getBlockPosition(x, y, z)
		dropBook = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(page)
		output.putByte(totalPages)
		output.putBlockPosition(x, y, z)
		output.putBool(dropBook)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleLecternUpdate(this)
	}
}
