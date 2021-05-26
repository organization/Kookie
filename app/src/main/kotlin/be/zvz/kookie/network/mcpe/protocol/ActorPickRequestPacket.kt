package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.ACTOR_PICK_REQUEST_PACKET)
class ActorPickRequestPacket : DataPacket(), ServerboundPacket {
    var entityUniqueId: Long = 0
    var hotbarSlot: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        entityUniqueId = input.getLLong()
        hotbarSlot = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLLong(entityUniqueId)
        output.putByte(hotbarSlot)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleActorPickRequest(this)
    }
}
