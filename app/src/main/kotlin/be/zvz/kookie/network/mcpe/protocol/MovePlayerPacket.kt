package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.MOVE_PLAYER_PACKET)
class MovePlayerPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var entityRuntimeId: Long = -1
    lateinit var position: Vector3
    var pitch: Float = 0F
    var yaw: Float = 0F
    var headYaw: Float = 0F
    var mode: Int = MODE_NORMAL
    var onGround: Boolean = false
    var ridingEid: Long = 0
    var teleportCause: Int = 0
    var teleportItem: Int = 0
    var tick: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        position = input.getVector3()
        pitch = input.getLFloat()
        yaw = input.getLFloat()
        headYaw = input.getLFloat()
        mode = input.getByte()
        onGround = input.getBoolean()
        ridingEid = input.getEntityRuntimeId()
        if (mode == MODE_TELEPORT) {
            teleportCause = input.getLInt()
            teleportItem = input.getLInt()
        }
        tick = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVector3(position)
        output.putLFloat(pitch)
        output.putLFloat(yaw)
        output.putLFloat(headYaw)
        output.putByte(mode)
        output.putBoolean(onGround)
        output.putEntityRuntimeId(ridingEid)
        if (mode == MODE_TELEPORT) {
            output.putLInt(teleportCause)
            output.putLInt(teleportItem)
        }
        output.putUnsignedVarLong(tick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleMovePlayer(this)
    }

    companion object {
        const val MODE_NORMAL = 0
        const val MODE_RESET = 1
        const val MODE_TELEPORT = 2
        const val MODE_PITCH = 3; // facepalm Mojang
    }
}
