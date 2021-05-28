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

class SetScoreboardIdentityPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SET_SCOREBOARD_IDENTITY_PACKET)

	 const val TYPE_REGISTER_IDENTITY = 0
	 const val TYPE_CLEAR_IDENTITY = 1

	var type: Int
	/** @var ScoreboardIdentityPacketEntry[] */
	 entries = []

	override fun decodePayload(input: PacketSerializer) {
		type = input.getByte()
		for(i = 0, count = input.getUnsignedVarInt() i < count ++i){
			entry = new ScoreboardIdentityPacketEntry()
			entry.scoreboardId = input.getVarLong()
			if(type === TYPE_REGISTER_IDENTITY){
				entry.entityUniqueId = input.getEntityUniqueId()
			}

			entries[] = entry
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(type)
		output.putUnsignedVarInt(count(entries))
		foreach(entries entry: as){
			output.putVarLong(entry.scoreboardId)
			if(type === TYPE_REGISTER_IDENTITY){
				output.putEntityUniqueId(entry.entityUniqueId)
			}
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSetScoreboardIdentity(this)
	}
}
