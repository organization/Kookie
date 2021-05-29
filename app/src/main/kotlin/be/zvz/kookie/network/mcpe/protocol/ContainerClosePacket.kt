package be.zvz.kookie.network.mcpe.protocol

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

class ContainerClosePacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.CONTAINER_CLOSE_PACKET)

    var windowId: Int
    var server: Boolean = false

    static
    fun create(windowId: Int, server: Boolean): self {
        result = new self
                result.windowId = windowId
        result.server = server
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        server = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putBool(server)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleContainerClose(this)
    }
}
