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

class PlayerHotbarPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_HOTBAR_PACKET)

	var selectedHotbarSlot: Int
	var windowId: Int = ContainerIds::INVENTORY
	var selectHotbarSlot: Boolean = true

	 static fun create(slot: Int, windowId: Int, Boolean selectSlot = true) : self{
		result = new self
		result.selectedHotbarSlot = slot
		result.windowId = windowId
		result.selectHotbarSlot = selectSlot
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		selectedHotbarSlot = input.getUnsignedVarInt()
		windowId = input.getByte()
		selectHotbarSlot = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(selectedHotbarSlot)
		output.putByte(windowId)
		output.putBool(selectHotbarSlot)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePlayerHotbar(this)
	}
}
