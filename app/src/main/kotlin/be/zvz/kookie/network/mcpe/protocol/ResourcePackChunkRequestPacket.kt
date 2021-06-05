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

@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_CHUNK_REQUEST_PACKET)
class ResourcePackChunkRequestPacket : DataPacket(), ServerboundPacket {
    lateinit var packId: String
    var chunkIndex: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        packId = input.getString()
        chunkIndex = input.getLInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(packId)
        output.putLInt(chunkIndex)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleResourcePackChunkRequest(this)
}
