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

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_FOG_PACKET)
class PlayerFogPacket : DataPacket(), ClientboundPacket {
    lateinit var fogLayers: List<String>

    companion object {
        @JvmStatic
        fun create(fogLayers: List<String>): PlayerFogPacket = PlayerFogPacket().apply {
            this.fogLayers = fogLayers
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        fogLayers = mutableListOf<String>().apply {
            for (i in 0 until input.getUnsignedVarInt()) {
                add(input.getString())
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(fogLayers.size)
        fogLayers.forEach { fogLayer ->
            output.putString(fogLayer)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePlayerFog(this)
}
