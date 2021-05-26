package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.SET_ACTOR_MOTION_PACKET)
class SetActorMotionPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    var entityRuntimeId: Long = 0
    lateinit var motion: Vector3

    companion object {
        fun create(entityRuntimeId: Long, motion: Vector3): SetActorMotionPacket {
            val packet = SetActorMotionPacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.motion = motion.asVector3()
            }

            return packet
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        motion = input.getVector3()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVector3(motion)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSetActorMotion(this)
    }
}
