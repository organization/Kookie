package be.zvz.kookie.network.mcpe.protocol.types.recipe

import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import java.util.*

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
            for (i in 0 until input.getUnsignedVarInt()) {
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
