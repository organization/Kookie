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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.recipe.FurnaceRecipe
import be.zvz.kookie.network.mcpe.protocol.types.recipe.MultiRecipe
import be.zvz.kookie.network.mcpe.protocol.types.recipe.PotionContainerChangeRecipe
import be.zvz.kookie.network.mcpe.protocol.types.recipe.PotionTypeRecipe
import be.zvz.kookie.network.mcpe.protocol.types.recipe.RecipeWithTypeId
import be.zvz.kookie.network.mcpe.protocol.types.recipe.ShapedRecipe
import be.zvz.kookie.network.mcpe.protocol.types.recipe.ShapelessRecipe

@ProtocolIdentify(ProtocolInfo.IDS.CRAFTING_DATA_PACKET)
class CraftingDataPacket : DataPacket(), ClientboundPacket {
    val entries = mutableListOf<RecipeWithTypeId>()
    val potionTypeRecipes = mutableListOf<PotionTypeRecipe>()
    val potionContainerRecipes = mutableListOf<PotionContainerChangeRecipe>()
    var cleanRecipes: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        for (i in 0 until input.getUnsignedVarInt()) {
            val type = Entry.from(input.getVarInt())
            entries.add(
                when (type) {
                    Entry.SHAPELESS, Entry.SHULKER_BOX, Entry.SHAPELESS_CHEMISTRY ->
                        ShapelessRecipe.decode(type, input)
                    Entry.SHAPED, Entry.SHAPED_CHEMISTRY ->
                        ShapedRecipe.decode(type, input)
                    Entry.FURNACE, Entry.FURNACE_DATA ->
                        FurnaceRecipe.decode(type, input)
                    Entry.MULTI ->
                        MultiRecipe.decode(type, input)
                    else -> throw PacketDecodeException("Unhandled recipe type ${type.value}!")
                }
            )
        }
        for (i in 0 until input.getUnsignedVarInt()) {
            potionTypeRecipes.add(
                PotionTypeRecipe(
                    inputItemId = input.getVarInt(),
                    inputItemMeta = input.getVarInt(),
                    ingredientItemId = input.getVarInt(),
                    ingredientItemMeta = input.getVarInt(),
                    outputItemId = input.getVarInt(),
                    outputItemMeta = input.getVarInt()
                )
            )
        }
        for (i in 0 until input.getUnsignedVarInt()) {
            potionContainerRecipes.add(
                PotionContainerChangeRecipe(
                    inputItemId = input.getVarInt(),
                    ingredientItemId = input.getVarInt(),
                    outputItemId = input.getVarInt()
                )
            )
        }
        cleanRecipes = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(entries.size)
        entries.forEach {
            it.encode(output)
        }
        output.putUnsignedVarInt(potionTypeRecipes.size)
        potionTypeRecipes.forEach {
            output.putVarInt(it.inputItemId)
            output.putVarInt(it.inputItemMeta)
            output.putVarInt(it.ingredientItemId)
            output.putVarInt(it.ingredientItemMeta)
            output.putVarInt(it.outputItemId)
            output.putVarInt(it.outputItemMeta)
        }
        output.putUnsignedVarInt(potionContainerRecipes.size)
        potionContainerRecipes.forEach {
            output.putVarInt(it.inputItemId)
            output.putVarInt(it.ingredientItemId)
            output.putVarInt(it.outputItemId)
        }
        output.putBoolean(cleanRecipes)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCraftingData(this)
    }

    enum class Entry(val value: Int) {
        UNKNOWN(-1),
        SHAPELESS(0),
        SHAPED(1),
        FURNACE(2),
        FURNACE_DATA(3),
        MULTI(4),
        SHULKER_BOX(5),
        SHAPELESS_CHEMISTRY(6),
        SHAPED_CHEMISTRY(7);

        companion object {
            private val VALUES = values()
            fun from(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
