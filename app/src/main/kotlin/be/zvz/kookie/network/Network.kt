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
package be.zvz.kookie.network

import be.zvz.kookie.Server
import be.zvz.kookie.VersionInfo
import be.zvz.kookie.network.mcpe.NetworkSessionManager
import org.slf4j.Logger
import java.net.InetSocketAddress

class Network(private val server: Server, private val logger: Logger) {

    private val interfaces = mutableListOf<NetworkInterface>()

    private val bannedIps = HashMap<InetSocketAddress, Int>()

    private var name = ""

    private val sessionManager = NetworkSessionManager()

    init {
        setName(server.configGroup.getConfigString("motd", "${VersionInfo.NAME} Server"))
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String = name

    fun addInterface(networkInterface: NetworkInterface) {
        interfaces.add(networkInterface)
        if (networkInterface is AdvancedNetworkInterface) {
            networkInterface.start()
        }
    }

    fun removeInterface(networkInterface: NetworkInterface) {
        if (interfaces.remove(networkInterface) && networkInterface is AdvancedNetworkInterface) {
            networkInterface.shutdown()
        }
    }

    fun getSessionManager(): NetworkSessionManager = sessionManager
}
