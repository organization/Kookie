package be.zvz.kookie.event.player

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

open class PlayerBucketEvent(
    player: Player,
    val blockClicked: Block,
    val blockFace: Int,
    val bucket: Item,
    val itemInHand: Item
) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false
}
