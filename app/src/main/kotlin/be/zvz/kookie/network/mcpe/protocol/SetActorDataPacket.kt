package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty

@ProtocolIdentify(ProtocolInfo.IDS.SET_ACTOR_DATA_PACKET)
class SetActorDataPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    var entityRuntimeId: Long = 0
    lateinit var metadata: MutableMap<Int, MetadataProperty>
    var tick: Long = 0

    companion object {
        fun create(entityRuntimeId: Long, metadata: HashMap<Int, MetadataProperty>, tick: Long): SetActorDataPacket {
            val packet = SetActorDataPacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.metadata = metadata
                this.tick = tick
            }

            return packet
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        metadata = input.getEntityMetadataProperty()
        tick = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putEntityMetadata(metadata)
        output.putUnsignedVarLong(tick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSetActorData(this)
    }
}
