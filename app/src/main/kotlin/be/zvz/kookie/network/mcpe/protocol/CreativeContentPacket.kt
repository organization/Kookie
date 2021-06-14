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
import be.zvz.kookie.network.mcpe.protocol.types.inventory.CreativeContentEntry

@ProtocolIdentify(ProtocolInfo.IDS.CREATIVE_CONTENT_PACKET)
class CreativeContentPacket : DataPacket(), ClientboundPacket {
    lateinit var entries: List<CreativeContentEntry>
        private set

    companion object {
        @JvmStatic
        fun create(entries: List<CreativeContentEntry>): CreativeContentPacket = CreativeContentPacket().apply {
            this.entries = entries
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        entries = mutableListOf<CreativeContentEntry>().apply {
            for (i in 0 until input.getUnsignedVarInt()) {
                add(CreativeContentEntry.read(input))
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(entries.size)
        entries.forEach { entry ->
            entry.write(output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCreativeContent(this)
    }
}
