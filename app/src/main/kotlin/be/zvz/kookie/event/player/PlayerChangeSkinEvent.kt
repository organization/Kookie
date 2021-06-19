package be.zvz.kookie.event.player

import be.zvz.kookie.entity.Skin
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

class PlayerChangeSkinEvent(player: Player, val oldSkin: Skin, var newSkin: Skin) : PlayerEvent(player) , Cancellable {
    override var isCancelled: Boolean = false
}
