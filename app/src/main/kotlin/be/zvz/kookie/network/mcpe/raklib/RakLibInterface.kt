/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.raklib

import be.zvz.kookie.Server
import be.zvz.kookie.VersionInfo
import be.zvz.kookie.network.AdvancedNetworkInterface
import be.zvz.kookie.network.Network
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.network.mcpe.NetworkSessionManager
import be.zvz.kookie.network.mcpe.protocol.ProtocolInfo
import com.koloboke.collect.map.hash.HashObjLongMaps
import com.nukkitx.network.raknet.RakNetServer
import com.nukkitx.network.raknet.RakNetServerListener
import com.nukkitx.network.raknet.RakNetServerSession
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.*

class RakLibInterface(private val server: Server, private val sessionManager: NetworkSessionManager, private val address: InetSocketAddress) :
    RakNetServerListener,
    AdvancedNetworkInterface {
    private val logger = LoggerFactory.getLogger(RakLibInterface::class.java)

    private val blockAddresses: MutableMap<InetSocketAddress, Long> = HashObjLongMaps.newMutableMap()

    private lateinit var network: Network

    private val raklib: RakNetServer = RakNetServer(address)

    override fun start() {
        logger.debug("Waiting for RakLib to start")
        raklib.bind()
        logger.debug("RakLib booted successfully")
    }

    override fun setName(name: String) {
        network.setName(name)
    }

    override fun tick() {
        if (!raklib.isRunning) {
            throw RuntimeException("RakLib thread crashed")
        }
    }

    override fun shutdown() {
        raklib.close()
    }

    override fun blockAddress(address: InetSocketAddress, timeout: Long) {
        blockAddresses[address] = timeout
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
        return true
    }

    override fun onQuery(address: InetSocketAddress): ByteArray {
        val info = server.queryInfo
        return StringJoiner(";")
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
            .toString().toByteArray()
    }

    override fun onSessionCreation(session: RakNetServerSession) {
        val networkSession = NetworkSession(server, session)
        sessionManager.add(networkSession)
    }

    override fun onUnhandledDatagram(ctx: ChannelHandlerContext, packet: DatagramPacket) {
    }
}
