package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class PlayerItemHeldEvent(player: Player, val item: Item, hotbarSlot: Int) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
