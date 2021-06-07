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
package be.zvz.kookie.nbt

import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.Tag

data class TreeRoot(
    val tag: Tag<*>,
    val name: String = ""
) {
    fun mustGetCompoundTag(): CompoundTag {
        if (tag is CompoundTag) {
            return tag
        }
        throw NbtDataException("Root is not a TAG_Compound")
    }
}
