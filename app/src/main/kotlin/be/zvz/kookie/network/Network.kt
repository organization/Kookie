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
