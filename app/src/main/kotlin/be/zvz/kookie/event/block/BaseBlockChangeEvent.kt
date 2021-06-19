package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Cancellable

open class BaseBlockChangeEvent(block: Block, var newState: Block) : BlockEvent(block), Cancellable {
    override var isCancelled: Boolean = false
}
