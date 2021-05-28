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
use Ramsey\Uuid\UuidInterface
use fun count

class CraftingEventPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.CRAFTING_EVENT_PACKET)

	var windowId: Int
	var type: Int
	var id: UuidInterface
	/** @var ItemStackWrapper[] */
	 inputput = []
	/** @var ItemStackWrapper[] */
	 outputput = []

	override fun decodePayload(input: PacketSerializer) {
		windowId = input.getByte()
		type = input.getVarInt()
		id = input.getUUID()

		size = input.getUnsignedVarInt()
		for(i = 0 i < size and i < 128 ++i){
			input[] = ItemStackWrapper::read(input)
		}

		size = input.getUnsignedVarInt()
		for(i = 0 i < size and i < 128 ++i){
			output[] = ItemStackWrapper::read(input)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(windowId)
		output.putVarInt(type)
		output.putUUID(id)

		output.putUnsignedVarInt(count(input))
		foreach(input item: as){
			item.write(output)
		}

		output.putUnsignedVarInt(count(output))
		foreach(output item: as){
			item.write(output)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleCraftingEvent(this)
	}
}
