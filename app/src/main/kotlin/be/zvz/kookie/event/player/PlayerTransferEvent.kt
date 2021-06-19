package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerTransferEvent(player: Player, var address: String, var port: Int) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
