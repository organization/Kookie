package be.zvz.kookie.network.mcpe.raklib

import be.zvz.kookie.Server
import be.zvz.kookie.VersionInfo
import be.zvz.kookie.network.AdvancedNetworkInterface
import be.zvz.kookie.network.Network
import be.zvz.kookie.network.mcpe.protocol.ProtocolInfo
import com.koloboke.collect.map.hash.HashObjLongMaps
import com.nukkitx.network.raknet.RakNetServer
import com.nukkitx.network.raknet.RakNetServerListener
import com.nukkitx.network.raknet.RakNetServerSession
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.util.*

class RakLibInterface(private val server: Server, private val address: InetSocketAddress) :
    RakNetServerListener,
    AdvancedNetworkInterface {

    private val blockAddresses: MutableMap<InetSocketAddress, Long> = HashObjLongMaps.newMutableMap()

    private lateinit var network: Network

    private lateinit var raklib: RakNetServer

    override fun blockAddress(address: InetSocketAddress, timeout: Long) {
        blockAddresses.put(address, timeout)
    }

    override fun unblockAddress(address: InetSocketAddress) {
        if (blockAddresses.containsKey(address)) {
            blockAddresses.remove(address)
        }
    }

    override fun setNetwork(network: Network) {
        this.network = network
    }

    override fun sendRawPacket(address: InetSocketAddress, payload: ByteBuf) {
        raklib.send(address, payload)
    }

    override fun addRawPacketFilter(regex: Regex) {
        TODO("Not yet implemented")
    }

    override fun onConnectionRequest(address: InetSocketAddress): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQuery(address: InetSocketAddress): ByteArray {
        val info = server.queryInfo
        val joiner = StringJoiner(";")
            .add("MCPE")
            .add(server.configGroup.getConfigString("motd", "${VersionInfo.NAME} Server"))
            .add(Integer.toString(ProtocolInfo.CURRENT_PROTOCOL))
            .add(ProtocolInfo.MINECRAFT_VERSION_NETWORK)
            .add(Integer.toString(info.getPlayerCount()))
            .add(Integer.toString(info.getMaxPlayerCount()))
            .add(java.lang.Long.toString(raklib.guid))
            .add("Powered by Kookie")
            .add("Survival")
            .add("1")
        return joiner.toString().toByteArray(StandardCharsets.UTF_8)
    }

    override fun onSessionCreation(session: RakNetServerSession) {
        TODO("Not yet implemented")
    }

    override fun onUnhandledDatagram(ctx: ChannelHandlerContext, packet: DatagramPacket) {
        TODO("Not yet implemented")
    }
}
