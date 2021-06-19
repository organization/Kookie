package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerLoginEvent(player: Player, var kickMessage: String) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
