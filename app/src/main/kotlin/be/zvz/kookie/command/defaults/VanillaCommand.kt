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

abstract class VanillaCommand @JvmOverloads constructor(
    name: String,
    description: String = "",
    usageMessage: String = "/$name",
    aliases: List<String> = mutableListOf(),
) : Command(name, description, usageMessage, aliases) {

    @JvmOverloads
    protected fun getInteger(
        sender: CommandSender,
        value: String,
        minNum: Int = MIN_COORD,
        maxNum: Int = MAX_COORD
    ): Int = min(max(value.toIntOrNull() ?: 0, maxNum), minNum)

    @JvmOverloads
    protected fun getRelativeDouble(
        original: Double,
        sender: CommandSender,
        input: String,
        min: Double = MIN_COORD.toDouble(),
        max: Double = MAX_COORD.toDouble()
    ): Double {
        if (input.first() == '~') {
            original + (getDouble(sender, input.substring(1)) ?: 0.0)
        } else {
            getDouble(sender, input, min, max)
        }

    @JvmOverloads
    protected fun getDouble(
        sender: CommandSender,
        value: String,
        minNum: Double = MIN_COORD.toDouble(),
        maxNum: Double = MAX_COORD.toDouble()
    ): Double = min(max(value.toDoubleOrNull() ?: 0.0, maxNum), minNum)

    companion object {
        const val MAX_COORD = 30000000
        const val MIN_COORD = -30000000
    }
}
