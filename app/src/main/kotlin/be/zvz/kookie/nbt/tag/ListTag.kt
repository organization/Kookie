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
import be.zvz.kookie.nbt.NbtException
import be.zvz.kookie.nbt.NbtStreamReader
import be.zvz.kookie.nbt.NbtStreamWriter

class ListTag<T> @JvmOverloads constructor(
    value: List<Tag<T>> = listOf(),
    private var tagType: NBT.TagType = NBT.TagType.NOTHING
) : Tag<List<Tag<T>>>() {
    private val list: MutableList<Tag<T>> = value.toMutableList()

    override val value: List<Tag<T>>
        get() = list

    override fun getTagType(): NBT.TagType {
        return tagType
    }

    init {
        value.forEach { tag ->
            if (tagType === NBT.TagType.NOTHING) {
                tagType = tag.getTagType()
            } else {
                if (tagType != tag.getTagType()) {
                    throw NbtException("Expected TagType ${tagType.name}, got ${tag.getTagType().name}")
                }
            }
        }
    }

    fun push(tag: Tag<T>) {
        if (tag.getTagType() !== tagType) {
            throw NbtException("Expected TagType ${tagType.name}, got ${tag.getTagType().name}")
        }
        list.add(tag)
    }

    fun get(index: Int): Tag<T>? = value.getOrNull(index)

    override fun makeCopy(): ListTag<T> = ListTag(
        mutableListOf<Tag<T>>().apply {
            list.forEach {
                add(it)
            }
        }
    )

    override fun write(writer: NbtStreamWriter) {
        writer.writeByte(tagType.value)
        writer.writeInt(list.size)
        list.forEach { tag ->
            tag.write(writer)
        }
    }

    companion object {
        @JvmStatic
        fun read(reader: NbtStreamReader, tracker: ReaderTracker): ListTag<*> {
            var tagType = NBT.TagType.from(reader.readByte())
            val value = mutableListOf<Tag<*>>()
            val size = reader.readInt()

            if (size > 0) {
                if (tagType == NBT.TagType.NOTHING) {
                    throw IllegalArgumentException("Unexpected non-empty list of TagType.NOTHING")
                }
                tracker.protectDepth {
                    for (i in 0 until size) {
                        value.add(NBT.createTag(tagType, reader, tracker))
                    }
                }
            } else {
                tagType = NBT.TagType.NOTHING
            }
            return ListTag(value.filterIsInstance<Tag<Any>>(), tagType)
        }
    }
}
