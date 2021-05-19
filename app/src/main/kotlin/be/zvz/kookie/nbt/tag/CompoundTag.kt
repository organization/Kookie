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
import com.koloboke.collect.map.hash.HashObjObjMaps

class CompoundTag : Tag<Map<String, Tag<*>>>() {
    override val value = HashObjObjMaps.newMutableMap<String, Tag<*>>()

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.COMPOUND
    }

    fun setTag(name: String, tag: Tag<*>) {
        value[name] = tag
    }

    fun getTag(name: String, default: Tag<*>? = null): Tag<*>? {
        return value[name] ?: default
    }

    fun getTagValue(name: String, default: Any? = null): Any? {
        return value[name]?.value ?: default
    }

    fun setString(name: String, value: String) {
        setTag(name, StringTag(value))
    }

    fun setByte(name: String, value: Byte) {
        setTag(name, ByteTag(value))
    }

    fun setFloat(name: String, value: Float) {
        setTag(name, FloatTag(value))
    }

    fun setDouble(name: String, value: Double) {
        setTag(name, DoubleTag(value))
    }

    fun setInt(name: String, value: Int) {
        setTag(name, IntTag(value))
    }

    fun setLong(name: String, value: Long) {
        setTag(name, LongTag(value))
    }

    fun setShort(name: String, value: Short) {
        setTag(name, ShortTag(value))
    }

    fun setCompound(name: String, value: CompoundTag) {
        setTag(name, value)
    }

    fun getString(name: String): String {
        val tag = getTag(name)
        if (tag !is StringTag) {
            throw NBTException("Found tag $name but tag is not StringTag")
        }
        return tag.value
    }

    fun getByte(name: String): Byte {
        val tag = getTag(name)
        if (tag !is ByteTag) {
            throw NBTException("Found tag $name but tag is not ByteTag")
        }
        return tag.value
    }

    fun getFloat(name: String): Float {
        val tag = getTag(name)
        if (tag !is FloatTag) {
            throw NBTException("Found tag $name but tag is not FloatTag")
        }
        return tag.value
    }

    fun getDouble(name: String): Double {
        val tag = getTag(name)
        if (tag !is DoubleTag) {
            throw NBTException("Found tag $name but tag is not DoubleTag")
        }
        return tag.value
    }

    fun getInt(name: String): Int {
        val tag = getTag(name)
        if (tag !is IntTag) {
            throw NBTException("Found tag $name but tag is not IntTag")
        }
        return tag.value
    }

    fun getLong(name: String): Long {
        val tag = getTag(name)
        if (tag !is LongTag) {
            throw NBTException("Found tag $name but tag is not LongTag")
        }
        return tag.value
    }

    fun getShort(name: String): Short {
        val tag = getTag(name)
        if (tag !is ShortTag) {
            throw NBTException("Found tag $name but tag is not ShortTag")
        }
        return tag.value
    }

    fun getCompoundTag(name: String): CompoundTag {
        val tag = getTag(name)
        if (tag !is CompoundTag) {
            throw NBTException("Found tag $name but tag is not CompoundTag")
        }
        return tag
    }

    fun getListTag(name: String): ListTag<*> {
        val tag = getTag(name)
        if (tag !is ListTag<*>) {
            throw NBTException("Found tag $name but tag is not ListTag")
        }
        return tag
    }

    companion object {
        fun create(): CompoundTag {
            return CompoundTag()
        }
    }

    override fun makeCopy(): CompoundTag = CompoundTag().let {
        it.value.forEach { (name, tag) ->
            value[name] = tag
        }
        it
    }
}
