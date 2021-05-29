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

class PositionTrackingDBClientRequestPacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.POSITION_TRACKING_D_B_CLIENT_REQUEST_PACKET)

    const val ACTION_QUERY = 0

    var action: Int
    var trackingId: Int

    static
    fun create(action: Int, trackingId: Int): self {
        result = new self
                result.action = action
        result.trackingId = trackingId
        return result
    }

    fun getAction(): Int {
        return action
    }

    fun getTrackingId(): Int {
        return trackingId
    }

    override fun decodePayload(input: PacketSerializer) {
        action = input.getByte()
        trackingId = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(action)
        output.putVarInt(trackingId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePositionTrackingDBClientRequest(this)
    }
}
