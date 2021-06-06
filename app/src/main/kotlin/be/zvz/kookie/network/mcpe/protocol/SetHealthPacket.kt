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

@ProtocolIdentify(ProtocolInfo.IDS.SET_HEALTH_PACKET)
class SetHealthPacket : DataPacket(), ClientboundPacket {

    var health: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        health = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(health)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSetHealth(this)
    }
}
