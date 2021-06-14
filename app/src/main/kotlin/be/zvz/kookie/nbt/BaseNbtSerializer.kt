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

import be.zvz.kookie.nbt.tag.ReaderTracker
import be.zvz.kookie.utils.Binary
import be.zvz.kookie.utils.BinaryDataException
import be.zvz.kookie.utils.BinaryStream
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseNbtSerializer : NbtStreamReader, NbtStreamWriter {
    protected var buffer: BinaryStream = BinaryStream()

    private fun readRoot(maxDepth: Int): TreeRoot {
        val type = NBT.TagType.from(readByte())
        if (type === NBT.TagType.NOTHING) {
            throw NbtDataException("Found TAG_End at the start of buffer")
        }
        val rootName = readString()
        return TreeRoot(
            NBT.createTag(
                type,
                this,
                ReaderTracker(maxDepth)
            ),
            rootName
        )
    }

    @JvmOverloads
    fun read(
        buffer: String,
        offset: AtomicInteger = AtomicInteger(0),
        maxDepth: Int = 0
    ): TreeRoot {
        this.buffer = BinaryStream(buffer, offset)
        val data = try {
            readRoot(maxDepth)
        } catch (e: BinaryDataException) {
            throw NbtDataException(e.message, e)
        }
        offset.set(this.buffer.offset.get())
        return data
    }

    @JvmOverloads
    fun readMultiple(bufferStr: String, maxDepth: Int = 0): List<TreeRoot> {
        this.buffer = BinaryStream(bufferStr)

        return mutableListOf<TreeRoot>().apply {
            while (!buffer.feof()) {
                try {
                    add(readRoot(maxDepth))
                } catch (e: BinaryDataException) {
                    throw NbtDataException(e.message, e)
                }
            }
        }
    }

    private fun writeRoot(root: TreeRoot) {
        writeByte(root.tag.getTagType().value)
        writeString(root.name)
        root.tag.write(this)
    }

    fun write(data: TreeRoot): String {
        buffer = BinaryStream()
        writeRoot(data)
        return buffer.buffer.toString()
    }

    fun writeMultiple(data: List<TreeRoot>): String {
        buffer = BinaryStream()
        data.forEach { root ->
            writeRoot(root)
        }
        return buffer.buffer.toString()
    }

    override fun readByte(): Int = buffer.getByte()
    override fun readSignedByte(): Int = Binary.signByte(buffer.getByte())
    override fun readString(): String = buffer.get(checkReadStringLength(readShort()))
    override fun readByteArray(): ByteArray = buffer.get(readInt()).toByteArray()
    override fun writeByte(v: Int) = buffer.putByte(v)
    override fun writeString(v: String) {
        writeShort(checkWriteStringLength(v.length))
        buffer.put(v)
    }

    override fun writeByteArray(v: ByteArray) {
        writeInt(v.size)
        buffer.put(v)
    }

    companion object {
        @JvmStatic
        protected fun checkReadStringLength(len: Int): Int {
            if (len > 32767) {
                throw NbtDataException("NBT string length too large ($len > 32767)")
            }
            return len
        }

        @JvmStatic
        protected fun checkWriteStringLength(len: Int): Int {
            if (len > 32767) {
                throw IllegalArgumentException("NBT string length too large ($len > 32767)")
            }
            return len
        }
    }
}
