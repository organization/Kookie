package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class InventoryTransactionChangedSlotsHack(
    private val containerId: Int,
    private val changedIndexSlotIndexes: MutableList<Int>
) {

    fun getContainerId(): Int = containerId

    fun getChangedSlotIndexes(): MutableList<Int> = changedIndexSlotIndexes

    fun write(output: PacketSerializer) {
        output.putByte(containerId)
        output.putUnsignedVarInt(changedIndexSlotIndexes.size)

        changedIndexSlotIndexes.forEach {
            output.putByte(it)
        }
    }

    companion object {
        fun read(input: PacketSerializer): InventoryTransactionChangedSlotsHack {
            val containerId = input.getByte()
            val changedSlotIndexes = mutableListOf<Int>()
            for (i in 0..input.getUnsignedVarInt()) {
                changedSlotIndexes.add(i)
            }
            return InventoryTransactionChangedSlotsHack(containerId, changedSlotIndexes)
        }
    }
}
