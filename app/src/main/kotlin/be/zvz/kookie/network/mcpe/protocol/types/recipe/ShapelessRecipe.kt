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

    companion object {
        @JvmStatic
        fun decode(type: CraftingDataPacket.Entry, input: PacketSerializer): ShapelessRecipe =
            decodeWith(type, input, { decodeInputs(input) }, ::ShapelessRecipe)

        @JvmStatic
        fun decodeInputs(input: PacketSerializer): List<RecipeIngredient> = mutableListOf<RecipeIngredient>().apply {
            for (i in 0 until input.getUnsignedVarInt()) {
                add(input.getRecipeIngredient())
            }
        }
    }

    override fun encodeInputs(output: PacketSerializer) {
        output.putUnsignedVarInt(inputs.size)
        inputs.forEach {
            output.putRecipeIngredient(it)
        }
    }
}
