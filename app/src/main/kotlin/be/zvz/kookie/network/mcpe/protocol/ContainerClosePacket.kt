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

@ProtocolIdentify(ProtocolInfo.IDS.CONTAINER_CLOSE_PACKET)
class ContainerClosePacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var windowId: Int = 0
    var server: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        server = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putBoolean(server)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleContainerClose(this)

    companion object {
        @JvmStatic
        fun create(windowId: Int, server: Boolean) =
            ContainerClosePacket().apply {
                this.windowId = windowId
                this.server = server
            }
    }
}
