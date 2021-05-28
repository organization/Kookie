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

class ShowCreditsPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SHOW_CREDITS_PACKET)

	 const val STATUS_START_CREDITS = 0
	 const val STATUS_END_CREDITS = 1

	var playerEid: Int
	var status: Int

	override fun decodePayload(input: PacketSerializer) {
		playerEid = input.getEntityRuntimeId()
		status = input.getVarInt()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putEntityRuntimeId(playerEid)
		output.putVarInt(status)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleShowCredits(this)
	}
}
