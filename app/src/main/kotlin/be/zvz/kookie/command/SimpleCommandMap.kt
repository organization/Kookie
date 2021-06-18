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
import be.zvz.kookie.command.utils.InvalidCommandSyntaxException
import com.koloboke.collect.map.hash.HashObjObjMaps
import kotlin.math.min

class SimpleCommandMap : CommandMap {

    var knownCommands: MutableMap<String, Command> = HashObjObjMaps.newMutableMap()

    fun setDefaultCommands() {
        // TODO: register default commands on here
    }

    override fun registerAll(fallbackPrefix: String, commands: List<Command>) {
        commands.forEach {
            register(fallbackPrefix, it)
        }
    }

    override fun register(fallbackPrefix: String, command: Command, label: String?): Boolean {
        var modifiedLabel = label
        if (modifiedLabel === null) {
            modifiedLabel = command.name
        }
        modifiedLabel = modifiedLabel.trim()
        val modifiedFallbackPrefix = fallbackPrefix.trim().lowercase()
        val registered = registerAlias(command, false, modifiedFallbackPrefix, modifiedLabel)

        command.aliases = command.aliases.filter {
            registerAlias(command, true, modifiedFallbackPrefix, it)
        }.toMutableList()

        if (!registered) {
            command.setLabel("$modifiedFallbackPrefix:$modifiedLabel")
        }

        command.register(this)
        return registered
    }

    fun unregister(command: Command) {
        val iterate = knownCommands.iterator()
        while (iterate.hasNext()) {
            val cmd = iterate.next().value
            if (cmd == command) {
                iterate.remove()
            }
        }
        command.unregister(this)
    }

    override fun dispatch(sender: CommandSender, cmdLine: String): Boolean {
        val args = mutableListOf<String>().apply {
            COMMAND_PARSE_REGEX.matchEntire(cmdLine)?.let { match ->
                match.groupValues.forEach {
                    add(it.replace("\\", ""))
                }
            }
        }

        val sentCommandLabel = StringBuilder()
        val target = matchCommand(sentCommandLabel, args)

        if (target === null) {
            return false
        }

        target.timings?.startTiming()

        try {
            target.execute(sender, sentCommandLabel.toString(), args)
        } catch (_: InvalidCommandSyntaxException) {
            sender.sendMessage(sender.language.translateString("commands.generic.usage", listOf(target.usageMessage)))
        } finally {
            target.timings?.stopTiming()
        }

        return true
    }

    override fun clearCommands() {
        knownCommands.forEach { (_, command) ->
            command.unregister(this)
        }
        knownCommands = HashObjObjMaps.newMutableMap()
        setDefaultCommands()
    }

    override fun getCommand(name: String): Command? = knownCommands[name]

    private fun registerAlias(command: Command, isAlias: Boolean, fallbackPrefix: String, label: String): Boolean {
        knownCommands["$fallbackPrefix:$label"] = command
        if ((command is VanillaCommand || isAlias) && knownCommands.containsKey(label)) {
            return false
        }
        val cmd = knownCommands[label]
        if (cmd !== null && cmd.label === label) {
            return false
        }
        if (!isAlias) {
            command.setLabel(label)
        }
        knownCommands[label] = command
        return true
    }

    fun matchCommand(commandName: StringBuilder, args: MutableList<String>): Command? {
        repeat(min(args.size, 255)) {
            commandName.append(args.removeFirst())
            val command = getCommand(commandName.toString())
            if (command !== null) {
                return command
            }
            commandName.append(" ")
        }
        return null
    }

    companion object {
        private val COMMAND_PARSE_REGEX = Regex("\"((?:\\\\\\\\.|[^\\\\\"])*)\"|(\\S+)")
    }
}
