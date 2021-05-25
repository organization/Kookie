package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.SET_TIME_PACKET)
class SetTimePacket : DataPacket(), ClientboundPacket {

    var time: Int = -1

    override fun decodePayload(input: PacketSerializer) {
        time = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(time)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSetTime(this)
    }

    companion object {
        fun create(time: Int): SetTimePacket {
            val result = SetTimePacket()
            result.time = time and 0xffffffff.toInt()
            return result
        }
    }
}