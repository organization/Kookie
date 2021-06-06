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
import java.util.*

@ProtocolIdentify(ProtocolInfo.IDS.CRAFTING_EVENT_PACKET)
class CraftingEventPacket : DataPacket(), ServerboundPacket {

    var windowId: Int = -1
    var type: Int = -1
    lateinit var id: UUID
    val inputStacks = mutableListOf<ItemStackWrapper>()
    val outputStacks = mutableListOf<ItemStackWrapper>()

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        type = input.getVarInt()
        id = input.getUUID()

        for (i in 0 until Math.min(input.getUnsignedVarInt(), 128)) {
            inputStacks.add(ItemStackWrapper.read(input))
        }
        for (i in 0 until Math.min(input.getUnsignedVarInt(), 128)) {
            outputStacks.add(ItemStackWrapper.read(input))
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putVarInt(type)
        output.putUUID(id)

        output.putUnsignedVarInt(inputStacks.size)
        inputStacks.forEach { item ->
            item.write(output)
        }

        output.putUnsignedVarInt(outputStacks.size)
        outputStacks.forEach { item ->
            item.write(output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCraftingEvent(this)
    }
}
