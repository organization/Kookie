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

class TransferPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.TRANSFER_PACKET)

	var address: string
	var port: Int = 19132

	 static fun create(address: string, port: Int) : self{
		result = new self
		result.address = address
		result.port = port
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		address = input.getString()
		port = input.getLShort()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(address)
		output.putLShort(port)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleTransfer(this)
	}
}
