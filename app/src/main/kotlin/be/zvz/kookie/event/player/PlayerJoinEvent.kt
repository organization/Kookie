package be.zvz.kookie.event.player

import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.player.Player

class PlayerJoinEvent(player: Player, joinMessage: Any) : PlayerEvent(player) {
    var joinMessage: Any = joinMessage
        set(value) {
            if (value !is TranslationContainer || value !is String) {
                throw IllegalArgumentException("Property `joinMessage` only accepts TranslationContainer and String")
            }
            field = value
        }
}
