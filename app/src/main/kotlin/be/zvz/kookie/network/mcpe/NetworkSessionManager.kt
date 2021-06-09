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
package be.zvz.kookie.network.mcpe

class NetworkSessionManager {

    private val sessions = mutableListOf<NetworkSession>()

    private val updateSessions = mutableListOf<NetworkSession>()

    fun add(session: NetworkSession) {
        sessions.add(session)
    }

    fun remove(session: NetworkSession) {
        sessions.remove(session)
    }

    fun scheduleUpdate(session: NetworkSession) {
        updateSessions.add(session)
    }

    fun getSessionsCount(): Int {
        return sessions.size
    }

    fun tick() {
        val iterate = updateSessions.iterator()
        while (iterate.hasNext()) {
            if (!iterate.next().tick()) {
                iterate.remove()
            }
        }
    }

    fun close(reason: String) {
        sessions.forEach {
            it.disconnect(reason)
        }
        sessions.clear()
        updateSessions.clear()
    }
}
