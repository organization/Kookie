package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.MOB_EFFECT_PACKET)
class MobEffectPacket : DataPacket(), ClientboundPacket {

    var entityRuntimeId: Long = 0
    var eventId: Int = 0
    var effectId: Int = 0
    var amplifier: Int = 0
    var particles: Boolean = true
    var duration: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        eventId = input.getByte()
        effectId = input.getVarInt()
        amplifier = input.getVarInt()
        particles = input.getBoolean()
        duration = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putByte(eventId)
        output.putVarInt(effectId)
        output.putVarInt(amplifier)
        output.putBoolean(particles)
        output.putVarInt(duration)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleMobEffect(this)
    }

    companion object {
        const val EVENT_ADD = 1;
        const val EVENT_MODIFY = 2;
        const val EVENT_REMOVE = 3;
    }
}