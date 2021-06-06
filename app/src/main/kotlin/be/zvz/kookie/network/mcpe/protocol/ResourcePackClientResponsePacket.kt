package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_CLIENT_RESPONSE_PACKET)
class ResourcePackClientResponsePacket : DataPacket(), ServerboundPacket {

    var status: Int = -1

    var packIds = mutableListOf<String>()

    override fun decodePayload(input: PacketSerializer) {
        status = input.getByte()
        for (i in 0 until input.getLShort()) {
            packIds.add(input.getString())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(status)
        packIds.forEach {
            output.putString(it)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleResourcePackClientResponse(this)
    }

    enum class Status(val state: Int) {
        REFUSED(1),
        SEND_PACKS(2),
        HAVE_ALL_PACKS(3),
        COMPLETED(4),
    }
}
