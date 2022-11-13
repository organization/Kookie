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
package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.NbtStreamWriter

abstract class Tag<T> {
    var cloning = false
        private set
    abstract val value: T

    abstract fun getTagType(): NBT.TagType

    abstract fun write(writer: NbtStreamWriter)

    override fun equals(other: Any?): Boolean =
        other is Tag<*> && this.value == other.value

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

    fun clone(): Tag<T> {
        if (cloning) {
            throw RuntimeException("Recursive NBT tag dependency detected")
        }
        cloning = true

        val clonedTag = makeCopy()
        this.cloning = false
        clonedTag.cloning = false
        return clonedTag
    }

    protected abstract fun makeCopy(): Tag<T>
}
