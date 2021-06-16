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

import be.zvz.kookie.command.defaults.VanillaCommand
import com.koloboke.collect.map.hash.HashObjObjMaps

class SimpleCommandMap : CommandMap {

    val knownCommands: MutableMap<String, Command> = HashObjObjMaps.newMutableMap()

    override fun registerAll(fallbackPrefix: String, commands: List<Command>) {
        TODO("Not yet implemented")
    }

    override fun register(fallbackPrefix: String, command: Command, label: String?): Boolean {
        var modifiedLabel = label
        if (modifiedLabel == null) {
            modifiedLabel = command.name
        }
        modifiedLabel = modifiedLabel.trim()
        val modifiedFallbackPrefix = fallbackPrefix.trim().lowercase()
        val registered = registerAlias(command, false, modifiedFallbackPrefix, modifiedLabel)

        command.aliases = command.aliases.filter { registerAlias(command, true, modifiedFallbackPrefix, it) }.toMutableList()

        if (!registered) {
            command.setLabel("$modifiedFallbackPrefix:$modifiedLabel")
        }

        command.register(this)
        return registered
    }

    override fun dispatch(sender: CommandSender, cmdLine: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun clearCommands() {
        TODO("Not yet implemented")
    }

    override fun getCommand(name: String): Command? {
        TODO("Not yet implemented")
    }

    private fun registerAlias(command: Command, isAlias: Boolean, fallbackPrefix: String, label: String): Boolean {
        knownCommands["$fallbackPrefix:$label"] = command
        if ((command is VanillaCommand || isAlias) && knownCommands.containsKey(label)) {
            return false
        }
        val cmd = knownCommands.getOrDefault(label, null)
        if (cmd != null && cmd.label == label) {
            return false
        }
        if (!isAlias) {
            command.setLabel(label)
        }
        knownCommands[label] = command
        return true
    }
}
