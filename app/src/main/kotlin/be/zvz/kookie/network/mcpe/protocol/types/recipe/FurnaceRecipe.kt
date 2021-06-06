package be.zvz.kookie.network.mcpe.protocol.types.recipe

import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper

class FurnaceRecipe(
    type: CraftingDataPacket.Entry,
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
        fun decode(type: CraftingDataPacket.Entry, input: PacketSerializer) =
            FurnaceRecipe(
                type = type,
                inputId = input.getVarInt(),
                inputMeta = if (type === CraftingDataPacket.Entry.FURNACE_DATA) {
                    input.getVarInt()
                } else {
                    null
                },
                result = ItemStackWrapper.read(input),
                blockName = input.getString()
            )
    }
}
