package be.zvz.kookie.event.player

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.item.Item
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player

class PlayerInteractEvent(
    player: Player,
    val item: Item,
    val block: Block,
    val touchVector: Vector3 = Vector3(),
    val face: Action = Action.RIGHT_CLICK_BLOCK
) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false

    enum class Action(id: Int) {
        LEFT_CLICK_BLOCK(0),
        RIGHT_CLICK_BLOCK(1)
    }
}
