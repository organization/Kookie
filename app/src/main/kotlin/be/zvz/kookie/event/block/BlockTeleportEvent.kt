package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.math.Vector3

class BlockTeleportEvent(block: Block, var to: Vector3) : BlockEvent(block), Cancellable {
    override var isCancelled: Boolean = false
}
