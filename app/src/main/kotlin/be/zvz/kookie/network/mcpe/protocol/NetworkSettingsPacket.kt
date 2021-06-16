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

@ProtocolIdentify(ProtocolInfo.IDS.NETWORK_SETTINGS_PACKET)
class NetworkSettingsPacket : DataPacket(), ClientboundPacket {
    var compressionThreshold: Int = NOTHING

    companion object {
        const val NOTHING = 0
        const val EVERYTHING = 1

        @JvmStatic
        fun create(compressionThreshold: Int) = NetworkSettingsPacket().apply {
            this.compressionThreshold = compressionThreshold
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        compressionThreshold = input.getLShort()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLShort(compressionThreshold)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleNetworkSettings(this)
}
