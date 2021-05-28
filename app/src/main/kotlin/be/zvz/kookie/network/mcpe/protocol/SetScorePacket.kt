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

class SetScorePacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SET_SCORE_PACKET)

	 const val TYPE_CHANGE = 0
	 const val TYPE_REMOVE = 1

	var type: Int
	/** @var ScorePacketEntry[] */
	 entries = []

	override fun decodePayload(input: PacketSerializer) {
		type = input.getByte()
		for(i = 0, i2 = input.getUnsignedVarInt() i < i2 ++i){
			entry = new ScorePacketEntry()
			entry.scoreboardId = input.getVarLong()
			entry.objectiveName = input.getString()
			entry.score = input.getLInt()
			if(type !== TYPE_REMOVE){
				entry.type = input.getByte()
				switch(entry.type){
					case ScorePacketEntry::TYPE_PLAYER:
					case ScorePacketEntry::TYPE_ENTITY:
						entry.entityUniqueId = input.getEntityUniqueId()
						break
					case ScorePacketEntry::TYPE_FAKE_PLAYER:
						entry.customName = input.getString()
						break
					default:
						throw new PacketDecodeException("Unknown entry type entry.type")
				}
			}
			entries[] = entry
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(type)
		output.putUnsignedVarInt(count(entries))
		foreach(entries entry: as){
			output.putVarLong(entry.scoreboardId)
			output.putString(entry.objectiveName)
			output.putLInt(entry.score)
			if(type !== TYPE_REMOVE){
				output.putByte(entry.type)
				switch(entry.type){
					case ScorePacketEntry::TYPE_PLAYER:
					case ScorePacketEntry::TYPE_ENTITY:
						output.putEntityUniqueId(entry.entityUniqueId)
						break
					case ScorePacketEntry::TYPE_FAKE_PLAYER:
						output.putString(entry.customName)
						break
					default:
						throw new \InvalidArgumentException("Unknown entry type entry.type")
				}
			}
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSetScore(this)
	}
}
