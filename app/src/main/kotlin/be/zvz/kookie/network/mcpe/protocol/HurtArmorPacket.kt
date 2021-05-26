package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.HURT_ARMOR_PACKET)
class HurtArmorPacket : DataPacket(), ClientboundPacket {
    var cause: Int = 0
    var health: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        cause = input.getVarInt()
        health = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(cause)
        output.putVarInt(health)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleHurtArmor(this)
    }
}
