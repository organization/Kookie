package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerToggleFlightEvent(player: Player, val isFlying: Boolean) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
