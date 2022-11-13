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
package be.zvz.kookie.event.player

import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.player.Player

class PlayerQuitEvent(player: Player, quitMessage: Any) : PlayerEvent(player) {
    var quitMessage: Any = quitMessage
        set(value) {
            if (value !is TranslationContainer || value !is String) {
                throw IllegalArgumentException("Property `quitMessage` only accepts TranslationContainer and String")
            }
            field = value
        }
}
