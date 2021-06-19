package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.player.Player

class PlayerDataSaveEvent(player: Player, val nbt: CompoundTag) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
