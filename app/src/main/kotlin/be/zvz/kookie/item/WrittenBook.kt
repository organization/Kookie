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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.item

import be.zvz.kookie.nbt.tag.CompoundTag

class WrittenBook(identifier: ItemIdentifier, name: String) : WritableBookBase(identifier, name) {
    override val maxStackSize: Int = 16
    var generation: Int = GENERATION_ORIGINAL
        set(value) {
            if (value < 0 || value > 3) throw IllegalArgumentException("Generation \"$value\" is out of range")
            field = value
        }
    var author: String = ""
    var title: String = ""

    override fun deserializeCompoundTag(tag: CompoundTag) {
        super.deserializeCompoundTag(tag)
        generation = tag.getInt(TAG_GENERATION, generation)
        author = tag.getString(TAG_AUTHOR, author)
        title = tag.getString(TAG_TITLE, title)
    }

    override fun serializeCompoundTag(tag: CompoundTag) {
        super.serializeCompoundTag(tag)
        tag.setInt(TAG_GENERATION, generation)
        tag.setString(TAG_AUTHOR, author)
        tag.setString(TAG_TITLE, title)
    }

    companion object {
        const val GENERATION_ORIGINAL = 0
        const val GENERATION_COPY = 1
        const val GENERATION_COPY_OF_COPY = 2
        const val GENERATION_TATTERED = 3

        const val TAG_GENERATION = "generation" // TAG_Int
        const val TAG_AUTHOR = "author" // TAG_String
        const val TAG_TITLE = "title" // TAG_String
    }
}
