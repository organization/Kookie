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

class MultiplayerSettingsPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.MULTIPLAYER_SETTINGS_PACKET)

	 const val ACTION_ENABLE_MULTIPLAYER = 0
	 const val ACTION_DISABLE_MULTIPLAYER = 1
	 const val ACTION_REFRESH_JOIN_CODE = 2

	var action: Int

	 static fun create(action: Int) : self{
		result = new self
		result.action = action
		return result
	}

	 fun getAction() : Int{
		return action
	}

	override fun decodePayload(input: PacketSerializer) {
		action = input.getVarInt()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putVarInt(action)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleMultiplayerSettings(this)
	}
}
