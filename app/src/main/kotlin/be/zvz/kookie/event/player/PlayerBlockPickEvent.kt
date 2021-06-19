package be.zvz.kookie.event.player

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class PlayerBlockPickEvent(player: Player, val blockClicked: Block, val resultItem: Item) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
