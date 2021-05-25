package be.zvz.kookie.item

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory

class ItemBlock(identifier: ItemIdentifier, val block: Block) : Item(identifier, block.name) {
    private val blockFullId: Int = block.getFullId()

    override fun getBlock(clickedFace: Int?): Block = BlockFactory.fromFullBlock(blockFullId)

    // TODO Fuel Time
}
