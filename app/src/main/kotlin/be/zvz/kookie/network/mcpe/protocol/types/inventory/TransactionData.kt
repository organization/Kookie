package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

abstract class TransactionData {

    private val actions: MutableList<NetworkInventoryAction> = mutableListOf()

    abstract val typeId: Int

    fun getActions(): MutableList<NetworkInventoryAction> {
        return actions
    }

    fun decode(input: PacketSerializer) {
        for (i in 0..input.getUnsignedVarInt()) {
            actions.add(NetworkInventoryAction().read(input))
        }
    }

    abstract fun decodeData(input: PacketSerializer)
}
