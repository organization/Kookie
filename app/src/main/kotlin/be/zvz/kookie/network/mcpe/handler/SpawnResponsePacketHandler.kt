package be.zvz.kookie.network.mcpe.handler

import com.nukkitx.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket

class SpawnResponsePacketHandler(val responseCallback: () -> Unit) : PacketHandler() {

    override fun handle(packet: SetLocalPlayerAsInitializedPacket): Boolean {
        responseCallback()
        return true
    }
}
