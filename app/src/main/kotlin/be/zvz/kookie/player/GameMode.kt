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

enum class GameMode(protected val magicNumber: Int, val modeName: String, val translationKey: String, val aliases: Array<String>) : IGameMode {
    SURVIVAL(0, "Survival", "gameMode.survival", arrayOf("s", "0")) {
        override fun id(): Int = this.magicNumber
    },
    CREATIVE(1, "Creative", "gameMode.creative", arrayOf("c", "1")) {
        override fun id(): Int = this.magicNumber
    },
    ADVENTURE(2, "Adventure", "%gameMode.adventure", arrayOf("a", "2")) {
        override fun id(): Int = this.magicNumber
    },
    SPECTATOR(3, "Spectator", "%gameMode.spectator", arrayOf("v", "view", "3")) {
        override fun id(): Int = this.magicNumber
    };

    companion object {
        fun from(findValue: Int): GameMode = values().firstOrNull { it.magicNumber == findValue } ?: SURVIVAL
    }
}
