package be.zvz.kookie.event.player

import be.zvz.kookie.player.Player

class PlayerCommandPreprocessEvent(player: Player, var message: String) : PlayerEvent(player)
