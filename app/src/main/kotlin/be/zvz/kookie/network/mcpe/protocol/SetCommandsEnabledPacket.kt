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

class SetCommandsEnabledPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.SET_COMMANDS_ENABLED_PACKET)

	var enabled: Boolean

	 static fun create(enabled: Boolean) : self{
		result = new self
		result.enabled = enabled
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		enabled = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putBool(enabled)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleSetCommandsEnabled(this)
	}
}
