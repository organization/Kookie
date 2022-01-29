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
import be.zvz.kookie.network.mcpe.protocol.types.PlayerListEntry

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_LIST_PACKET)
class PlayerListPacket : DataPacket(), ClientboundPacket {

    lateinit var entries: List<PlayerListEntry>
    var type: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        type = input.getByte()
        entries = mutableListOf<PlayerListEntry>().apply {
            repeat(input.getUnsignedVarInt()) {
                val entry = PlayerListEntry()

                if (type == TYPE_ADD) {
                    entry.uuid = input.getUUID()
                    entry.entityUniqueId = input.getEntityUniqueId()
                    entry.username = input.getString()
                    entry.xboxUserId = input.getString()
                    entry.platformChatId = input.getString()
                    entry.buildPlatform = input.getLInt()
                    entry.skinData = input.getSkin()
                    entry.isTeacher = input.getBoolean()
                    entry.isHost = input.getBoolean()
                } else {
                    entry.uuid = input.getUUID()
                }

                add(entry)
            }
        }

        if (type == TYPE_ADD) {
            entries.forEach {
                it.skinData.isVerified = input.getBoolean()
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type)
        output.putUnsignedVarInt(entries.size)
        entries.forEach {
            if (type == TYPE_ADD) {
                output.putUUID(it.uuid)
                output.putEntityUniqueId(it.entityUniqueId)
                output.putString(it.username)
                output.putString(it.xboxUserId)
                output.putString(it.platformChatId)
                output.putLInt(it.buildPlatform)
                output.putSkin(it.skinData)
                output.putBoolean(it.isTeacher)
                output.putBoolean(it.isHost)
            } else {
                output.putUUID(it.uuid)
            }
        }
        if (type == TYPE_ADD) {
            entries.forEach {
                output.putBoolean(it.skinData.isVerified)
            }
        }
    }

    companion object {
        const val TYPE_ADD = 0
        const val TYPE_REMOVE = 1

        @JvmStatic
        fun add(entries: List<PlayerListEntry>) = PlayerListPacket().apply {
            this.type = TYPE_ADD
            this.entries = entries
        }

        @JvmStatic
        fun remove(entries: List<PlayerListEntry>) = PlayerListPacket().apply {
            this.type = TYPE_REMOVE
            this.entries = entries
        }
    }
}
