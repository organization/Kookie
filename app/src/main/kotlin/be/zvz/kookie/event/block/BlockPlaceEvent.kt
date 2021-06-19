package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class BlockPlaceEvent(
    val player: Player,
    blockPlace: Block,
    val blockReplace: Block,
    val blockAgainst: Block,
    val item: Item
) : BlockEvent(blockPlace)
