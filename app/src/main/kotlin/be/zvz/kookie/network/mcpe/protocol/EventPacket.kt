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

class EventPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.EVENT_PACKET)

	 const val TYPE_ACHIEVEMENT_AWARDED = 0
	 const val TYPE_ENTITY_INTERACT = 1
	 const val TYPE_PORTAL_BUILT = 2
	 const val TYPE_PORTAL_USED = 3
	 const val TYPE_MOB_KILLED = 4
	 const val TYPE_CAULDRON_USED = 5
	 const val TYPE_PLAYER_DEATH = 6
	 const val TYPE_BOSS_KILLED = 7
	 const val TYPE_AGENT_COMMAND = 8
	 const val TYPE_AGENT_CREATED = 9
	 const val TYPE_PATTERN_REMOVED = 10 //???
	 const val TYPE_COMMANED_EXECUTED = 11
	 const val TYPE_FISH_BUCKETED = 12
	 const val TYPE_MOB_BORN = 13
	 const val TYPE_PET_DIED = 14
	 const val TYPE_CAULDRON_BLOCK_USED = 15
	 const val TYPE_COMPOSTER_BLOCK_USED = 16
	 const val TYPE_BELL_BLOCK_USED = 17
	 const val TYPE_ACTOR_DEFINITION = 18
	 const val TYPE_RAID_UPDATE = 19
	 const val TYPE_PLAYER_MOVEMENT_ANOMALY = 20 //anti cheat
	 const val TYPE_PLAYER_MOVEMENT_CORRECTED = 21
	 const val TYPE_HONEY_HARVESTED = 22
	 const val TYPE_TARGET_BLOCK_HIT = 23
	 const val TYPE_PIGLIN_BARTER = 24

	var playerRuntimeId: Int
	var eventData: Int
	var type: Int

	override fun decodePayload(input: PacketSerializer) {
		playerRuntimeId = input.getEntityRuntimeId()
		eventData = input.getVarInt()
		type = input.getByte()

		//TODO: nice confusing mess
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putEntityRuntimeId(playerRuntimeId)
		output.putVarInt(eventData)
		output.putByte(type)

		//TODO: also nice confusing mess
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleEvent(this)
	}
}
