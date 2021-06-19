package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block

class BlockSpreadEvent(block: Block, val source: Block) : BlockEvent(block)
