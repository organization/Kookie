package be.zvz.kookie.item

import be.zvz.kookie.nbt.tag.CompoundTag

open class Item(
    private val identifier: ItemIdentifier,
    protected val name: String = "Unknown",
) {
    val TAG_ENCH = "ench"
    val TAG_DISPLAY = "display"
    val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"

    val TAG_DISPLAY_NAME = "Name"
    val TAG_DISPLAY_LORE = "Lore"

    private var nbt: CompoundTag? = CompoundTag()
    protected var count: Int = 1
    protected var customName: String = ""
    protected val lore: MutableList<String> = mutableListOf()
    protected val blockEntityTag: CompoundTag? = null
    protected val canPlaceOn: MutableList<String> = mutableListOf()
    protected val canDestroy: MutableList<String> = mutableListOf()
}
