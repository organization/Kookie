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

import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.network.mcpe.protocol.serializer.NetworkNbtSerializer
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.ItemComponentPacketEntry

@ProtocolIdentify(ProtocolInfo.IDS.ITEM_COMPONENT_PACKET)
class ItemComponentPacket : DataPacket(), ClientboundPacket {

    lateinit var entries: List<ItemComponentPacketEntry>

    override fun decodePayload(input: PacketSerializer) {
        entries = mutableListOf<ItemComponentPacketEntry>().apply {
            repeat(input.getUnsignedVarInt()) {
                val name = input.getString()
                val nbt = input.getNbtCompoundRoot()
                add(ItemComponentPacketEntry(name, nbt))
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(entries.size)
        entries.forEach {
            output.putString(it.name)
            output.put(NetworkNbtSerializer().write(TreeRoot(it.componentNbt)))
        }
    }

    companion object {
        @JvmStatic
        fun create(entries: List<ItemComponentPacketEntry>) =
            ItemComponentPacket().apply {
                this.entries = entries
            }
    }
}
