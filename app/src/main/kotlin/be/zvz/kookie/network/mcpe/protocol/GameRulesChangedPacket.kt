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

class GameRulesChangedPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.GAME_RULES_CHANGED_PACKET)

	/**
	 * @var GameRule[]
	 * @phpstan-var array<string, GameRule>
	 */
	 gameRules = []

	override fun decodePayload(input: PacketSerializer) {
		gameRules = input.getGameRules()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putGameRules(gameRules)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleGameRulesChanged(this)
	}
}
