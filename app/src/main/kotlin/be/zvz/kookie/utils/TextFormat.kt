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
package be.zvz.kookie.utils

class TextFormat {
    companion object {
        const val ESCAPE = "§"
        const val EOL = "\n"

        const val BLACK = "${ESCAPE}0"
        const val DARK_BLUE = "${ESCAPE}1"
        const val DARK_GREEN = "${ESCAPE}2"
        const val DARK_AQUA = "${ESCAPE}3"
        const val DARK_RED = "${ESCAPE}4"
        const val DARK_PURPLE = "${ESCAPE}5"
        const val GOLD = "${ESCAPE}6"
        const val GRAY = "${ESCAPE}7"
        const val DARK_GRAY = "${ESCAPE}8"
        const val BLUE = "${ESCAPE}9"
        const val GREEN = "${ESCAPE}a"
        const val AQUA = "${ESCAPE}b"
        const val RED = "${ESCAPE}c"
        const val LIGHT_PURPLE = "${ESCAPE}d"
        const val YELLOW = "${ESCAPE}e"
        const val WHITE = "${ESCAPE}f"

        const val OBFUSCATED = "${ESCAPE}k"
        const val BOLD = "${ESCAPE}l"
        const val STRIKETHROUGH = "${ESCAPE}m"
        const val UNDERLINE = "${ESCAPE}n"
        const val ITALIC = "${ESCAPE}o"
        const val RESET = "${ESCAPE}r"

        @JvmStatic
        fun clean(message: String): String {
            return message.replace(Regex("$ESCAPE[0-9a-fk-or]/u"), "")
        }
    }
}
