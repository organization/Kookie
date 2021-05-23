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

import be.zvz.kookie.nbt.tag.*

class NBT {

    enum class TagType(val value: Int) {
        BYTE(1),
        SHORT(2),
        INT(3),
        LONG(4),
        FLOAT(5),
        DOUBLE(6),
        BYTE_ARRAY(7),
        STRING(8),
        LIST(9),
        COMPOUND(10),
        INT_ARRAY(11),
        LONG_ARRAY(12),
        NOTHING(0);

        companion object {
            private val VALUES = values()
            fun from(value: Int) = VALUES.firstOrNull { it.value == value } ?: NOTHING
        }
    }

    companion object {
        fun createTag(
            type: TagType,
            reader: NbtStreamReader,
            tracker: ReaderTracker
        ): Tag<*> {
            return when (type) {
                TagType.BYTE -> ByteTag.read(reader)
                TagType.SHORT -> ShortTag.read(reader)
                TagType.INT -> IntTag.read(reader)
                TagType.LONG -> LongTag.read(reader)
                TagType.FLOAT -> FloatTag.read(reader)
                TagType.DOUBLE -> DoubleTag.read(reader)
                TagType.BYTE_ARRAY -> ByteArrayTag.read(reader)
                TagType.STRING -> StringTag.read(reader)
                TagType.LIST -> ListTag.read(reader, tracker)
                TagType.COMPOUND -> CompoundTag.read(reader, tracker)
                TagType.INT_ARRAY -> IntArrayTag.read(reader)
                TagType.LONG_ARRAY -> LongArrayTag.read(reader)
                TagType.NOTHING -> throw NbtDataException("Unknown NBT tag type $type")
            }
        }
    }
}
