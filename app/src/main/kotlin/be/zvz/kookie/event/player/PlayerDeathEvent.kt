package be.zvz.kookie.event.player

import be.zvz.kookie.entity.Living
import be.zvz.kookie.event.entity.EntityDeathEvent
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class PlayerDeathEvent(
    player: Living,
    drops: MutableList<Item> = mutableListOf(),
    xp: Int = 0
) : EntityDeathEvent(player, drops, xp) {

    fun getPlayer(): Player = entity as Player
}
