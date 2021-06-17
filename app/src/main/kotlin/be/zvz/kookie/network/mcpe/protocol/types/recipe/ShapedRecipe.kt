package be.zvz.kookie.network.mcpe.protocol.types.recipe

import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import be.zvz.kookie.utils.inline.forEachValue
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
        inputs.forEachValue { it.forEachValue(output::putRecipeIngredient) }
    }
}
