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

@ProtocolIdentify(ProtocolInfo.IDS.POSITION_TRACKING_D_B_CLIENT_REQUEST_PACKET)
class PositionTrackingDBClientRequestPacket : DataPacket(), ServerboundPacket {
    var action: Int = 0
    var trackingId: Int = 0

    fun create(action: Int, trackingId: Int): PositionTrackingDBClientRequestPacket =
        PositionTrackingDBClientRequestPacket().apply {
            this.action = action
            this.trackingId = trackingId
        }

    override fun decodePayload(input: PacketSerializer) {
        action = input.getByte()
        trackingId = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(action)
        output.putVarInt(trackingId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePositionTrackingDBClientRequest(this)

    enum class Action(val id: Int) {
        QUERY(0);

        companion object {
            private val VALUES = values()

            @JvmStatic
            fun from(value: Int) = VALUES.first { it.id == value }
        }
    }
}
