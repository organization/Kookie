package be.zvz.kookie.event.block

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.Event

abstract class BlockEvent(val block: Block) : Event()
