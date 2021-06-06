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

@ProtocolIdentify(ProtocolInfo.IDS.EMOTE_PACKET)
class EmotePacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var entityRuntimeId: Long = 0
    lateinit var emoteId: String
    var flags: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        emoteId = input.getString()
        flags = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putString(emoteId)
        output.putByte(flags)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleEmote(this)
    }

    companion object {
        const val FLAG_SERVER = 1 shl 0

        fun create(entityRuntimeId: Long, emoteId: String, flags: Int): EmotePacket =
            EmotePacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.emoteId = emoteId
                this.flags = flags
            }
    }
}
