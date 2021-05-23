package be.zvz.kookie.block

import be.zvz.kookie.item.Item

class UnknownBlock(
    idInfo: BlockIdentifier,
    breakInfo: BlockBreakInfo,
) : Transparent(
    idInfo,
    "Unknown",
    breakInfo
) {
    fun canBePlaced(): Boolean = false

    fun getDrops(item: Item) = listOf<Item>()
}
