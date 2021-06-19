package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class PlayerItemConsumeEvent(player: Player, item: Item) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
    var item: Item = item
        private set
        get() {
            return field.clone()
        }
}
