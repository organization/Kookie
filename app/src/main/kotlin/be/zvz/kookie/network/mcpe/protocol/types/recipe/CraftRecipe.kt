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
package be.zvz.kookie.network.mcpe.protocol.types.recipe

import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import java.util.UUID

abstract class CraftRecipe<I>(
    type: CraftingDataPacket.Entry,
    val recipeId: String,
    val inputs: I,
    val outputs: List<ItemStackWrapper>,
    val uuid: UUID,
    val blockName: String,
    val priority: Int,
    val recipeNetId: Int
) : RecipeWithTypeId(type) {

    override fun encode(output: PacketSerializer) {
        output.putUnsignedVarInt(outputs.size)
        outputs.forEach {
            it.write(output)
        }

        output.putString(recipeId)
        encodeInputs(output)
        output.putUUID(uuid)
        output.putString(blockName)
        output.putVarInt(priority)
        output.writeGenericTypeNetworkId(recipeNetId)
    }

    abstract fun encodeInputs(output: PacketSerializer)

    companion object {
        @JvmStatic
        protected fun <I, T> decodeWith(
            type: CraftingDataPacket.Entry,
            input: PacketSerializer,
            inputsDecoder: () -> I,
            constructor: (CraftingDataPacket.Entry, String, I, List<ItemStackWrapper>, UUID, String, Int, Int) -> T
        ): T {
            val recipeId = input.getString()
            val inputs = inputsDecoder()
            val outputs = mutableListOf<ItemStackWrapper>()
            repeat(input.getUnsignedVarInt()) {
                outputs.add(ItemStackWrapper.read(input))
            }
            val uuid = input.getUUID()
            val blockName = input.getString()
            val priority = input.getInt()
            val recipeNetId = input.readGenericTypeNetworkId()

            return constructor(
                type,
                recipeId,
                inputs,
                outputs,
                uuid,
                blockName,
                priority,
                recipeNetId
            )
        }
    }
}
