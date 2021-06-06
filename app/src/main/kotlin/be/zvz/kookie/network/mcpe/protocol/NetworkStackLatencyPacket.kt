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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.NETWORK_STACK_LATENCY_PACKET)
class NetworkStackLatencyPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var timestamp: Long = 0
    var needResponse: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        timestamp = input.getLLong()
        needResponse = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLLong(timestamp)
        output.putBoolean(needResponse)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleNetworkStackLatency(this)
    }

    companion object {
        fun request(timestampNs: Long) = NetworkStackLatencyPacket().apply {
            this.timestamp = timestampNs
            this.needResponse = true
        }

        fun response(timestampNs: Long) = NetworkStackLatencyPacket().apply {
            this.timestamp = timestampNs
            this.needResponse = false
        }
    }
}
