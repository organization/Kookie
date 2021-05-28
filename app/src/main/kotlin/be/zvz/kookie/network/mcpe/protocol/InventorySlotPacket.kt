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

class InventorySlotPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.INVENTORY_SLOT_PACKET)

	var windowId: Int
	var inputventorySlot: Int
	var item: ItemStackWrapper

	 static fun create(windowId: Int, slot: Int, item: ItemStackWrapper) : self{
		result = new self
		result.inventorySlot = slot
		result.item = item
		result.windowId = windowId

		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		windowId = input.getUnsignedVarInt()
		inventorySlot = input.getUnsignedVarInt()
		item = ItemStackWrapper::read(input)
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(windowId)
		output.putUnsignedVarInt(inventorySlot)
		item->write(output)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleInventorySlot(this)
	}
}
