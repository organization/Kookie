package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.InventoryTransactionPacket
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class ReleaseItemTransactionData : TransactionData() {

    override val typeId = InventoryTransactionPacket.TYPE_RELEASE_ITEM

    private var actionType: Int = -1

    private var hotbarSlot: Int = -1

    private lateinit var itemInHand: ItemStackWrapper

    private lateinit var headPos: Vector3

    fun getActionType(): Int = actionType

    fun getHotbarSlot(): Int = hotbarSlot

    fun getItemInHand(): ItemStackWrapper = itemInHand

    fun getHeadPos(): Vector3 = headPos

    override fun decodeData(input: PacketSerializer) {
        actionType = input.getUnsignedVarInt()
        hotbarSlot = input.getVarInt()
        itemInHand = ItemStackWrapper.read(input)
        headPos = input.getVector3()
    }

    override fun encodeData(output: PacketSerializer) {
        output.putUnsignedVarInt(actionType)
        output.putVarInt(hotbarSlot)
        itemInHand.write(output)
        output.putVector3(headPos)
    }

    companion object {
        fun new(
            actions: MutableList<NetworkInventoryAction>,
            actionType: Int,
            hotbarSlot: Int,
            itemInHand: ItemStackWrapper,
            headPos: Vector3
        ): ReleaseItemTransactionData {
            val result = ReleaseItemTransactionData()
            result.setActions(actions)
            result.actionType = actionType
            result.hotbarSlot = hotbarSlot
            result.itemInHand = itemInHand
            result.headPos = headPos
            return result
        }
    }
}