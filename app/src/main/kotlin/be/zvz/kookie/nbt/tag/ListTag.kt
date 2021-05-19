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
package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.NBTException

class ListTag<T>(override val value: MutableList<Tag<T>> = mutableListOf()) : Tag<List<Tag<T>>>() {

    private var tagType: NBT.TagType = NBT.TagType.NOTHING

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.LIST
    }

    init {
        for (tag in value) {
            if (tagType === NBT.TagType.NOTHING) {
                tagType = tag.getTagType()
            } else {
                if (tagType != tag.getTagType()) {
                    throw NBTException("Expected TagType ${tagType.name}, got ${tag.getTagType().name}")
                }
            }
        }
    }

    fun push(tag: Tag<T>) {
        if (tag.getTagType() !== tagType) {
            throw NBTException("Expected TagType ${tagType.name}, got ${tag.getTagType().name}")
        }
        value.add(tag)
    }

    fun get(index: Int): Tag<T>? = value.getOrNull(index)

    override fun makeCopy(): ListTag<T> = ListTag(
        mutableListOf<Tag<T>>().apply {
            value.forEach {
                add(it)
            }
        }
    )
}
