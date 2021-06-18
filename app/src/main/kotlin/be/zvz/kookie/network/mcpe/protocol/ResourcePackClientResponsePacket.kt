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

@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_CLIENT_RESPONSE_PACKET)
class ResourcePackClientResponsePacket : DataPacket(), ServerboundPacket {

    var status: Int = -1

    var packIds = mutableListOf<String>()

    override fun decodePayload(input: PacketSerializer) {
        status = input.getByte()
        repeat(input.getLShort()) {
            packIds.add(input.getString())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(status)
        packIds.forEach(output::putString)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleResourcePackClientResponse(this)

    enum class Status(val state: Int) {
        REFUSED(1),
        SEND_PACKS(2),
        HAVE_ALL_PACKS(3),
        COMPLETED(4),
    }
}
