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
package be.zvz.kookie.command

import be.zvz.kookie.permission.PermissionManager
import be.zvz.kookie.timings.TimingsHandler
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.utils.TextFormat

abstract class Command(
    val name: String,
    val description: String = "",
    val usageMessage: String = "/$name",
    val aliases: MutableList<String> = mutableListOf(),
) {
    var permission: String = ""
        set(value) {
            value.split(";").forEach {
                if (PermissionManager.getPermission(it) == null) {
                    throw IllegalArgumentException("Cannot use non-existing permission \"$it\"")
                }
            }

            field = value
        }

    var permissionMessage: String = ""

    private var nextLabel: String = ""
    var label: String = ""
        private set

    private var timings: TimingsHandler? = null
    private val activeAliases: MutableList<String> = mutableListOf()
    private var commandMap: CommandMap? = null

    fun testPermission(target: CommandSender): Boolean {
        if(testPermissionSilent(target)) {
            return true
        }

        target.sendMessage(
            when(permissionMessage) {
                "" -> target.language.translateString("${TextFormat.RED}%commands.generic.permission")
                else -> permissionMessage.replace("<permission>", permission)
            }
        )

        return false
    }

    fun testPermissionSilent(target: CommandSender): Boolean {
        if (permission.trim() != "") {
            permission.split(";").forEach {
                if (target.hasPermission(it)) {
                    return true
                }
            }
        } else {
            return true
        }

        return false
    }

    fun setLabel(name: String): Boolean {
        nextLabel = name
        if (!isRegistered()) {
            timings = TimingsHandler("${Timings.INCLUDED_BY_OTHER_TIMINGS_PREFIX}Command: $name")
            label = name

            return true
        }

        return false
    }

    fun register(newCommandMap: CommandMap): Boolean {
        if (allowChangesFrom(newCommandMap)) {
            commandMap = newCommandMap

            return true
        }

        return false
    }

    fun unregister(newCommandMap: CommandMap): Boolean {
        if (allowChangesFrom(newCommandMap)){
            commandMap = null

            activeAliases.clear()
            aliases.forEachIndexed { index: Int, s: String ->
                activeAliases[index] = s
            }

            label = nextLabel

            return true
        }

        return false
    }

    private fun allowChangesFrom(newCommandMap: CommandMap): Boolean = commandMap == null || commandMap == newCommandMap

    fun isRegistered(): Boolean = commandMap != null

    abstract fun execute(sender: CommandSender, args: List<String>)

    companion object {
        fun broadcastMessage(translationKey: String, params: MutableList<String>): Int {
            return -1 // TODO
        }
    }
}
