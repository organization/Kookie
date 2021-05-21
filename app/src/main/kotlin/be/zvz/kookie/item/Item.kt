/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.item

import be.zvz.kookie.block.BlockToolType
import be.zvz.kookie.nbt.tag.CompoundTag

open class Item(
    private val identifier: ItemIdentifier,
    protected val name: String = "Unknown",
) {
    private var nbt: CompoundTag = CompoundTag()
    protected var count: Int = 1
    var customName: String = ""
        set(v) {
            if (v != "") {
                field = v
            }
        }
    protected var lore = mutableListOf<String>()
    protected var blockEntityTag: CompoundTag? = null
    protected var canPlaceOn = mutableListOf<String>()
    protected var canDestroy = mutableListOf<String>()
    open val blockToolType = BlockToolType.NONE
    open val blockToolHarvestLevel = 0

    fun hasCustomBlockData(): Boolean = this.blockEntityTag != null
    fun clearCustomBlockData(): Item = this.apply {
        blockEntityTag = null
    }
    fun getCustomBlockData(): CompoundTag? = this.blockEntityTag
    fun getMiningEfficiency(isCorrectTool: Boolean): Float = 1F

    companion object {
        const val TAG_ENCH = "ench"
        const val TAG_DISPLAY = "display"
        const val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"
        const val TAG_DISPLAY_NAME = "Name"
        const val TAG_DISPLAY_LORE = "Lore"
    }
}
