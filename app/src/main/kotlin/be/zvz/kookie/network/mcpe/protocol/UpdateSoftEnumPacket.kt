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

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_SOFT_ENUM_PACKET)
class UpdateSoftEnumPacket : DataPacket(), ClientboundPacket {
    lateinit var enumName: String
    val values = mutableListOf<String>()
    var type: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        enumName = input.getString()
        repeat(input.getUnsignedVarInt()) {
            values.add(input.getString())
        }
        type = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(enumName)
        output.putUnsignedVarInt(values.size)
        values.forEach { v ->
            output.putString(v)
        }
        output.putByte(type)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleUpdateSoftEnum(this)

    enum class Type(val id: Int) {
        ADD(0),
        REMOVE(1),
        SET(2);

        companion object {
            @JvmStatic
            fun from(findValue: Int): Type = values().firstOrNull { it.id == findValue } ?: ADD
        }
    }
}
