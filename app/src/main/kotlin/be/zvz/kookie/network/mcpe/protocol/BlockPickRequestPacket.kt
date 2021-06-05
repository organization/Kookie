package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_PICK_REQUEST_PACKET)
class BlockPickRequestPacket : DataPacket(), ServerboundPacket {

    val blockX: AtomicInteger = AtomicInteger()
    val blockY: AtomicInteger = AtomicInteger()
    val blockZ: AtomicInteger = AtomicInteger()

    var addUserData: Boolean = false
    var hotbarSlot: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(blockX, blockY, blockZ)
        addUserData = input.getBoolean()
        hotbarSlot = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(blockX.get(), blockY.get(), blockZ.get())
        output.putBoolean(addUserData)
        output.putByte(hotbarSlot)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleBlockPickRequest(this)
    }
}
