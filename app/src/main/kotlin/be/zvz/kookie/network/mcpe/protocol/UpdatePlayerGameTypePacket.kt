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

class UpdatePlayerGameTypePacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_PLAYER_GAME_TYPE_PACKET)

	/**
	 * @var Int
	 * @see GameMode
	 */
	 gameMode

	var playerEntityUniqueId: Int

	 static fun create(gameMode: Int, playerEntityUniqueId: Int) : self{
		result = new self
		result.gameMode = gameMode
		result.playerEntityUniqueId = playerEntityUniqueId
		return result
	}

	 fun getGameMode() : Int{ return gameMode }

	 fun getPlayerEntityUniqueId() : Int{ return playerEntityUniqueId }

	override fun decodePayload(input: PacketSerializer) {
		gameMode = input.getVarInt()
		playerEntityUniqueId = input.getEntityUniqueId()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putVarInt(gameMode)
		output.putEntityUniqueId(playerEntityUniqueId)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleUpdatePlayerGameType(this)
	}
}
