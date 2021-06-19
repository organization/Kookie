package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.player.Player

class PlayerKickEvent(player: Player, var reason: String, quitMessage: Any) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
    var quitMessage: Any = quitMessage
        set(value) {
            if (value !is TranslationContainer || value !is String) {
                throw IllegalArgumentException("Property `quitMessage` only accepts TranslationContainer and String")
            }
            field = value
        }
}
