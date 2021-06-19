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
import com.koloboke.collect.map.hash.HashIntObjMaps
import java.util.UUID

class ShapedRecipe(
    type: CraftingDataPacket.Entry,
    recipeId: String,
    inputs: Map<Int, Map<Int, RecipeIngredient>>,
    outputs: List<ItemStackWrapper>,
    uuid: UUID,
    blockName: String,
    priority: Int,
    recipeNetId: Int
) : CraftRecipe<Map<Int, Map<Int, RecipeIngredient>>>(
    type,
    recipeId,
    inputs,
    outputs,
    uuid,
    blockName,
    priority,
    recipeNetId
) {

    companion object {
        @JvmStatic
        fun decode(type: CraftingDataPacket.Entry, input: PacketSerializer): ShapedRecipe =
            decodeWith(type, input, { decodeInputs(input) }, ::ShapedRecipe)

        @JvmStatic
        fun decodeInputs(input: PacketSerializer): Map<Int, Map<Int, RecipeIngredient>> =
            HashIntObjMaps.newMutableMap<Map<Int, RecipeIngredient>>().apply {
                repeat(input.getUnsignedVarInt()) { rowIndex ->
                    put(
                        rowIndex,
                        HashIntObjMaps.newMutableMap<RecipeIngredient>().apply {
                            repeat(input.getUnsignedVarInt()) { columnIndex ->
                                put(columnIndex, input.getRecipeIngredient())
                            }
                        }
                    )
                }
            }
    }

    override fun encodeInputs(output: PacketSerializer) {
        output.putUnsignedVarInt(inputs.size)
        inputs.values.forEach { it.values.forEach(output::putRecipeIngredient) }
    }
}
