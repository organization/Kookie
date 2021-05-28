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

import be.zvz.kookie.network.mcpe.protocol.types.ChunkCacheBlob
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.CLIENT_CACHE_MISS_RESPONSE_PACKET)
class ClientCacheMissResponsePacket : DataPacket(), ClientboundPacket {

    var blobs: MutableList<ChunkCacheBlob> = mutableListOf()

    companion object {
        fun create(blobs: List<ChunkCacheBlob>): ClientCacheMissResponsePacket = ClientCacheMissResponsePacket().apply {
            this.blobs = blobs.toMutableList() //이렇게 하면 상관없지 않?
            // 그러네
        }
    }


    override fun decodePayload(input: PacketSerializer) {
        for (i in 0..input.getUnsignedVarInt()){
            val hash = input.getLLong()
            val payload = input.getString()
            blobs[] = new ChunkCacheBlob (hash, payload)
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(count(blobs))
        foreach(blobs blob : as) {
            output.putLLong(blob.getHash())
            output.putString(blob.getPayload())
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleClientCacheMissResponse(this)
    }
}
