package be.zvz.kookie.network.mcpe

import com.nukkitx.network.raknet.EncapsulatedPacket
import com.nukkitx.network.raknet.RakNetSessionListener
import com.nukkitx.network.raknet.RakNetState
import com.nukkitx.network.util.DisconnectReason
import io.netty.buffer.ByteBuf

class NetworkPacketListener(private val session: NetworkSession) : RakNetSessionListener {
    override fun onSessionChangeState(state: RakNetState) {
        TODO("Not yet implemented")
    }

    override fun onDisconnect(reason: DisconnectReason) {
        TODO("Not yet implemented")
    }

    override fun onEncapsulated(packet: EncapsulatedPacket) {
        TODO("Not yet implemented")
    }

    override fun onDirect(buf: ByteBuf) {
        session.handleBuffer(buf)
    }
}