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

class ShapelessRecipe(
    type: CraftingDataPacket.Entry,
    recipeId: String,
    inputs: List<RecipeIngredient>,
    outputs: List<ItemStackWrapper>,
    uuid: UUID,
    blockName: String,
    priority: Int,
    recipeNetId: Int
) : CraftRecipe<List<RecipeIngredient>>(
    type,
    recipeId,
    inputs,
    outputs,
    uuid,
    blockName,
    priority,
    recipeNetId
) {

    override fun encodeInputs(output: PacketSerializer) {
        output.putUnsignedVarInt(inputs.size)
        inputs.forEach(output::putRecipeIngredient)
    }

    companion object {
        @JvmStatic
        fun decode(type: CraftingDataPacket.Entry, input: PacketSerializer): ShapelessRecipe =
            decodeWith(type, input, { decodeInputs(input) }, ::ShapelessRecipe)

        @JvmStatic
        fun decodeInputs(input: PacketSerializer): List<RecipeIngredient> = mutableListOf<RecipeIngredient>().apply {
            repeat(input.getUnsignedVarInt()) {
                add(input.getRecipeIngredient())
            }
        }
    }
}
