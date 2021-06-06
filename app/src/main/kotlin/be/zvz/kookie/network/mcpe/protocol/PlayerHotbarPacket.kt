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
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ContainerIds

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_HOTBAR_PACKET)
class PlayerHotbarPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var selectedHotbarSlot: Int = 0
    var windowId: Int = ContainerIds.INVENTORY.id
    var selectHotbarSlot: Boolean = true

    override fun decodePayload(input: PacketSerializer) {
        selectedHotbarSlot = input.getUnsignedVarInt()
        windowId = input.getByte()
        selectHotbarSlot = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(selectedHotbarSlot)
        output.putByte(windowId)
        output.putBoolean(selectHotbarSlot)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerHotbar(this)
    }

    companion object {
        fun create(slot: Int, windowId: Int, selectSlot: Boolean = true) = PlayerHotbarPacket().apply {
            this.selectedHotbarSlot = slot
            this.windowId = windowId
            this.selectHotbarSlot = selectSlot
        }
    }
}
