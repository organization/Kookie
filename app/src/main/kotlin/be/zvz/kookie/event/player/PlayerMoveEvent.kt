package be.zvz.kookie.event.player

import be.zvz.kookie.entity.Location
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerMoveEvent(player: Player, val from: Location, var to: Location) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
