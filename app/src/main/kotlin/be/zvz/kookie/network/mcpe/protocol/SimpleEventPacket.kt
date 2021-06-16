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

@ProtocolIdentify(ProtocolInfo.IDS.SIMPLE_EVENT_PACKET)
class SimpleEventPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var eventType: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        eventType = input.getLShort()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLShort(eventType)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleSimpleEvent(this)

    companion object {
        const val TYPE_ENABLE_COMMANDS = 1
        const val TYPE_DISABLE_COMMANDS = 2
        const val TYPE_UNLOCK_WORLD_TEMPLATE_SETTINGS = 3
    }
}
