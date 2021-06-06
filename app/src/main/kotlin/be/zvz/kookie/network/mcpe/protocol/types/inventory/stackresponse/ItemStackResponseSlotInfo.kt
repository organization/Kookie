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

class ItemStackResponseSlotInfo(
    val slot: Int,
    val horbarSlot: Int,
    val count: Int,
    val itemStackId: Int,
    val customName: String,
    val durabilityCorrection: Int
) {
    fun write(output: PacketSerializer) {
        output.putByte(slot)
        output.putByte(horbarSlot)
        output.putByte(count)
        output.writeGenericTypeNetworkId(itemStackId)
        output.putString(customName)
        output.putVarInt(durabilityCorrection)
    }

    companion object {
        fun read(input: PacketSerializer): ItemStackResponseSlotInfo {
            val slot = input.getByte()
            val horbarSlot = input.getByte()
            val count = input.getByte()
            val itemStackId = input.readGenericTypeNetworkId()
            val customName = input.getString()
            val durabilityCorrection = input.getVarInt()
            return ItemStackResponseSlotInfo(slot, horbarSlot, count, itemStackId, customName, durabilityCorrection)
        }
    }
}
