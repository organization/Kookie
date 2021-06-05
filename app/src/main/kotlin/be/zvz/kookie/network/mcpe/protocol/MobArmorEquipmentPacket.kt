package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper

@ProtocolIdentify(ProtocolInfo.IDS.MOB_ARMOR_EQUIPMENT_PACKET)
class MobArmorEquipmentPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    var entityRuntimeId: Long = 0

    lateinit var head: ItemStackWrapper
    lateinit var chest: ItemStackWrapper
    lateinit var legs: ItemStackWrapper
    lateinit var feet: ItemStackWrapper

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        head = ItemStackWrapper.read(input)
        chest = ItemStackWrapper.read(input)
        legs = ItemStackWrapper.read(input)
        feet = ItemStackWrapper.read(input)
    }
    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        head.write(output)
        chest.write(output)
        legs.write(output)
        feet.write(output)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleMobArmorEquipment(this)

    companion object {
        fun create(
            entityRuntimeId: Long,
            head: ItemStackWrapper,
            chest: ItemStackWrapper,
            legs: ItemStackWrapper,
            feet: ItemStackWrapper,
        ): MobArmorEquipmentPacket {
            val packet = MobArmorEquipmentPacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.head = head
                this.chest = chest
                this.legs = legs
                this.feet = feet
            }
            return packet
        }
    }
}
