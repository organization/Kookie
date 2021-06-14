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

class ItemStackResponse(
    val result: Int,
    val requestId: Int,
    val containerInfos: MutableList<ItemStackResponseContainerInfo>
) {
    fun write(output: PacketSerializer) {
        output.putByte(result)
        output.putUnsignedVarInt(requestId)
        output.putUnsignedVarInt(containerInfos.size)
        containerInfos.forEach {
            it.write(output)
        }
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer): ItemStackResponse {
            val result = input.getByte()
            val requestId = input.readGenericTypeNetworkId()
            val containerInfos = mutableListOf<ItemStackResponseContainerInfo>()
            for (i in 0 until input.getUnsignedVarInt()) {
                containerInfos.add(ItemStackResponseContainerInfo.read(input))
            }
            return ItemStackResponse(result, requestId, containerInfos)
        }
    }
}
