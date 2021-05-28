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

class EmoteListPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.EMOTE_LIST_PACKET)

	var playerEntityRuntimeId: Int
	/** @var UuidInterface[] */
	 emoteIds

	/**
	 * @param UuidInterface[] emoteIds
	 */
	 static fun create(playerEntityRuntimeId: Int, emoteIds: array) : self{
		result = new self
		result.playerEntityRuntimeId = playerEntityRuntimeId
		result.emoteIds = emoteIds
		return result
	}

	 fun getPlayerEntityRuntimeId() : Int{ return playerEntityRuntimeId }

	/** @return UuidInterface[] */
	 fun getEmoteIds() : array{ return emoteIds }

	override fun decodePayload(input: PacketSerializer) {
		playerEntityRuntimeId = input.getEntityRuntimeId()
		emoteIds = []
		for(i = 0, len = input.getUnsignedVarInt() i < len ++i){
			emoteIds[] = input.getUUID()
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putEntityRuntimeId(playerEntityRuntimeId)
		output.putUnsignedVarInt(count(emoteIds))
		foreach(emoteIds emoteId: as){
			output.putUUID(emoteId)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleEmoteList(this)
	}
}
