package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.InventoryTransactionPacket
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class UseItemOnEntityTransactionData : TransactionData() {

    override val typeId = InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY

    private var entityRuntimeId: Long = -1L

    private var actionType: Int = -1

    private var hotbarSlot: Int = -1

    private lateinit var itemInHand: ItemStackWrapper

    private lateinit var playerPos: Vector3

    private lateinit var clickPos: Vector3

    fun getActionType(): Int = actionType

    fun getHotbarSlot(): Int = hotbarSlot

    fun getItemInHand(): ItemStackWrapper = itemInHand

    fun getPlayerPos(): Vector3 = playerPos

    fun getClickPos(): Vector3 = clickPos

    override fun decodeData(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        actionType = input.getUnsignedVarInt()
        hotbarSlot = input.getVarInt()
        itemInHand = ItemStackWrapper.read(input)
        playerPos = input.getVector3()
        clickPos = input.getVector3()
    }

    override fun encodeData(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putUnsignedVarInt(actionType)
        output.putVarInt(hotbarSlot)
        itemInHand.write(output)
        output.putVector3(playerPos)
        output.putVector3(clickPos)
    }

    companion object {
        const val ACTION_INTERACT = 0
        const val ACTION_ATTACK = 1

        fun new(
            actions: MutableList<NetworkInventoryAction>,
            entityRuntimeId: Long,
            actionType: Int,
            hotbarSlot: Int,
            itemInHand: ItemStackWrapper,
            playerPos: Vector3,
            clickPos: Vector3
        ): UseItemOnEntityTransactionData {
            val result = UseItemOnEntityTransactionData()
            result.setActions(actions)
            result.entityRuntimeId = entityRuntimeId
            result.actionType = actionType
            result.hotbarSlot = hotbarSlot
            result.itemInHand = itemInHand
            result.playerPos = playerPos
            result.clickPos = clickPos
            return result
        }
    }
}
