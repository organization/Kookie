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
package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.Event
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.player.PlayerInfo
import com.koloboke.collect.map.hash.HashObjObjMaps

class PlayerPreLoginEvent(val info: PlayerInfo, val ip: String, val port: Int, var isAuthRequired: Boolean) :
    Event(),
    Cancellable {
    override var isCancelled: Boolean = false
        get() {
            return !isAllowed()
        }
    override val handlers: HandlerList
        get() = handlerList

    var kickReasons: MutableMap<Reason, String> = HashObjObjMaps.newMutableMap()

    fun isKickReasonSet(flag: Reason): Boolean = kickReasons.containsKey(flag)

    fun setKickReason(flag: Reason, message: String) {
        kickReasons[flag] = message
    }

    fun clearKickReason(flag: Reason) {
        if (kickReasons.containsKey(flag)) {
            kickReasons.remove(flag)
        }
    }

    fun clearKickReasons() {
        kickReasons = HashObjObjMaps.newMutableMap()
    }

    fun getKickMessage(flag: Reason): String? = kickReasons[flag]

    fun getFinalKickMessage(): String {
        REASON_PRIORITY.forEach {
            if (kickReasons.containsKey(it)) {
                return kickReasons.getValue(it)
            }
        }
        return ""
    }

    fun isAllowed(): Boolean = kickReasons.isNotEmpty()

    enum class Reason(reason: Int) {
        PLUGIN(0),
        SERVER_FULL(1),
        SERVER_WHITELISTED(2),
        BANNED(3)
    }

    companion object {
        val REASON_PRIORITY: List<Reason> = listOf(
            Reason.PLUGIN,
            Reason.SERVER_FULL,
            Reason.SERVER_WHITELISTED,
            Reason.BANNED
        )
        private val handlerList = HandlerList(PlayerPreLoginEvent::class.java)
    }
}
