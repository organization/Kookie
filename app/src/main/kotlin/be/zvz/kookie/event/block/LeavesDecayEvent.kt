package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable

class LeavesDecayEvent(block: Block) : BlockEvent(block), Cancellable {
    override var isCancelled: Boolean = false
}
