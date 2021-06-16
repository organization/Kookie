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
import be.zvz.kookie.network.mcpe.protocol.types.ScorePacketEntry
import be.zvz.kookie.network.mcpe.protocol.types.ScorePacketEntry.Type as EntryType

@ProtocolIdentify(ProtocolInfo.IDS.SET_SCORE_PACKET)
class SetScorePacket : DataPacket(), ClientboundPacket {
    lateinit var type: Type
    val entries = mutableListOf<ScorePacketEntry>()

    override fun decodePayload(input: PacketSerializer) {
        type = Type.from(input.getByte())
        repeat(input.getUnsignedVarInt()) {
            val entry =
                ScorePacketEntry(
                    scoreboardId = input.getVarLong(),
                    objectiveName = input.getString(),
                    score = input.getLInt()
                )

            if (type == Type.CHANGE) {
                entry.type = EntryType.from(input.getByte())
                when (entry.type) {
                    EntryType.PLAYER, EntryType.ENTITY -> entry.entityUniqueId = input.getEntityUniqueId()
                    EntryType.FAKE_PLAYER -> entry.customName = input.getString()
                }
            }

            entries.add(entry)
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type.id)
        output.putUnsignedVarInt(entries.size)
        entries.forEach {
            output.putVarLong(it.scoreboardId)
            output.putString(it.objectiveName)
            output.putLInt(it.score)
            it.type?.let { type ->
                output.putByte(type.id)
                when (type) {
                    EntryType.PLAYER, EntryType.ENTITY -> output.putEntityUniqueId(it.entityUniqueId)
                    EntryType.FAKE_PLAYER -> output.putString(it.customName)
                }
            }
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleSetScore(this)

    enum class Type(val id: Int) {
        CHANGE(0),
        REMOVE(1);

        companion object {
            private val VALUES = values()
            @JvmStatic
            fun from(value: Int) = VALUES.firstOrNull { it.id == value }
                ?: throw PacketDecodeException("Unhandled set score type $value!")
        }
    }
}
