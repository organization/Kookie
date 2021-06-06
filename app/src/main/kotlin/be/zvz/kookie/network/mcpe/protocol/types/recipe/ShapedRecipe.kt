package be.zvz.kookie.network.mcpe.protocol.types.recipe

import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import com.koloboke.collect.map.hash.HashIntObjMaps
import java.util.*

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
        fun decode(type: CraftingDataPacket.Entry, input: PacketSerializer): ShapedRecipe =
            decodeWith(type, input, { decodeInputs(input) }, ::ShapedRecipe)

        fun decodeInputs(input: PacketSerializer): Map<Int, Map<Int, RecipeIngredient>> =
            HashIntObjMaps.newMutableMap<Map<Int, RecipeIngredient>>().apply {
                for (rowIndex in 0 until input.getUnsignedVarInt()) {
                    put(
                        rowIndex,
                        HashIntObjMaps.newMutableMap<RecipeIngredient>().apply {
                            for (columnIndex in 0 until input.getUnsignedVarInt()) {
                                put(columnIndex, input.getRecipeIngredient())
                            }
                        }
                    )
                }
            }
    }

    override fun encodeInputs(output: PacketSerializer) {
        output.putUnsignedVarInt(inputs.size)
        inputs.forEach { (_, row) ->
            row.forEach { (_, column) -> output.putRecipeIngredient(column) }
        }
    }
}
