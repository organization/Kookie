package be.zvz.kookie.item

import be.zvz.kookie.block.BlockToolType
import be.zvz.kookie.nbt.tag.CompoundTag

open class Item(
    private val identifier: ItemIdentifier,
    protected val name: String = "Unknown",
) {
    private var nbt: CompoundTag = CompoundTag()
    protected var count: Int = 1
    protected var customName: String = ""
    protected val lore = mutableListOf<String>()
    protected val blockEntityTag: CompoundTag? = null
    protected val canPlaceOn = mutableListOf<String>()
    protected val canDestroy = mutableListOf<String>()
    open val blockToolType = BlockToolType.NONE
    open val blockToolHarvestLevel = 0

    fun getMiningEfficiency(isCorrectTool: Boolean): Float = 1F

    companion object {
        const val TAG_ENCH = "ench"
        const val TAG_DISPLAY = "display"
        const val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"
        const val TAG_DISPLAY_NAME = "Name"
        const val TAG_DISPLAY_LORE = "Lore"
    }
}
