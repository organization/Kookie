package be.zvz.kookie.event.inventory

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.event.block.BlockEvent
import be.zvz.kookie.item.Item

// TODO: Furnace should be tile
class FurnaceBurnEvent(furnace: Block, val fuel: Item, burnTime: Int) : BlockEvent(furnace) {
    // TODO
    override val handlers: HandlerList
        get() = handlerList

    companion object {
        private val handlerList = HandlerList(FurnaceBurnEvent::class.java)
    }
}
