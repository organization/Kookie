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

class ScriptCustomEventPacket : DataPacket(){ //TODO: this doesn't have handlers in either client or server in the game as of 1.8
@ProtocolIdentify(ProtocolInfo.IDS.SCRIPT_CUSTOM_EVENT_PACKET)

	var eventName: string
	/** @var string json data */
	 eventData

	override fun decodePayload(input: PacketSerializer) {
		eventName = input.getString()
		eventData = input.getString()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(eventName)
		output.putString(eventData)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleScriptCustomEvent(this)
	}
}
