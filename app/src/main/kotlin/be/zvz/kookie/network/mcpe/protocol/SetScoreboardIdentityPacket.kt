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
import be.zvz.kookie.network.mcpe.protocol.SetScorePacket.Type
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.ScoreboardIdentityPacketEntry

@ProtocolIdentify(ProtocolInfo.IDS.SET_SCOREBOARD_IDENTITY_PACKET)
class SetScoreboardIdentityPacket : DataPacket(), ClientboundPacket {
    lateinit var type: Type
    val entries = mutableListOf<ScoreboardIdentityPacketEntry>()

    override fun decodePayload(input: PacketSerializer) {
        type = Type.from(input.getByte())
        repeat(input.getUnsignedVarInt()) {
            entries.add(
                ScoreboardIdentityPacketEntry(
                    scoreboardId = input.getVarLong(),
                    entityUniqueId = if (type === Type.CHANGE) input.getEntityUniqueId() else null
                )
            )
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type.id)
        output.putUnsignedVarInt(entries.size)
        entries.forEach {
            output.putVarLong(it.scoreboardId)
            if (it.entityUniqueId != null) {
                output.putEntityUniqueId(it.entityUniqueId)
            }
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleSetScoreboardIdentity(this)
}
