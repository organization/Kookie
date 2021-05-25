package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
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
    lateinit var motion: Vector3
    var pitch: Float = 0F
    var yaw: Float = 0F
    var headYaw: Float? = null
    lateinit var item: ItemStackWrapper
    var metadata: MutableMap<String, MetadataProperty> = HashObjObjMaps.newMutableMap()

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
        /*
        	$this->uuid = $in->getUUID();
		$this->username = $in->getString();
		$this->entityUniqueId = $in->getEntityUniqueId();
		$this->entityRuntimeId = $in->getEntityRuntimeId();
		$this->platformChatId = $in->getString();
		$this->position = $in->getVector3();
		$this->motion = $in->getVector3();
		$this->pitch = $in->getLFloat();
		$this->yaw = $in->getLFloat();
		$this->headYaw = $in->getLFloat();
		$this->item = ItemStackWrapper::read($in);
		$this->metadata = $in->getEntityMetadata();

		$this->uvarint1 = $in->getUnsignedVarInt();
		$this->uvarint2 = $in->getUnsignedVarInt();
		$this->uvarint3 = $in->getUnsignedVarInt();
		$this->uvarint4 = $in->getUnsignedVarInt();
		$this->uvarint5 = $in->getUnsignedVarInt();

		$this->long1 = $in->getLLong();

		$linkCount = $in->getUnsignedVarInt();
		for($i = 0; $i < $linkCount; ++$i){
			$this->links[$i] = $in->getEntityLink();
		}

		$this->deviceId = $in->getString();
		$this->buildPlatform = $in->getLInt();
         */
    }

    override fun encodePayload(output: PacketSerializer) {
        TODO("Not yet implemented")
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        TODO("Not yet implemented")
    }
}
