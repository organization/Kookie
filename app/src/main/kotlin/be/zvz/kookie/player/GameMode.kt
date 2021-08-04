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
package be.zvz.kookie.player

import be.zvz.kookie.lang.KnownTranslationKeys

enum class GameMode(
    protected val magicNumber: Int,
    val modeName: String,
    val translationKey: KnownTranslationKeys,
    val aliases: Array<String>
) : IGameMode {
    SURVIVAL(0, "Survival", KnownTranslationKeys.GAMEMODE_SURVIVAL, arrayOf("s", "0")),
    CREATIVE(1, "Creative", KnownTranslationKeys.GAMEMODE_CREATIVE, arrayOf("c", "1")),
    ADVENTURE(2, "Adventure", KnownTranslationKeys.GAMEMODE_ADVENTURE, arrayOf("a", "2")),
    SPECTATOR(3, "Spectator", KnownTranslationKeys.GAMEMODE_SPECTATOR, arrayOf("v", "view", "3"));

    override fun id(): Int = this.magicNumber

    companion object {
        @JvmStatic
        fun from(findValue: Int): GameMode = values().firstOrNull { it.magicNumber == findValue } ?: SURVIVAL
    }
}
