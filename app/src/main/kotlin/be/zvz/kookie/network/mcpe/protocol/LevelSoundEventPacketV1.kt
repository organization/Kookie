package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

/**
 * Useless leftover from v1.9 refactor, does nothing
 */
@ProtocolIdentify(ProtocolInfo.IDS.LEVEL_SOUND_EVENT_PACKET_V1)
class LevelSoundEventPacketV1 : DataPacket() {
    override fun decodePayload(input: PacketSerializer) {
        TODO("LevelSoundEventPacketV1 doesn't use")
    }

    override fun encodePayload(output: PacketSerializer) {
        TODO("LevelSoundEventPacketV1 doesn't use")
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleLevelSoundEventPacketV1(this)
    }
}
