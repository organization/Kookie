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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_CLIENT_RESPONSE_PACKET)
class ResourcePackClientResponsePacket : DataPacket(), ServerboundPacket {

    var status: Int = -1

    var packIds: MutableList<String> = mutableListOf()

    override fun decodePayload(input: PacketSerializer) {
        status = input.getByte()
        for (i in 0..input.getLShort()) {
            packIds.add(input.getString())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(status)
        packIds.forEach {
            output.putString(it)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleResourcePackClientResponse(this)
    }

    companion object {
        const val STATUS_REFUSED = 1
        const val STATUS_SEND_PACKS = 2
        const val STATUS_HAVE_ALL_PACKS = 3
        const val STATUS_COMPLETED = 4
    }
}
