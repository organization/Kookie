package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.TICK_SYNC_PACKET)
class TickSyncPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var clientSendTime: Long = 0
    var serverReceiveTime: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        clientSendTime = input.getLLong()
        serverReceiveTime = input.getLLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLLong(clientSendTime)
        output.putLLong(serverReceiveTime)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleTickSync(this)
    }
}