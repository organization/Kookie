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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.CLIENT_CACHE_BLOB_STATUS_PACKET)
class ClientCacheBlobStatusPacket : DataPacket(), ServerboundPacket {

    lateinit var hitHashes: MutableList<Long>
    lateinit var missHashes: MutableList<Long>

    companion object {
        fun create(hitHashes: List<Long>, missHashes: List<Long>): ClientCacheBlobStatusPacket =
            ClientCacheBlobStatusPacket().apply {
                this.hitHashes = hitHashes.toMutableList()
                this.missHashes = missHashes.toMutableList()
            }
    }

    override fun decodePayload(input: PacketSerializer) {
        val missCount = input.getUnsignedVarInt()
        val hitCount = input.getUnsignedVarInt()
        for (i in 0..missCount) {
            missHashes.add(input.getLLong())
        }
        for (i in 0..hitCount) {
            hitHashes.add(input.getLLong())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(missHashes.size)
        output.putUnsignedVarInt(hitHashes.size)
        missHashes.forEach { output.putLLong(it) }
        hitHashes.forEach { output.putLLong(it) }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleClientCacheBlobStatus(this)
    }
}
