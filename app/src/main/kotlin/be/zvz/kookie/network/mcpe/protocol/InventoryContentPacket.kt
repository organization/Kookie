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
use fun count

class InventoryContentPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.INVENTORY_CONTENT_PACKET)

	var windowId: Int
	/** @var ItemStackWrapper[] */
	 items = []

	/**
	 * @param ItemStackWrapper[] items
	 *
	 * @return InventoryContentPacket
	 */
	 static fun create(windowId: Int, items: array) : self{
		result = new self
		result.windowId = windowId
		result.items = items
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		windowId = input.getUnsignedVarInt()
		count = input.getUnsignedVarInt()
		for(i = 0 i < count ++i){
			items[] = ItemStackWrapper::read(input)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(windowId)
		output.putUnsignedVarInt(count(items))
		foreach(items item: as){
			item.write(output)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleInventoryContent(this)
	}
}
