package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class BlockBreakEvent(
    val player: Player,
    block: Block,
    val item: Item,
    val instaBreak: Boolean = false,
    var drops: MutableList<Item> = mutableListOf(),
    var xpDropAmount: Int = 0
) : BlockEvent(block), Cancellable {
    override var isCancelled: Boolean = false
}
