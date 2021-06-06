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
package be.zvz.kookie.network.mcpe.protocol.types.inventory.stackrequest

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStack

@ItemStackRequestIdentify(ItemStackRequestActionType.CRAFTING_RESULTS_DEPRECATED_ASK_TY_LAING)
class DeprecatedCraftingResultsStackRequestAction(val results: MutableList<ItemStack>, val iterations: Int) :
    ItemStackRequestAction() {

    override fun write(out: PacketSerializer) {
        out.putUnsignedVarInt(results.size)
        results.forEach {
            out.putItemStackWithoutStackId(it)
        }
        out.putByte(iterations)
    }

    companion object {
        fun read(input: PacketSerializer): DeprecatedCraftingResultsStackRequestAction {
            val results = mutableListOf<ItemStack>()
            for (i in 0 until input.getUnsignedVarInt()) {
                results.add(input.getItemStackWithoutStackId())
            }
            val iterations = input.getByte()
            return DeprecatedCraftingResultsStackRequestAction(results, iterations)
        }
    }
}
