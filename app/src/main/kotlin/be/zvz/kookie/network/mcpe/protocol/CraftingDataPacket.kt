package be.zvz.kookie.network.mcpe.protocol

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
use fun count

class CraftingDataPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.CRAFTING_DATA_PACKET)

	 const val ENTRY_SHAPELESS = 0
	 const val ENTRY_SHAPED = 1
	 const val ENTRY_FURNACE = 2
	 const val ENTRY_FURNACE_DATA = 3
	 const val ENTRY_MULTI = 4
	 const val ENTRY_SHULKER_BOX = 5
	 const val ENTRY_SHAPELESS_CHEMISTRY = 6
	 const val ENTRY_SHAPED_CHEMISTRY = 7

	/** @var RecipeWithTypeId[] */
	 entries = []
	/** @var PotionTypeRecipe[] */
	 potionTypeRecipes = []
	/** @var PotionContainerChangeRecipe[] */
	 potionContainerRecipes = []
	var cleanRecipes: Boolean = false

	override fun decodePayload(input: PacketSerializer) {
		recipeCount = input.getUnsignedVarInt()
		for(i = 0 i < recipeCount ++i){
			recipeType = input.getVarInt()

			switch(recipeType){
				case ENTRY_SHAPELESS:
				case ENTRY_SHULKER_BOX:
				case ENTRY_SHAPELESS_CHEMISTRY:
					entries[] = ShapelessRecipe::decode(recipeType, input)
					break
				case ENTRY_SHAPED:
				case ENTRY_SHAPED_CHEMISTRY:
					entries[] = ShapedRecipe::decode(recipeType, input)
					break
				case ENTRY_FURNACE:
				case ENTRY_FURNACE_DATA:
					entries[] = FurnaceRecipe::decode(recipeType, input)
					break
				case ENTRY_MULTI:
					entries[] = MultiRecipe::decode(recipeType, input)
					break
				default:
					throw new PacketDecodeException("Unhandled recipe type recipeType!") //do not continue attempting to decode
			}
		}
		for(i = 0, count = input.getUnsignedVarInt() i < count ++i){
			inputputId = input.getVarInt()
			inputputMeta = input.getVarInt()
			inputgredientId = input.getVarInt()
			inputgredientMeta = input.getVarInt()
			outputputId = input.getVarInt()
			outputputMeta = input.getVarInt()
			potionTypeRecipes[] = new PotionTypeRecipe(inputputId, inputputMeta, inputgredientId, inputgredientMeta, outputputId, outputputMeta)
		}
		for(i = 0, count = input.getUnsignedVarInt() i < count ++i){
			inputput = input.getVarInt()
			inputgredient = input.getVarInt()
			outputput = input.getVarInt()
			potionContainerRecipes[] = new PotionContainerChangeRecipe(inputput, inputgredient, outputput)
		}
		cleanRecipes = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(entries))
		foreach(entries d: as){
			output.putVarInt(d.getTypeId())
			d.encode(output)
		}
		output.putUnsignedVarInt(count(potionTypeRecipes))
		foreach(potionTypeRecipes recipe: as){
			output.putVarInt(recipe.getInputItemId())
			output.putVarInt(recipe.getInputItemMeta())
			output.putVarInt(recipe.getIngredientItemId())
			output.putVarInt(recipe.getIngredientItemMeta())
			output.putVarInt(recipe.getOutputItemId())
			output.putVarInt(recipe.getOutputItemMeta())
		}
		output.putUnsignedVarInt(count(potionContainerRecipes))
		foreach(potionContainerRecipes recipe: as){
			output.putVarInt(recipe.getInputItemId())
			output.putVarInt(recipe.getIngredientItemId())
			output.putVarInt(recipe.getOutputItemId())
		}

		output.putBool(cleanRecipes)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleCraftingData(this)
	}
}
