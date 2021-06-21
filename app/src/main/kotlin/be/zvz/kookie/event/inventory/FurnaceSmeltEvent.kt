package be.zvz.kookie.event.inventory

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.block.BlockEvent
import be.zvz.kookie.item.Item

class FurnaceSmeltEvent(furnace: Block, val source: Item, val result: Item) : BlockEvent(furnace) {
    // TODO
}
