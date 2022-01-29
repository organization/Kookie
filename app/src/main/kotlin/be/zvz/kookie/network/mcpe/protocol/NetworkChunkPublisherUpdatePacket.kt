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

@ProtocolIdentify(ProtocolInfo.IDS.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET)
class NetworkChunkPublisherUpdatePacket : DataPacket(), ClientboundPacket {

    lateinit var position: PacketSerializer.BlockPosition
    var radius: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        input.getSignedBlockPosition(position)
        radius = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putSignedBlockPosition(position)
        output.putUnsignedVarInt(radius)
    }

    companion object {
        @JvmStatic
        fun create(x: Int, y: Int, z: Int, blockRadius: Int) =
            NetworkChunkPublisherUpdatePacket().apply {
                this.position = PacketSerializer.BlockPosition(x, y, z)
                this.radius = blockRadius
            }
    }
}
