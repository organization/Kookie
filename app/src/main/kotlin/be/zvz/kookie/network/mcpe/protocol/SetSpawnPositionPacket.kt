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

class SetSpawnPositionPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SET_SPAWN_POSITION_PACKET)

	 const val TYPE_PLAYER_SPAWN = 0
	 const val TYPE_WORLD_SPAWN = 1

	var spawnType: Int
	var x: Int
	var y: Int
	var z: Int
	var dimension: Int
	var x: Int2
	var y: Int2
	var z: Int2

	 static fun playerSpawn(x: Int, y: Int, z: Int, dimension: Int, x2: Int, y2: Int, z2: Int) : self{
		result = new self
		result.spawnType = TYPE_PLAYER_SPAWN
		[result.x, result.y, result.z] = [x, y, z]
		[result.x2, result.y2, result.z2] = [x2, y2, z2]
		result.dimension = dimension
		return result
	}

	 static fun worldSpawn(x: Int, y: Int, z: Int, dimension: Int) : self{
		result = new self
		result.spawnType = TYPE_WORLD_SPAWN
		[result.x, result.y, result.z] = [x, y, z]
		[result.x2, result.y2, result.z2] = [x, y, z]
		result.dimension = dimension
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		spawnType = input.getVarInt()
		input.getBlockPosition(x, y, z)
		dimension = input.getVarInt()
		input.getBlockPosition(x2, y2, z2)
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putVarInt(spawnType)
		output.putBlockPosition(x, y, z)
		output.putVarInt(dimension)
		output.putBlockPosition(x2, y2, z2)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSetSpawnPosition(this)
	}
}
