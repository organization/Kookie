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
package be.zvz.kookie.network.mcpe.protocol.types.inventory.stackresponse

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class ItemStackResponseContainerInfo(
    val containerId: Int,
    val slots: MutableList<ItemStackResponseSlotInfo>
) {
    fun write(output: PacketSerializer) {
        output.putByte(containerId)
        output.putUnsignedVarInt(slots.size)
        slots.forEach {
            it.write(output)
        }
    }

    companion object {
        fun read(input: PacketSerializer): ItemStackResponseContainerInfo {
            val containerId = input.getByte()
            val slots = mutableListOf<ItemStackResponseSlotInfo>()
            for (i in 0 until input.getUnsignedVarInt()) {
                slots.add(ItemStackResponseSlotInfo.read(input))
            }
            return ItemStackResponseContainerInfo(containerId, slots)
        }
    }
}
