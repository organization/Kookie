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

class SetDisplayObjectivePacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SET_DISPLAY_OBJECTIVE_PACKET)

	var displaySlot: string
	var objectiveName: string
	var displayName: string
	var criteriaName: string
	var sortOrder: Int

	override fun decodePayload(input: PacketSerializer) {
		displaySlot = input.getString()
		objectiveName = input.getString()
		displayName = input.getString()
		criteriaName = input.getString()
		sortOrder = input.getVarInt()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(displaySlot)
		output.putString(objectiveName)
		output.putString(displayName)
		output.putString(criteriaName)
		output.putVarInt(sortOrder)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSetDisplayObjective(this)
	}
}
