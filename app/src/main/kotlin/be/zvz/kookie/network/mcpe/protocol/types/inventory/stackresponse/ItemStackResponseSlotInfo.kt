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
 * Copyright (C) 2021 - 2022 organization Team
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
    val hotbarSlot: Int,
    val count: Int,
    val itemStackId: Int,
    val customName: String,
    val durabilityCorrection: Int
) {
    fun write(output: PacketSerializer) {
        output.putByte(slot)
        output.putByte(hotbarSlot)
        output.putByte(count)
        output.writeGenericTypeNetworkId(itemStackId)
        output.putString(customName)
        output.putVarInt(durabilityCorrection)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer) = ItemStackResponseSlotInfo(
            slot = input.getByte(),
            hotbarSlot = input.getByte(),
            count = input.getByte(),
            itemStackId = input.readGenericTypeNetworkId(),
            customName = input.getString(),
            durabilityCorrection = input.getVarInt()
        )
    }
}
