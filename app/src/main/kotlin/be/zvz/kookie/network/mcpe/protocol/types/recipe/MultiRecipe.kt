package be.zvz.kookie.network.mcpe.protocol.types.recipe

import be.zvz.kookie.network.mcpe.protocol.CraftingDataPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import java.util.UUID

class MultiRecipe(
    type: CraftingDataPacket.Entry,
    val recipeId: UUID,
    val recipeNetId: Int
) : RecipeWithTypeId(type) {

    override fun encode(output: PacketSerializer) {
        output.putUUID(recipeId)
        output.writeGenericTypeNetworkId(recipeNetId)
    }

    companion object {
        @JvmStatic
        fun decode(type: CraftingDataPacket.Entry, input: PacketSerializer) =
            MultiRecipe(
                type = type,
                recipeId = input.getUUID(),
                recipeNetId = input.readGenericTypeNetworkId()
            )

        enum class Type(val uuid: String) {
            REPAIR_ITEM("00000000-0000-0000-0000-000000000001"),
            MAP_EXTENDING("D392B075-4BA1-40AE-8789-AF868D56F6CE"),
            MAP_EXTENDING_CARTOGRAPHY("8B36268C-1829-483C-A0F1-993B7156A8F2"),
            MAP_CLONING("85939755-BA10-4D9D-A4CC-EFB7A8E943C4"),
            MAP_CLONING_CARTOGRAPHY("442D85ED-8272-4543-A6F1-418F90DED05D"),
            MAP_UPGRADING("AECD2294-4B94-434B-8667-4499BB2C9327"),
            MAP_UPGRADING_CARTOGRAPHY("98C84B38-1085-46BD-B1CE-DD38C159E6CC"),
            BOOK_CLONING("D1CA6B84-338E-4F2F-9C6B-76CC8B4BD98D"),
            BANNER_DUPLICATE("B5C5D105-75A2-4076-AF2B-923EA2BF4BF0"),
            BANNER_ADD_PATTERN("D81AAEAF-E172-4440-9225-868DF030D27B"),
            FIREWORKS("00000000-0000-0000-0000-000000000002"),
            MAP_LOCKING_CARTOGRAPH("602234E4-CAC1-4353-8BB7-B1EBFF70024B")
        }
    }
}
