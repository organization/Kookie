package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class InventoryTransactionPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    override fun decodePayload(input: PacketSerializer) {
    }

    override fun encodePayload(output: PacketSerializer) {
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return true
    }

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_MISMATCH = 1
        const val TYPE_USE_ITEM = 2
        const val TYPE_USE_ITEM_ON_ENTITY = 3
        const val TYPE_RELEASE_ITEM = 4
    }
}
