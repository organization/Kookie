package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityLink
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.NetworkAttribute
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.ADD_ACTOR_PACKET)
class AddActorPacket : DataPacket(), ClientboundPacket {

    var entityUniqueId: Long? = null
    var entityRuntimeId: Long = -1L
    var type: String = ""
    lateinit var position: Vector3
    var motion: Vector3? = null
    var pitch: Float = 0F
    var yaw: Float = 0F
    var headYaw: Float = 0F

    var metadata: MutableMap<Int, MetadataProperty> = mutableMapOf()

    val attributes: MutableList<NetworkAttribute> = mutableListOf()

    var links: MutableList<EntityLink> = mutableListOf()
    override fun decodePayload(input: PacketSerializer) {
        entityUniqueId = input.getEntityUniqueId()
        entityRuntimeId = input.getEntityRuntimeId()
        type = input.getString()
        position = input.getVector3()
        motion = input.getVector3()
        pitch = input.getLFloat()
        yaw = input.getLFloat()
        headYaw = input.getLFloat()

        for (i in 0..input.getUnsignedVarInt()) {
            val id = input.getString()
            val min = input.getLFloat()
            val current = input.getLFloat()
            val max = input.getLFloat()
            attributes.add(NetworkAttribute(id, min, max, current, current))
        }
        metadata = input.getEntityMetadataProperty()
        for (i in 0..input.getUnsignedVarInt()) {
            links.add(input.getEntityLink())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(entityUniqueId ?: entityRuntimeId)
        output.putEntityRuntimeId(entityRuntimeId)
        output.putString(type)
        output.putVector3(position)
        output.putVector3(motion ?: Vector3(0F, 0F, 0F))
        output.putLFloat(pitch)
        output.putLFloat(yaw)
        output.putLFloat(headYaw)

        output.putUnsignedVarInt(attributes.size)
        attributes.forEach {
            output.putString(it.getId())
            output.putLFloat(it.getMin())
            output.putLFloat(it.getCurrent())
            output.putLFloat(it.getMax())
        }
        output.putEntityMetadata(metadata)

        output.putUnsignedVarInt(links.size)
        links.forEach {
            output.putEntityLink(it)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        TODO("Not yet implemented")
    }
}
