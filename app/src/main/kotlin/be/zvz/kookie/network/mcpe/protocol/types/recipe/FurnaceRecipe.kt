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

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import com.nukkitx.protocol.bedrock.data.inventory.CraftingDataType

class FurnaceRecipe(
    type: CraftingDataType,
    val inputId: Int,
    val inputMeta: Int?,
    val result: ItemStackWrapper,
    val blockName: String
) : RecipeWithTypeId(type) {

    override fun encode(output: PacketSerializer) {
        output.putVarInt(inputId)
        if (inputMeta !== null) {
            output.putVarInt(inputMeta)
        }
        result.write(output)
        output.putString(blockName)
    }

    companion object {
        @JvmStatic
        fun decode(type: CraftingDataType, input: PacketSerializer) =
            FurnaceRecipe(
                type = type,
                inputId = input.getVarInt(),
                inputMeta = if (type === CraftingDataType.FURNACE) {
                    input.getVarInt()
                } else {
                    null
                },
                result = ItemStackWrapper.read(input),
                blockName = input.getString()
            )
    }
}
