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
