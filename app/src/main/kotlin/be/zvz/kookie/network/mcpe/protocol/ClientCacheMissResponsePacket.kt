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
import be.zvz.kookie.network.mcpe.protocol.types.ChunkCacheBlob

@ProtocolIdentify(ProtocolInfo.IDS.CLIENT_CACHE_MISS_RESPONSE_PACKET)
class ClientCacheMissResponsePacket : DataPacket(), ClientboundPacket {

    var blobs: MutableList<ChunkCacheBlob> = mutableListOf()

    companion object {
        @JvmStatic
        fun create(blobs: List<ChunkCacheBlob>) =
            ClientCacheMissResponsePacket().apply {
                this.blobs = blobs.toMutableList()
            }
    }

    override fun decodePayload(input: PacketSerializer) {
        repeat(input.getUnsignedVarInt()) {
            blobs.add(
                ChunkCacheBlob(
                    hash = input.getLLong(),
                    payload = input.getString()
                )
            )
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(blobs.size)
        blobs.forEach {
            output.putLLong(it.hash)
            output.putString(it.payload)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleClientCacheMissResponse(this)
}
