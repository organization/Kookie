package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.RIDER_JUMP_PACKET)
class RiderJumpPacket : DataPacket(), ServerboundPacket {

    var jumpLength: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        jumpLength = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(jumpLength)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleRiderJump(this)
    }
}
