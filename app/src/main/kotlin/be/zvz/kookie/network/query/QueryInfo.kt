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
package be.zvz.kookie.network.query

import be.zvz.kookie.Server

class QueryInfo(private val server: Server) {

    private var maxPlayers: Int = 0
    private var players = mapOf<Any, Any>()
    private var plugins = mapOf<Any, Any>()

    fun getPlayerCount(): Int = players.size

    fun getMaxPlayerCount(): Int = maxPlayers
}
