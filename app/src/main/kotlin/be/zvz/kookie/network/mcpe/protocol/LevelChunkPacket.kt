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

@ProtocolIdentify(ProtocolInfo.IDS.LEVEL_CHUNK_PACKET)
class LevelChunkPacket : DataPacket(), ClientboundPacket {

    var chunkX: Int = 0
    var chunkZ: Int = 0
    var subChunkCount: Int = 0
    var cacheEnabled: Boolean = false
    lateinit var usedBlobHashes: MutableList<Long>
    lateinit var extraPayload: String

    override fun decodePayload(input: PacketSerializer) {
        chunkX = input.getVarInt()
        chunkZ = input.getVarInt()
        subChunkCount = input.getUnsignedVarInt()
        cacheEnabled = input.getBoolean()
        if (cacheEnabled) {
            repeat(input.getUnsignedVarInt()) {
                usedBlobHashes.add(input.getLLong())
            }
        }
        extraPayload = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(chunkX)
        output.putVarInt(chunkZ)
        output.putUnsignedVarInt(subChunkCount)
        output.putBoolean(cacheEnabled)
        if (cacheEnabled) {
            output.putUnsignedVarInt(usedBlobHashes.size)
            usedBlobHashes.forEach(output::putLLong)
        }
        output.putString(extraPayload)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleLevelChunk(this)

    companion object {
        @JvmStatic
        fun withoutCache(chunkX: Int, chunkZ: Int, subChunkCount: Int, payload: String) = LevelChunkPacket().apply {
            this.chunkX = chunkX
            this.chunkZ = chunkZ
            this.subChunkCount = subChunkCount
            this.extraPayload = payload

            this.cacheEnabled = false
        }

        @JvmStatic
        fun withCache(
            chunkX: Int,
            chunkZ: Int,
            subChunkCount: Int,
            usedBlobHashes: MutableList<Long>,
            extraPayload: String
        ) = LevelChunkPacket().apply {
            this.chunkX = chunkX
            this.chunkZ = chunkZ
            this.subChunkCount = subChunkCount
            this.extraPayload = extraPayload

            this.cacheEnabled = true
            this.usedBlobHashes = usedBlobHashes
        }
    }
}
