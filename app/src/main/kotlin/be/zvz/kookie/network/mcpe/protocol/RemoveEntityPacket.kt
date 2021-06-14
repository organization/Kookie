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

@ProtocolIdentify(ProtocolInfo.IDS.REMOVE_ENTITY_PACKET)
class RemoveEntityPacket : DataPacket(), ClientboundPacket {
    var uvarInt1: Int = 0

    companion object {
        @JvmStatic
        fun create(uvarInt1: Int): RemoveEntityPacket {
            return RemoveEntityPacket().apply {
                this.uvarInt1 = uvarInt1
            }
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        uvarInt1 = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(uvarInt1)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleRemoveEntity(this)
}
