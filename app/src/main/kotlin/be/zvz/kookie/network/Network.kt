package be.zvz.kookie.network

import be.zvz.kookie.Server
import be.zvz.kookie.VersionInfo
import org.slf4j.Logger
import java.net.InetSocketAddress

class Network(private val server: Server, private val logger: Logger) {

    private val interfaces = mutableListOf<Any>() // TODO: change this *Any* type when Network interfaces are implemented

    private val bannedIps = HashMap<InetSocketAddress, Int>()

    private var name = ""

    private val sessionManager = null // TODO: change this when SessionManager is implemented

    init {
        setName(server.configGroup.getConfigString("motd", VersionInfo.NAME + " Server"))
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String = name
}
