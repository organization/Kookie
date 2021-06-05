package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_EVENT_PACKET)
class BlockEventPacket : DataPacket(), ClientboundPacket {
    val pos = PacketSerializer.BlockPosition()
    var eventType: Int = 0
    var eventData: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(pos)
        eventType = input.getVarInt()
        eventData = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(pos)
        output.putVarInt(eventType)
        output.putVarInt(eventData)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        const val EVENT_CHEST = 1

        const val CHEST_OPEN = 0
        const val CHEST_CLOSE = 1
    }
}
