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
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper

@ProtocolIdentify(ProtocolInfo.IDS.INVENTORY_CONTENT_PACKET)
class InventoryContentPacket : DataPacket(), ClientboundPacket {

    var windowId: Int = 0
    lateinit var items: MutableList<ItemStackWrapper>

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getUnsignedVarInt()
        for (i in 0 until input.getUnsignedVarInt()) {
            items.add(ItemStackWrapper.read(input))
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(windowId)
        output.putUnsignedVarInt(items.size)
        items.forEach {
            it.write(output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleInventoryContent(this)
    }

    companion object {
        @JvmStatic
        fun create(windowId: Int, items: MutableList<ItemStackWrapper>) = InventoryContentPacket().apply {
            this.windowId = windowId
            this.items = items
        }
    }
}
