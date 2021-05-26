package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.INTERACT_PACKET)
class InteractPacket : DataPacket(), ServerboundPacket {
    var action: Int = 0
    var target: Long = 0

    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f

    override fun decodePayload(input: PacketSerializer) {
        action = input.getByte()
        target = input.getEntityRuntimeId()

        if (this.action == ACTION_MOUSEOVER) {
            this.x = input.getLFloat()
            this.y = input.getLFloat()
            this.z = input.getLFloat()
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(action)
        output.putEntityRuntimeId(target)

        if (action == ACTION_MOUSEOVER) {
            output.putLFloat(x)
            output.putLFloat(y)
            output.putLFloat(z)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleInteract(this)
    }

    companion object {
        const val ACTION_LEAVE_VEHICLE = 3
        const val ACTION_MOUSEOVER = 4
        const val ACTION_OPEN_NPC = 5
        const val ACTION_OPEN_INVENTORY = 6
    }
}
