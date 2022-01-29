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

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_EVENT_PACKET)
class BlockEventPacket : DataPacket(), ClientboundPacket {
    val pos = PacketSerializer.BlockPosition()
    var eventType: Int = 0
    var eventData: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(pos)
        eventType = input.getVarInt()
        eventData = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(pos)
        output.putVarInt(eventType)
        output.putVarInt(eventData)
    }

    companion object {
        const val EVENT_CHEST = 1

        const val CHEST_OPEN = 0
        const val CHEST_CLOSE = 1
    }
}
