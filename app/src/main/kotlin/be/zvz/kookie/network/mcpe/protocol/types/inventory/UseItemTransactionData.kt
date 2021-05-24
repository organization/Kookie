package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.InventoryTransactionPacket
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

class UseItemTransactionData : TransactionData() {

    override val typeId = InventoryTransactionPacket.TYPE_USE_ITEM

    private var actionType: Int = -1

    private lateinit var blockPos: Vector3

    private var face: Int = -1

    private var hotbarSlot: Int = -1

    private lateinit var itemInHand: ItemStackWrapper

    private lateinit var playerPos: Vector3

    private lateinit var clickPos: Vector3

    private var blockRuntimeId: Int = -1

    fun getActionType(): Int = actionType

    fun getBlockPos(): Vector3 = blockPos

    fun getFace(): Int = face

    fun getHotbarSlot(): Int = hotbarSlot

    fun getItemInHand(): ItemStackWrapper = itemInHand

    fun getPlayerPos(): Vector3 = playerPos

    fun getClickPos(): Vector3 = clickPos

    fun getBlockRuntimeId(): Int = blockRuntimeId

    override fun decodeData(input: PacketSerializer) {
        actionType = input.getUnsignedVarInt()

        val x = AtomicInteger()
        val y = AtomicInteger()
        val z = AtomicInteger()
        input.getBlockPosition(x, y, z)
        blockPos = Vector3(x.get().toFloat(), y.get().toFloat(), z.get().toFloat())
        face = input.getVarInt()
        itemInHand = ItemStackWrapper.read(input)
        playerPos = input.getVector3()
        clickPos = input.getVector3()
        blockRuntimeId = input.getUnsignedVarInt()
    }

    override fun encodeData(output: PacketSerializer) {
        output.putUnsignedVarInt(actionType)
        output.putBlockPosition(blockPos.x.toInt(), blockPos.y.toInt(), blockPos.z.toInt())
        output.putVarInt(face)
        output.putVarInt(hotbarSlot)
        itemInHand.write(output)
        output.putVector3(playerPos)
        output.putVector3(clickPos)
        output.putUnsignedVarInt(blockRuntimeId)
    }

    companion object {
        const val ACTION_CLICK_BLOCK = 0
        const val ACTION_CLICK_AIR = 1
        const val ACTION_BREAK_BLOCK = 2

        fun new(
            actions: MutableList<NetworkInventoryAction>,
            actionType: Int,
            blockPos: Vector3,
            face: Int,
            hotbarSlot: Int,
            itemInHand: ItemStackWrapper,
            playerPos: Vector3,
            clickPos: Vector3,
            blockRuntimeId: Int
        ): UseItemTransactionData {
            val result = UseItemTransactionData()
            result.setActions(actions)
            result.actionType = actionType
            result.blockPos = blockPos
            result.face = face
            result.hotbarSlot = hotbarSlot
            result.itemInHand = itemInHand
            result.playerPos = playerPos
            result.clickPos = clickPos
            result.blockRuntimeId = blockRuntimeId
            return result
        }
    }
}
