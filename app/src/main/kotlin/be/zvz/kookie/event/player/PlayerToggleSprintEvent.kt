package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerToggleSprintEvent(player: Player, val isSprinting: Boolean) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
