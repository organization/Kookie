package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.protocol.InventoryTransactionPacket
import be.zvz.kookie.network.mcpe.protocol.PacketDecodeException
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class MismatchTransactionData : TransactionData() {

    override val typeId = InventoryTransactionPacket.TYPE_MISMATCH

    override fun decodeData(input: PacketSerializer) {
        if (getActions().size > 0) {
            throw PacketDecodeException("Mismatch transaction type should not have any actions associated with it, but got ${getActions().size}")
        }
    }

    override fun encodeData(output: PacketSerializer) {
    }
}
