package be.zvz.kookie.event.player

import be.zvz.kookie.player.Player

class PlayerDisplayNameChangeEvent(player: Player, val oldName: String, val newName: String) : PlayerEvent(player)
