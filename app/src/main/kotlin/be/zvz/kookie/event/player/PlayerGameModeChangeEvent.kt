package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.GameMode
import be.zvz.kookie.player.Player

class PlayerGameModeChangeEvent(player: Player, val newGameMode: GameMode) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
