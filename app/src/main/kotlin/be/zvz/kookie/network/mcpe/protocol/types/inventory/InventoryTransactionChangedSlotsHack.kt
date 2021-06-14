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
package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class InventoryTransactionChangedSlotsHack(
    private val containerId: Int,
    private val changedIndexSlotIndexes: MutableList<Int>
) {

    fun getContainerId(): Int = containerId

    fun getChangedSlotIndexes(): MutableList<Int> = changedIndexSlotIndexes

    fun write(output: PacketSerializer) {
        output.putByte(containerId)
        output.putUnsignedVarInt(changedIndexSlotIndexes.size)

        changedIndexSlotIndexes.forEach {
            output.putByte(it)
        }
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer): InventoryTransactionChangedSlotsHack {
            val containerId = input.getByte()
            val changedSlotIndexes = mutableListOf<Int>()
            for (i in 0 until input.getUnsignedVarInt()) {
                changedSlotIndexes.add(i)
            }
            return InventoryTransactionChangedSlotsHack(containerId, changedSlotIndexes)
        }
    }
}
