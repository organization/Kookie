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

@ProtocolIdentify(ProtocolInfo.IDS.TICK_SYNC_PACKET)
class TickSyncPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var clientSendTime: Long = 0
    var serverReceiveTime: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        clientSendTime = input.getLLong()
        serverReceiveTime = input.getLLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLLong(clientSendTime)
        output.putLLong(serverReceiveTime)
    }
}
