package be.zvz.kookie.event.player

import be.zvz.kookie.event.Event
import be.zvz.kookie.player.Player

abstract class PlayerEvent(val player: Player) : Event()
