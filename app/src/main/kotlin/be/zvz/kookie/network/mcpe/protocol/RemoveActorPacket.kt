package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.REMOVE_ACTOR_PACKET)
class RemoveActorPacket : DataPacket(), ClientboundPacket {

    var entityUniqueId: Long = -1

    override fun decodePayload(input: PacketSerializer) {
        entityUniqueId = input.getEntityUniqueId()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(entityUniqueId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleRemoveActor(this)
    }
}
