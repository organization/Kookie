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
import be.zvz.kookie.network.mcpe.protocol.resourcepacks.ResourcePackType
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_DATA_INFO_PACKET)
class ResourcePackDataInfoPacket : DataPacket(), ClientboundPacket {

    lateinit var packId: String
    var maxChunkSize: Int = 0
    var chunkCount: Int = 0
    var compressedPackSize: Long = 0
    lateinit var sha256: String
    var isPremium: Boolean = false
    var packType: ResourcePackType = ResourcePackType.RESOURCES // TODO: check the values for this

    companion object {
        @JvmStatic
        fun create(
            packId: String,
            maxChunkSize: Int,
            chunkCount: Int,
            compressedPackSize: Long,
            sha256sum: String
        ): ResourcePackDataInfoPacket = ResourcePackDataInfoPacket().apply {
            this.packId = packId
            this.maxChunkSize = maxChunkSize
            this.chunkCount = chunkCount
            this.compressedPackSize = compressedPackSize
            this.sha256 = sha256sum
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        packId = input.getString()
        maxChunkSize = input.getLInt()
        chunkCount = input.getLInt()
        compressedPackSize = input.getLLong()
        sha256 = input.getString()
        isPremium = input.getBoolean()
        packType = ResourcePackType.from(input.getByte())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(packId)
        output.putLInt(maxChunkSize)
        output.putLInt(chunkCount)
        output.putLLong(compressedPackSize)
        output.putString(sha256)
        output.putBoolean(isPremium)
        output.putByte(packType.value)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleResourcePackDataInfo(this)
}
