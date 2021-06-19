package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block

class BlockGrowEvent(block: Block, newState: Block) : BaseBlockChangeEvent(block, newState)
