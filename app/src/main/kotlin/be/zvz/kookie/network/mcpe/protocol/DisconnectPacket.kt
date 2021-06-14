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

@ProtocolIdentify(ProtocolInfo.IDS.DISCONNECT_PACKET)
class DisconnectPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var hideDisconnectionScreen: Boolean = false

    var message: String = ""

    override fun canBeSentBeforeLogin(): Boolean {
        return true
    }

    override fun decodePayload(input: PacketSerializer) {
        hideDisconnectionScreen = input.getBoolean()
        if (!hideDisconnectionScreen) {
            message = input.getString()
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBoolean(hideDisconnectionScreen)
        if (!hideDisconnectionScreen) {
            output.putString(message)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleDisconnect(this)
    }

    companion object {
        @JvmStatic
        fun silent(): DisconnectPacket =
            DisconnectPacket().apply {
                this.hideDisconnectionScreen = false
            }

        @JvmStatic
        fun message(message: String): DisconnectPacket =
            DisconnectPacket().apply {
                this.hideDisconnectionScreen = false
                this.message = message
            }
    }
}
