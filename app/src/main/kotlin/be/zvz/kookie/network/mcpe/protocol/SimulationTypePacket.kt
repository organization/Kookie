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

@ProtocolIdentify(ProtocolInfo.IDS.SIMULATION_TYPE_PACKET)
class SimulationTypePacket : DataPacket(), ClientboundPacket {

    enum class Type(val type: Int) {
        GAME(0),
        EDITOR(1),
        TEST(2)
    }

    lateinit var type: Type

    override fun decodePayload(input: PacketSerializer) {
        type = when (input.getByte()) {
            0 -> Type.GAME
            1 -> Type.EDITOR
            2 -> Type.TEST
            else -> throw PacketDecodeException("Unknown game type given")
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type.type)
    }
}
