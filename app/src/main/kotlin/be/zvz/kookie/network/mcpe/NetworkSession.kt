package be.zvz.kookie.network.mcpe

import be.zvz.kookie.Server
import be.zvz.kookie.entity.Attribute
import be.zvz.kookie.entity.Living
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import com.nukkitx.network.raknet.RakNetSession
import com.nukkitx.network.util.DisconnectReason
import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import java.util.*

class NetworkSession(private val server: Server, private val session: RakNetSession) {

    val logger = LoggerFactory.getLogger(NetworkSession::class.java)
    private var ping: Int? = null
    private var player: Player? = null
    private var info: PlayerInfo? = null
    private var connected = false
    private var connectedTime = Date()
    private val sendBuffer = mutableListOf<ByteBuf>()

    init {
    }

    fun tick(): Boolean {
        info?.let {
            if (connectedTime.time <= Date().time + 10) {
                disconnect("Login Timeout")
                return false
            }
            return true
        }

        player?.let {
            it.doChunkRequest()
            val dirtyAttributes = it.attributeMap.needSend()
            syncAttributes(it, dirtyAttributes)
            dirtyAttributes.forEach { (_, attribute) ->
                attribute.markSynchronized()
            }
        }

        flushSendBuffer()
        return true
    }

    fun syncAttributes(entity: Living, attributes: Map<String, Attribute>) {
        if (attributes.isNotEmpty()) {
            TODO("sendDataPacket")
        }
    }

    private fun flushSendBuffer(immediate: Boolean = false) {
        if (sendBuffer.size > 0) {
            sendBuffer.forEach {
                session.send(it)
            }
            sendBuffer.clear()
        }
    }

    @JvmOverloads
    fun disconnect(reason: String, notify: Boolean = true) {
        session.disconnect(DisconnectReason.DISCONNECTED)
    }
}
