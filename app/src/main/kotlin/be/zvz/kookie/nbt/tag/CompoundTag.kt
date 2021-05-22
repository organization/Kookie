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

    fun count(): Int = value.size

    @JvmOverloads
    fun getTag(name: String, default: Tag<*>? = null): Tag<*>? {
        return value[name] ?: default
    }

    fun getCompoundTag(name: String): CompoundTag? {
        val tag = getTag(name) ?: return null
        if (tag !is CompoundTag) {
            throw NBTException("Found tag $name but tag is not CompoundTag")
        }
        return tag
    }

    fun getListTag(name: String): ListTag<*>? {
        val tag = getTag(name) ?: return null
        if (tag !is ListTag<*>) {
            throw NBTException("Found tag $name but tag is not ListTag")
        }
        return tag
    }

    fun setTag(name: String, tag: Tag<*>) {
        value[name] = tag
    }

    fun removeTag(vararg names: String) {
        names.forEach {
            value.remove(it)
        }
    }

    @JvmOverloads
    fun getTagValue(name: String, expectedClass: String, default: Any? = null): Any {
        val tag = getTag(name)
        if (tag?.javaClass?.name == expectedClass) {
            return tag.value!!
        }
        if (tag != null) {
            throw NBTException("Expected a tag of type $expectedClass, got " + tag.javaClass.name)
        }
        if (default == null) {
            throw NBTException("Tag \"$name\" does not exist");
        }
        return default
    }

    fun getByte(name: String, default: String?): Byte = getTagValue(name, ByteTag::javaClass.name, default) as Byte
    fun getShort(name: String, default: String?): Short = getTagValue(name, ShortTag::javaClass.name, default) as Short
    fun getInt(name: String, default: String?): Int = getTagValue(name, IntTag::javaClass.name, default) as Int
    fun getLong(name: String, default: String?): Long = getTagValue(name, LongTag::javaClass.name, default) as Long
    fun getFloat(name: String, default: String?): Float = getTagValue(name, FloatTag::javaClass.name, default) as Float
    fun getDouble(name: String, default: String?): Double = getTagValue(name, DoubleTag::javaClass.name, default) as Double
    fun getString(name: String, default: String?): String = getTagValue(name, StringTag::javaClass.name, default) as String

    fun setString(name: String, value: String) {
        setTag(name, StringTag(value))
    }

    fun setByte(name: String, value: Byte) {
        setTag(name, ByteTag(value))
    }

    fun setShort(name: String, value: Short) {
        setTag(name, ShortTag(value))
    }

    fun setInt(name: String, value: Int) {
        setTag(name, IntTag(value))
    }

    fun setLong(name: String, value: Long) {
        setTag(name, LongTag(value))
    }

    fun setFloat(name: String, value: Float) {
        setTag(name, FloatTag(value))
    }

    fun setDouble(name: String, value: Double) {
        setTag(name, DoubleTag(value))
    }

    companion object {
        @JvmStatic
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
