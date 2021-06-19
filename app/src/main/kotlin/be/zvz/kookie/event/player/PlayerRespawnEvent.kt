package be.zvz.kookie.event.player

import be.zvz.kookie.player.Player
import be.zvz.kookie.world.Position

class PlayerRespawnEvent(player: Player, var respawnPosition: Position) : PlayerEvent(player)
