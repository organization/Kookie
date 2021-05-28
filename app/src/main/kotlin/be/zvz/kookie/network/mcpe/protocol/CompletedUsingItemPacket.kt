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

class CompletedUsingItemPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.COMPLETED_USING_ITEM_PACKET)

	 const val ACTION_UNKNOWN = -1
	 const val ACTION_EQUIP_ARMOR = 0
	 const val ACTION_EAT = 1
	 const val ACTION_ATTACK = 2
	 const val ACTION_CONSUME = 3
	 const val ACTION_THROW = 4
	 const val ACTION_SHOOT = 5
	 const val ACTION_PLACE = 6
	 const val ACTION_FILL_BOTTLE = 7
	 const val ACTION_FILL_BUCKET = 8
	 const val ACTION_POUR_BUCKET = 9
	 const val ACTION_USE_TOOL = 10
	 const val ACTION_INTERACT = 11
	 const val ACTION_RETRIEVED = 12
	 const val ACTION_DYED = 13
	 const val ACTION_TRADED = 14

	var itemId: Int
	var action: Int

	 fun decodePayload(input: PacketSerializer) {
		itemId = input.getShort()
		action = input.getLInt()
	}

	 fun encodePayload(output: PacketSerializer) {
		output.putShort(itemId)
		output.putLInt(action)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleCompletedUsingItem(this)
	}
}
