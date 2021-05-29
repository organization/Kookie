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

class NetworkChunkPublisherUpdatePacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET)

    var x: Int
    var y: Int
    var z: Int
    var radius: Int

    static
    fun create(x: Int, y: Int, z: Int, blockRadius: Int): self {
        result = new self
                result.x = x
        result.y = y
        result.z = z
        result.radius = blockRadius
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        input.getSignedBlockPosition(x, y, z)
        radius = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putSignedBlockPosition(x, y, z)
        output.putUnsignedVarInt(radius)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleNetworkChunkPublisherUpdate(this)
    }
}
