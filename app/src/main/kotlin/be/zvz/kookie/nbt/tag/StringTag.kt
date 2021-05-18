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

class StringTag(private val value: String) : Tag() {

    init {
        if (value.length > 32767) {
            throw NBTException("StringTag cannot hold more than 32767 bytes, got " + value.length)
        }
    }

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.STRING
    }

    override fun getValue(): String {
        return value
    }
}
