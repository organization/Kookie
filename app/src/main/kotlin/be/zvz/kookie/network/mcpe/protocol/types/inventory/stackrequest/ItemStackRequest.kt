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

class ItemStackRequest(
    val requestId: Int,
    val actions: MutableList<ItemStackRequestAction>,
    val filterStrings: MutableList<String>
) {

    fun write(out: PacketSerializer) {
        out.writeGenericTypeNetworkId(requestId)
        out.putUnsignedVarInt(actions.size)
        actions.forEach {
            out.putByte(getType(it::class.java))
            it.write(out)
        }
        out.putUnsignedVarInt(filterStrings.size)
        filterStrings.forEach {
            out.putString(it)
        }
    }

    companion object {
        private fun getType(clazz: Class<out ItemStackRequestAction>) =
            clazz.getAnnotation(ItemStackRequestIdentify::class.java)!!.typeId.type
        private val actionMap = mapOf(
            getType(TakeStackRequestAction::class.java) to TakeStackRequestAction::read,
            getType(PlaceStackRequestAction::class.java) to PlaceStackRequestAction::read,
            getType(SwapStackRequestAction::class.java) to SwapStackRequestAction::read,
            getType(DropStackRequestAction::class.java) to DropStackRequestAction::read,
            getType(DestroyStackRequestAction::class.java) to DestroyStackRequestAction::read,
            getType(CraftingConsumeInputStackRequestAction::class.java) to CraftingConsumeInputStackRequestAction::read,
            getType(
                CraftingMarkSecondaryResultStackRequestAction::class.java
            ) to CraftingMarkSecondaryResultStackRequestAction::read,
            getType(LabTableCombineStackRequestAction::class.java) to LabTableCombineStackRequestAction::read,
            getType(BeaconPaymentStackRequestAction::class.java) to BeaconPaymentStackRequestAction::read,
            getType(MineBlockStackRequestAction::class.java) to MineBlockStackRequestAction::read,
            getType(CraftRecipeStackRequestAction::class.java) to CraftRecipeStackRequestAction::read,
            getType(CraftRecipeAutoStackRequestAction::class.java) to CraftRecipeAutoStackRequestAction::read,
            getType(CreativeCreateStackRequestAction::class.java) to CreativeCreateStackRequestAction::read,
            getType(CraftRecipeOptionalStackRequestAction::class.java) to CraftRecipeOptionalStackRequestAction::read,
            getType(
                DeprecatedCraftingNonImplementedStackRequestAction::class.java
            ) to DeprecatedCraftingNonImplementedStackRequestAction::read,
            getType(DeprecatedCraftingResultsStackRequestAction::class.java) to DeprecatedCraftingResultsStackRequestAction::read
        )

        fun readAction(input: PacketSerializer, typeId: Int): ItemStackRequestAction =
            (actionMap[typeId] ?: throw IllegalArgumentException("Unhandled item stack request action type $typeId"))(input)

        fun read(input: PacketSerializer): ItemStackRequest {
            val requestId = input.readGenericTypeNetworkId()
            val actions = mutableListOf<ItemStackRequestAction>()
            for (i in 0 until input.getUnsignedVarInt()) {
                val typeId = input.getByte()
                actions.add(readAction(input, typeId))
            }
            val filterStrings = mutableListOf<String>()
            for (i in 0 until input.getUnsignedVarInt()) {
                filterStrings.add(input.getString())
            }
            return ItemStackRequest(requestId, actions, filterStrings)
        }
    }
}
