package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.ADD_PAINTING_PACKET)
class AddPaintingPacket : DataPacket(), ClientboundPacket {
    var entityUniqueId: Long? = null
    var entityRuntimeId: Long = 0
    lateinit var position: Vector3
    var direction: Int = 0
    lateinit var title: String

    override fun decodePayload(input: PacketSerializer) {
        entityUniqueId = input.getEntityUniqueId()
        entityRuntimeId = input.getEntityRuntimeId()
        position = input.getVector3()
        direction = input.getVarInt()
        title = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(entityUniqueId ?: entityRuntimeId)
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVector3(position)
        output.putVarInt(direction)
        output.putString(title)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleAddPainting(this)
}
