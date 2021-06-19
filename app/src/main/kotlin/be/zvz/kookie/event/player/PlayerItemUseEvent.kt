package be.zvz.kookie.event.player

import be.zvz.kookie.item.Item
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player

class PlayerItemUseEvent(player: Player, val item: Item, val directionVector: Vector3) : PlayerEvent(player) {
}
