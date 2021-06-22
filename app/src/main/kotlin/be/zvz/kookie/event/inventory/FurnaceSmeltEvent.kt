package be.zvz.kookie.event.inventory

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.event.block.BlockEvent
import be.zvz.kookie.item.Item

class FurnaceSmeltEvent(furnace: Block, val source: Item, val result: Item) : BlockEvent(furnace) {
    // TODO
    override val handlers: HandlerList
        get() = handlerList

    companion object {
        private val handlerList = HandlerList(FurnaceSmeltEvent::class.java)
    }
}
