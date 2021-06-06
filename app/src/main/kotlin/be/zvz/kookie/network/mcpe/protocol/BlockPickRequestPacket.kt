package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_PICK_REQUEST_PACKET)
class BlockPickRequestPacket : DataPacket(), ServerboundPacket {
    val pos = PacketSerializer.BlockPosition()
    var addUserData: Boolean = false
    var hotbarSlot: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(pos)
        addUserData = input.getBoolean()
        hotbarSlot = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(pos)
        output.putBoolean(addUserData)
        output.putByte(hotbarSlot)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleBlockPickRequest(this)
    }
}
