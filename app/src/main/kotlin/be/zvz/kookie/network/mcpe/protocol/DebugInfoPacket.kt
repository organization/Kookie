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

@ProtocolIdentify(ProtocolInfo.IDS.DEBUG_INFO_PACKET)
class DebugInfoPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var entityUniqueId: Long = 0
    lateinit var data: String

    override fun decodePayload(input: PacketSerializer) {
        entityUniqueId = input.getEntityUniqueId()
        data = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(entityUniqueId)
        output.putString(data)
    }

    companion object {
        @JvmStatic
        fun create(entityUniqueId: Long, data: String) =
            DebugInfoPacket().apply {
                this.entityUniqueId = entityUniqueId
                this.data = data
            }
    }
}
