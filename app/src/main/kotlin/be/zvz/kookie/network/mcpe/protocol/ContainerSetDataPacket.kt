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

class ContainerSetDataPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.CONTAINER_SET_DATA_PACKET)

	 const val PROPERTY_FURNACE_SMELT_PROGRESS = 0
	 const val PROPERTY_FURNACE_REMAINING_FUEL_TIME = 1
	 const val PROPERTY_FURNACE_MAX_FUEL_TIME = 2
	 const val PROPERTY_FURNACE_STORED_XP = 3
	 const val PROPERTY_FURNACE_FUEL_AUX = 4

	 const val PROPERTY_BREWING_STAND_BREW_TIME = 0
	 const val PROPERTY_BREWING_STAND_FUEL_AMOUNT = 1
	 const val PROPERTY_BREWING_STAND_FUEL_TOTAL = 2

	var windowId: Int
	var property: Int
	var value: Int

	 static fun create(windowId: Int, propertyId: Int, value: Int) : self{
		result = new self
		result.property = propertyId
		result.value = value
		result.windowId = windowId
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		windowId = input.getByte()
		property = input.getVarInt()
		value = input.getVarInt()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(windowId)
		output.putVarInt(property)
		output.putVarInt(value)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleContainerSetData(this)
	}
}
