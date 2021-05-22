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

import be.zvz.kookie.nbt.*
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
            throw NbtException("Found tag $name but tag is not CompoundTag")
        }
        return tag
    }

    fun getListTag(name: String): ListTag<*>? {
        val tag = getTag(name) ?: return null
        if (tag !is ListTag<*>) {
            throw NbtException("Found tag $name but tag is not ListTag")
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
            throw NbtException("Expected a tag of type $expectedClass, got " + tag.javaClass.name)
        }
        if (default == null) {
            throw NbtException("Tag \"$name\" does not exist")
        }
        return default
    }

    fun getByte(name: String, default: Int?): Int = getTagValue(name, ByteTag::javaClass.name, default) as Int
    fun getShort(name: String, default: Int?): Int = getTagValue(name, ShortTag::javaClass.name, default) as Int
    fun getInt(name: String, default: Int?): Int = getTagValue(name, IntTag::javaClass.name, default) as Int
    fun getLong(name: String, default: Long?): Long = getTagValue(name, LongTag::javaClass.name, default) as Long
    fun getFloat(name: String, default: Float?): Float = getTagValue(name, FloatTag::javaClass.name, default) as Float
    fun getDouble(name: String, default: Double?): Double = getTagValue(name, DoubleTag::javaClass.name, default) as Double
    fun getString(name: String, default: String?): String = getTagValue(name, StringTag::javaClass.name, default) as String

    fun setString(name: String, value: String): CompoundTag = this.apply {
        setTag(name, StringTag(value))
    }

    fun setByte(name: String, value: Int): CompoundTag = this.apply {
        setTag(name, ByteTag(value))
    }

    fun setShort(name: String, value: Int): CompoundTag = this.apply {
        setTag(name, ShortTag(value))
    }

    fun setInt(name: String, value: Int): CompoundTag = this.apply {
        setTag(name, IntTag(value))
    }

    fun setLong(name: String, value: Long): CompoundTag = this.apply {
        setTag(name, LongTag(value))
    }

    fun setFloat(name: String, value: Float): CompoundTag = this.apply {
        setTag(name, FloatTag(value))
    }

    fun setDouble(name: String, value: Double): CompoundTag = this.apply {
        setTag(name, DoubleTag(value))
    }

    companion object {
        @JvmStatic
        fun create(): CompoundTag {
            return CompoundTag()
        }

        fun read(reader: NbtStreamReader, tracker: ReaderTracker): CompoundTag {
            val result = CompoundTag()
            tracker.protectDepth {
                var type = reader.readByte()
                while (type != NBT.TagType.NOTHING.value) {
                    val name = reader.readString()
                    val tag = NBT.createTag(NBT.TagType.getByValue(type), reader, tracker)
                    if (result.getTag(name) !== null) {
                        throw NbtDataException("Duplicate key \"$name\"")
                    }
                    result.setTag(name, tag)
                    type = reader.readByte()
                }
            }
            return result
        }
    }

    override fun write(writer: NbtStreamWriter) {
        value.forEach { (name, tag) ->
            writer.writeByte(tag.getTagType().value)
            writer.writeString(name)
            tag.write(writer)
        }
        writer.writeByte(NBT.TagType.NOTHING.value)
    }

    override fun makeCopy(): CompoundTag = CompoundTag().let {
        it.value.forEach { (name, tag) ->
            value[name] = tag
        }
        it
    }
}
