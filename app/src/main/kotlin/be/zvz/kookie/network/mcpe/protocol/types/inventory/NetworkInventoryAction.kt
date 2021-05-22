package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.protocol.PacketDecodeException
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.lang.IllegalArgumentException

class NetworkInventoryAction {

    var sourceType: Int = -1

    var windowId: Int = -1

    var sourceFlags = 0
    var inventorySlot = 0

    lateinit var oldItem: ItemStackWrapper
    lateinit var newItem: ItemStackWrapper

    fun read(input: PacketSerializer): NetworkInventoryAction {
        when (input.getUnsignedVarInt()) {
            SOURCE_CONTAINER -> windowId = input.getVarInt()
            SOURCE_WORLD -> sourceFlags = input.getUnsignedVarInt()
            SOURCE_CREATIVE -> {}
            SOURCE_TODO -> windowId = input.getVarInt()
            else -> throw PacketDecodeException("Unknown inventory action source type this.sourceType")
        }
        inventorySlot = input.getUnsignedVarInt()
        oldItem = ItemStackWrapper.read(input)
        newItem = ItemStackWrapper.read(input)

        return this
    }

    fun write(output: PacketSerializer) {
        output.putUnsignedVarInt(sourceType)

        when (sourceType) {
            SOURCE_CONTAINER -> output.putVarInt(windowId)
            SOURCE_WORLD -> output.putUnsignedVarInt(sourceFlags)
            SOURCE_CREATIVE -> {}
            SOURCE_TODO -> output.putVarInt(windowId)
            else -> throw IllegalArgumentException("Unknown inventory action source type this.sourceType")
        }
    }

    companion object {
        const val SOURCE_CONTAINER = 0
        const val SOURCE_WORLD = 2 // drop, pickup item entity
        const val SOURCE_CREATIVE = 3
        const val SOURCE_TODO = 99999

        /**
         * Fake window IDs for the SOURCE_TODO type (99999)
         *
         * These identifiers are used for inventory source types which are not currently implemented server-side in MCPE.
         * As a general rule of thumb, anything that doesn't have a permanent inventory is client-side. These types are
         * to allow servers to track what is going on in client-side windows.
         *
         * Expect these to change in the future.
         */
        const val SOURCE_TYPE_CRAFTING_RESULT = -4
        const val SOURCE_TYPE_CRAFTING_USE_INGREDIENT = -5

        const val SOURCE_TYPE_ANVIL_RESULT = -12
        const val SOURCE_TYPE_ANVIL_OUTPUT = -13

        const val SOURCE_ENCHANT_OUTPUT = -17

        const val SOURCE_TYPE_TRADING_INPUT_1 = -20
        const val SOURCE_TYPE_TRADING_INPUT_2 = -21
        const val SOURCE_TYPE_TRADING_USE_INPUTS = -22
        const val SOURCE_TYPE_TRADING_OUTPUT = -23

        const val SOURCE_TYPE_BEACON = -24

        const val ACTION_MAGIC_SLOT_CREATIVE_DELETE_ITEM = 0
        const val ACTION_MAGIC_SLOT_PICKUP_ITEM = 1
    }
}
