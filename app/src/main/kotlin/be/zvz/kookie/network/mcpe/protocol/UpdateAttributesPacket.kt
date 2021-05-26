package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.types.entity.NetworkAttribute
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_ATTRIBUTES_PACKET)
class UpdateAttributesPacket : DataPacket(), ClientboundPacket {

    var entityRuntimeId: Long = 0
    var entries: MutableList<NetworkAttribute> = mutableListOf()
    var tick: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        entries = input.getAttributeList()
        tick = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putAttributeList(entries)
        output.putUnsignedVarLong(tick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateAttributes(this)
    }
}
