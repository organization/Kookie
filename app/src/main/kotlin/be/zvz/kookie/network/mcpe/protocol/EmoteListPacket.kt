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
import java.util.*

@ProtocolIdentify(ProtocolInfo.IDS.EMOTE_LIST_PACKET)
class EmoteListPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var playerEntityRuntimeId: Long = 0
    lateinit var emoteIds: List<UUID>

    override fun decodePayload(input: PacketSerializer) {
        playerEntityRuntimeId = input.getEntityRuntimeId()
        emoteIds = mutableListOf<UUID>().apply {
            for (i in 0 until input.getUnsignedVarInt()) {
                add(input.getUUID())
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(playerEntityRuntimeId)
        output.putUnsignedVarInt(emoteIds.size)
        emoteIds.forEach {
            output.putUUID(it)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleEmoteList(this)
    }

    companion object {
        fun create(playerEntityRuntimeId: Long, emoteIds: List<UUID>) = EmoteListPacket().apply {
            this.playerEntityRuntimeId = playerEntityRuntimeId
            this.emoteIds = emoteIds
        }
    }
}
