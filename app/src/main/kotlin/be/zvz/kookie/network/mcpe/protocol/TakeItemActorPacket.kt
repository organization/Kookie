package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.TAKE_ITEM_ACTOR_PACKET)
class TakeItemActorPacket : DataPacket(), ClientboundPacket {

    var target: Long = -1
    var eid: Long = -1

    override fun decodePayload(input: PacketSerializer) {
        target = input.getEntityRuntimeId()
        eid = input.getEntityRuntimeId()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(target)
        output.putEntityRuntimeId(eid)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleTakeItemActor(this)
    }
}
