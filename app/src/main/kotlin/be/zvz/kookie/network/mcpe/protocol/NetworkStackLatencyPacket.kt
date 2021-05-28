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

class NetworkStackLatencyPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.NETWORK_STACK_LATENCY_PACKET)

	var timestamp: Int
	var needResponse: Boolean

	 static fun request(timestampNs: Int) : self{
		result = new self
		result.timestamp = timestampNs
		result.needResponse = true
		return result
	}

	 static fun response(timestampNs: Int) : self{
		result = new self
		result.timestamp = timestampNs
		result.needResponse = false
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		timestamp = input.getLLong()
		needResponse = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putLLong(timestamp)
		output.putBool(needResponse)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleNetworkStackLatency(this)
	}
}
