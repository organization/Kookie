package be.zvz.kookie.network.mcpe

import be.zvz.kookie.Server
import be.zvz.kookie.player.PlayerInfo
import org.slf4j.Logger
import java.net.InetSocketAddress
import java.util.*

class NetworkSession(private val server: Server, address: InetSocketAddress) {

    lateinit var logger: Logger

    private var ping: Int? = null

    private var info: PlayerInfo? = null

    private var connected = false

    private var connectedTime: Date = Date()

    init {

    }

    fun tick(): Boolean {
        if (info == null) {
            if (connectedTime.time <= Date().time + 10) {
                disconnect("Login Timeout")
                return false
            }
        }
        // TODO: add sendFlushBuffer() on here
        return true
    }

    fun disconnect(reason: String, notify: Boolean = true) {
    }
}
