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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.command.defaults

import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.command.ConsoleCommandSender
import be.zvz.kookie.command.utils.InvalidCommandSyntaxException
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.player.Player
import be.zvz.kookie.utils.TextFormat
import be.zvz.kookie.utils.Union

class SayCommand(name: String) : VanillaCommand(
    name,
    "%pocketmine.command.say.description",
    "%commands.say.usage",
) {
    init {
        permission = "pocketmine.command.say"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: List<String>): Boolean {
        if (testPermission(sender)) {
            return true
        }

        if (args.isEmpty()) {
            throw InvalidCommandSyntaxException()
        }

        sender.server.broadcastMessage(
            TranslationContainer(
                "${TextFormat.LIGHT_PURPLE}%chat.type.announcement",
                listOf(
                    Union.U3.ofA(
                        when (sender) {
                            is Player -> sender.displayName
                            is ConsoleCommandSender -> "Server"
                            else -> sender.name
                        }
                    ),
                    Union.U3.ofA(
                        "${TextFormat.LIGHT_PURPLE}${args.joinToString(" ")}"
                    )
                )
            )
        )

        return true
    }
}
