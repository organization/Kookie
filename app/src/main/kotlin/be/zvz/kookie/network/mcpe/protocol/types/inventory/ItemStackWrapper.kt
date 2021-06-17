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

data class ItemStackWrapper(val stackId: Int, val itemStack: ItemStack) {
    fun write(out: PacketSerializer) {
        out.putItemStack(itemStack) {
            if (stackId == 0) {
                it.putBoolean(false)
            } else {
                it.putBoolean(true)
                it.writeGenericTypeNetworkId(stackId)
            }
        }
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer): ItemStackWrapper {
            var stackId = 0
            val stack = input.getItemStack {
                val hasNetId = it.getBoolean()
                if (hasNetId) {
                    stackId = it.readGenericTypeNetworkId()
                }
            }
            return ItemStackWrapper(stackId, stack)
        }

        @JvmStatic
        fun legacy(itemStack: ItemStack): ItemStackWrapper = ItemStackWrapper(if (itemStack.id == 0) 0 else 1, itemStack)
    }
}
