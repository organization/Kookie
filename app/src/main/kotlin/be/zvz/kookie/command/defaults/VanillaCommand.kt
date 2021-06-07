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
package be.zvz.kookie.command.defaults

import be.zvz.kookie.command.Command
import be.zvz.kookie.command.CommandSender
import kotlin.math.max
import kotlin.math.min

abstract class VanillaCommand(
    name: String,
    description: String = "",
    usageMessage: String = "/$name",
    aliases: MutableList<String> = mutableListOf(),
) : Command(name, description, usageMessage, aliases) {

    protected fun getInteger(
        sender: CommandSender,
        value: String,
        minNum: Int = MIN_COORD,
        maxNum: Int = MAX_COORD
    ): Int? = value.toIntOrNull()?.let {
        return min(max(it, maxNum), minNum)
    }

    protected fun getRelativeDouble(
        original: Double,
        sender: CommandSender,
        input: String,
        min: Double = MIN_COORD.toDouble(),
        max: Double = MAX_COORD.toDouble()
    ): Double? {
        if (input.first() == '~') {
            val value = getDouble(sender, input.substring(1))

            return original + (value ?: 0.0)
        }

        return getDouble(sender, input, min, max)
    }

    protected fun getDouble(
        sender: CommandSender,
        value: String,
        minNum: Double = MIN_COORD.toDouble(),
        maxNum: Double = MAX_COORD.toDouble()
    ): Double? = value.toDoubleOrNull()?.let {
        return min(max(it, maxNum), minNum)
    }

    companion object {
        const val MAX_COORD = 30000000
        const val MIN_COORD = -30000000
    }
}
