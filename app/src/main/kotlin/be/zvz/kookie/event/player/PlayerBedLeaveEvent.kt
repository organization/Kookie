package be.zvz.kookie.event.player

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerBedLeaveEvent(player: Player, val bed: Block) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
