package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.types.inventory.*
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.INVENTORY_TRANSACTION_PACKET)
class InventoryTransactionPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var requestId: Int = -1

    var requestChangedSlot = mutableListOf<InventoryTransactionChangedSlotsHack>()

    lateinit var trData: TransactionData

    override fun decodePayload(input: PacketSerializer) {
        requestId = input.readGenericTypeNetworkId()
        if (requestId != 0) {
            for (i in 0..input.getUnsignedVarInt()) {
                requestChangedSlot.add(InventoryTransactionChangedSlotsHack.read(input))
            }
        }

        val transactionType = input.getUnsignedVarInt()

        trData = when (transactionType) {
            TYPE_NORMAL -> NormalTransactionData()
            TYPE_MISMATCH -> MismatchTransactionData()
            TYPE_USE_ITEM -> UseItemTransactionData()
            TYPE_USE_ITEM_ON_ENTITY -> UseItemOnEntityTransactionData()
            else -> throw PacketDecodeException("Unknown transaction type $transactionType")
        }
        trData.decode(input)
    }

    override fun encodePayload(output: PacketSerializer) {
        output.writeGenericTypeNetworkId(requestId)
        if (requestId != 0) {
            output.putUnsignedVarInt(requestChangedSlot.size)
            requestChangedSlot.forEach {
                it.write(output)
            }
        }
        output.putUnsignedVarInt(trData.typeId)
        trData.encode(output)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleInventoryTransaction(this)
    }

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_MISMATCH = 1
        const val TYPE_USE_ITEM = 2
        const val TYPE_USE_ITEM_ON_ENTITY = 3
        const val TYPE_RELEASE_ITEM = 4
    }
}
