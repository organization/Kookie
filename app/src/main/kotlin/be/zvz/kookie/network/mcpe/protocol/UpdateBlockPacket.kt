package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_BLOCK_PACKET)
class UpdateBlockPacket : DataPacket(), ClientboundPacket {
    val pos = PacketSerializer.BlockPosition()
    var blockRuntimeId: Int = 0

    /**
     * Flags are used by MCPE internally for block setting, but only flag 2 (network flag) is relevant for network.
     * This field is pointless really.
     */
    var flags: Int = 0x02
    var dataLayerId: Int = DATA_LAYER_NORMAL

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(pos)
        blockRuntimeId = input.getUnsignedVarInt()
        flags = input.getUnsignedVarInt()
        dataLayerId = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(pos)
        output.putUnsignedVarInt(blockRuntimeId)
        output.putUnsignedVarInt(flags)
        output.putUnsignedVarInt(dataLayerId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateBlock(this)
    }

    companion object {
        const val DATA_LAYER_NORMAL = 0
        const val DATA_LAYER_LIQUID = 1
    }
}
