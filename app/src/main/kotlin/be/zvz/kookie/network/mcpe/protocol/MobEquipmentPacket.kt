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

@ProtocolIdentify(ProtocolInfo.IDS.MOB_EQUIPMENT_PACKET)
class MobEquipmentPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var entityRuntimeId: Long = 0
    lateinit var item: ItemStackWrapper
    var inventorySlot: Int = 0
    var hotbarSlot: Int = 0
    var windowId: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        item = ItemStackWrapper.read(input)
        inventorySlot = input.getByte()
        hotbarSlot = input.getByte()
        windowId = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        item.write(output)
        output.putByte(inventorySlot)
        output.putByte(hotbarSlot)
        output.putByte(windowId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleMobEquipment(this)
    }

    companion object {
        fun create(
            entityRuntimeId: Long,
            item: ItemStackWrapper,
            inventorySlot: Int,
            hotbarSlot: Int,
            windowId: Int
        ): MobEquipmentPacket {
            return MobEquipmentPacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.item = item
                this.inventorySlot = inventorySlot
                this.hotbarSlot = hotbarSlot
                this.windowId = windowId
            }
        }
    }
}
