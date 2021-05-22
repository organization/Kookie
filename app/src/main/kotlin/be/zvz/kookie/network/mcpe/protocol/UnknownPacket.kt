package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.UNKNOWN)
class UnknownPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    override fun canBeSentBeforeLogin(): Boolean {
        return true
    }

    override fun decodePayload(input: PacketSerializer) {
        // NO PAYLOAD
    }

    override fun encodePayload(output: PacketSerializer) {
        // NO PAYLOAD
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUnknown(this)
    }
}
