package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper

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

@ProtocolIdentify(ProtocolInfo.IDS.INVENTORY_SLOT_PACKET)
class InventorySlotPacket : DataPacket(), ClientboundPacket {

    var windowId: Int = 0
    var inventorySlot: Int = 0
    lateinit var item: ItemStackWrapper

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getUnsignedVarInt()
        inventorySlot = input.getUnsignedVarInt()
        item = ItemStackWrapper.read(input)
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(windowId)
        output.putUnsignedVarInt(inventorySlot)
        item.write(output)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleInventorySlot(this)
    }

    companion object {
        @JvmStatic
        fun create(windowId: Int, slot: Int, item: ItemStackWrapper) =
            InventorySlotPacket().apply {
                this.inventorySlot = slot
                this.item = item
                this.windowId = windowId
            }
    }
}
