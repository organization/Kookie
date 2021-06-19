package be.zvz.kookie.event.player

import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class PlayerDropItemEvent(player: Player, val item: Item) : PlayerEvent(player)
