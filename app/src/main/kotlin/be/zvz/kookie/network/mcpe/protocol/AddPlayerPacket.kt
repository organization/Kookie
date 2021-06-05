package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.types.DeviceOS
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityLink
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.util.*

@ProtocolIdentify(ProtocolInfo.IDS.ADD_PLAYER_PACKET)
class AddPlayerPacket : DataPacket(), ClientboundPacket {

    lateinit var uuid: UUID
    var username: String = ""
    var entityUniqueId: Long? = null // TODO
    var entityRuntimeId: Long = -1
    var platformChatId: String = ""
    lateinit var position: Vector3
    var motion: Vector3 = Vector3()
    var pitch: Float = 0F
    var yaw: Float = 0F
    var headYaw: Float? = null
    lateinit var item: ItemStackWrapper
    var metadata: MutableMap<Int, MetadataProperty> = HashObjObjMaps.newMutableMap()

    var uvarint1: Int = 0
    var uvarint2: Int = 0
    var uvarint3: Int = 0
    var uvarint4: Int = 0
    var uvarint5: Int = 0

    var long1: Int = 0

    var links: MutableList<EntityLink> = mutableListOf()

    var deviceId: String = ""

    var buildPlatform: Int = DeviceOS.UNKNOWN

    override fun decodePayload(input: PacketSerializer) {
        uuid = input.getUUID()
        username = input.getString()
        entityUniqueId = input.getEntityUniqueId()
        entityRuntimeId = input.getEntityRuntimeId()
        platformChatId = input.getString()
        position = input.getVector3()
        motion = input.getVector3()
        pitch = input.getLFloat()
        yaw = input.getLFloat()
        headYaw = input.getLFloat()
        item = ItemStackWrapper.read(input)
        metadata = input.getEntityMetadataProperty()

        uvarint1 = input.getUnsignedVarInt()
        uvarint2 = input.getUnsignedVarInt()
        uvarint3 = input.getUnsignedVarInt()
        uvarint4 = input.getUnsignedVarInt()
        uvarint5 = input.getUnsignedVarInt()

        long1 = input.getLong().toInt()

        for (i in 0..input.getUnsignedVarInt()) {
            links.add(input.getEntityLink())
        }
        deviceId = input.getString()
        buildPlatform = input.getLInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUUID(uuid)
        output.putString(username)
        output.putEntityUniqueId(entityUniqueId ?: entityRuntimeId)
        output.putEntityRuntimeId(entityRuntimeId)
        output.putString(platformChatId)
        output.putVector3(position)
        output.putVector3(motion)
        output.putLFloat(pitch)
        output.putLFloat(yaw)
        output.putLFloat(headYaw ?: yaw)
        output.putEntityMetadata(metadata)

        output.putUnsignedVarInt(uvarint1)
        output.putUnsignedVarInt(uvarint2)
        output.putUnsignedVarInt(uvarint3)
        output.putUnsignedVarInt(uvarint4)
        output.putUnsignedVarInt(uvarint5)

        output.putLong(long1.toLong())

        output.putUnsignedVarInt(links.size)
        links.forEach {
            output.putEntityLink(it)
        }
        output.putString(deviceId)
        output.putLInt(buildPlatform)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAddPlayer(this)
    }
}
