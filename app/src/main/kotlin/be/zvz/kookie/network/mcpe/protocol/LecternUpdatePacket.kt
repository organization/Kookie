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

@ProtocolIdentify(ProtocolInfo.IDS.LECTERN_UPDATE_PACKET)
class LecternUpdatePacket : DataPacket(), ServerboundPacket {

    var page: Int = 0
    var totalPages: Int = 0
    lateinit var position: PacketSerializer.BlockPosition
    var dropBook: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        page = input.getByte()
        totalPages = input.getByte()
        input.getBlockPosition(position)
        dropBook = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(page)
        output.putByte(totalPages)
        output.putBlockPosition(position)
        output.putBoolean(dropBook)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleLecternUpdate(this)
    }
}
