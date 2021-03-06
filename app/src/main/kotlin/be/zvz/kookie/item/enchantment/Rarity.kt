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
package be.zvz.kookie.item.enchantment

enum class Rarity(val rarity: Int) {
    COMMON(10),
    UNCOMMON(5),
    RARE(2),
    MYTHIC(1);

    companion object {
        private val VALUES = values()

        @JvmStatic
        fun from(findValue: Int): Rarity = VALUES.first { it.rarity == findValue }
    }
}
