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
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.nbt.tag.StringTag

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
    var lore = mutableListOf<String>()
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
    fun hasNamedTag(): Boolean = getNamedTag().count() > 0
    fun getNamedTag(): CompoundTag = serializeCompoundTag(nbt)
    fun setNamedTag(tag: CompoundTag): Item = this.apply {
        if (tag.count() == 0) clearNamedTag()
        else {
            nbt = tag.clone()

        }
    }

    fun clearNamedTag(): Item = this.apply {
        nbt = CompoundTag()
        deserializeCompoundTag(nbt)
    }

    fun getMiningEfficiency(isCorrectTool: Boolean): Float = 1F

    protected fun deserializeCompoundTag(tag: CompoundTag) {
    }

    protected fun serializeCompoundTag(tag: CompoundTag): CompoundTag {
        val display = tag.getCompoundTag(TAG_DISPLAY) ?: CompoundTag()

        if (customName.isEmpty()) {
            display.removeTag(TAG_DISPLAY_NAME)
        } else {
            display.setString(TAG_DISPLAY_NAME, customName)
        }

        if (lore.isNotEmpty()) {
            val loreTag = ListTag<String>()
            lore.forEach {
                loreTag.push(StringTag(it))
            }
            display.setTag(TAG_DISPLAY_LORE, loreTag)
        } else {
            display.removeTag(TAG_DISPLAY_LORE)
        }

        if (display.count() > 0) {
            tag.setTag(TAG_DISPLAY, display)
        } else {
            tag.removeTag(TAG_DISPLAY)
        }

        // TODO: enchantment

        getCustomBlockData().let {
            if (it == null) {
                tag.removeTag(TAG_BLOCK_ENTITY_TAG)
            } else {
                tag.setTag(TAG_BLOCK_ENTITY_TAG, it.clone())
            }
        }

        if (this.canPlaceOn.isNotEmpty()) {
            val canPlaceOnTag = ListTag<String>()
            canPlaceOn.forEach {
                canPlaceOnTag.push(StringTag(it))
            }
            tag.setTag("CanPlaceOn", canPlaceOnTag)
        } else {
            tag.removeTag("CanPlaceOn")
        }
        if (this.canDestroy.isNotEmpty()) {
            val canDestroyTag = ListTag<String>()
            canDestroy.forEach {
                canDestroyTag.push(StringTag(it))
            }
            tag.setTag("canDestroy", canDestroyTag)
        } else {
            tag.removeTag("canDestroy")
        }

        return tag
    }

    companion object {
        const val TAG_ENCH = "ench"
        const val TAG_DISPLAY = "display"
        const val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"
        const val TAG_DISPLAY_NAME = "Name"
        const val TAG_DISPLAY_LORE = "Lore"
    }
}
