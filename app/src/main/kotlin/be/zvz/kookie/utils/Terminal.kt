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
package be.zvz.kookie.utils

object Terminal {

    var enableANSI = false

    const val RESET = "\u001b[0m"

    const val BLACK = "\u001b[0;30m"

    const val RED = "\u001b[0;31m"

    const val GREEN = "\u001b[0;32m"

    const val YELLOW = "\u001b[0;33m"

    const val BLUE = "\u001b[0;34m"

    const val PURPLE = "\u001b[0;35m"

    const val CYAN = "\u001b[0;36m"

    const val WHITE = "\u001b[0;37m"

    @JvmStatic
    @JvmOverloads
    fun init(ansi: Boolean = true) {
        enableANSI = ansi
    }

    @JvmStatic
    fun toANSI(message: String): String {
        val regex = "§[0-9a-fgoi]"
        return message
    }
}
