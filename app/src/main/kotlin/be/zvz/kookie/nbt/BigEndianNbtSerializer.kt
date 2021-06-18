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

import be.zvz.kookie.utils.Binary
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class BigEndianNbtSerializer : BaseNbtSerializer() {
    override fun readShort(): Int = buffer.getShort()
    override fun readSignedShort(): Int = buffer.getSignedShort()
    override fun readInt(): Int = buffer.getInt()
    override fun readLong(): Long = buffer.getLong()
    override fun readFloat(): Float = buffer.getFloat()
    override fun readDouble(): Double = buffer.getDouble()

    private fun unpackNStar(value: String, byteSize: Int): ByteBuffer {
        val bytes = value.toByteArray()
        val buf = ByteBuffer.allocate(byteSize)
        buf.order(ByteOrder.BIG_ENDIAN)
        buf.put(bytes)
        buf.flip()
        return buf
    }

    private fun packNStar(value: IntArray): String {
        val byteBuffer = ByteBuffer.allocate(Int.SIZE_BYTES).apply {
            order(ByteOrder.BIG_ENDIAN)
        }
        value.forEach(byteBuffer::putInt)
        return Binary.toPositiveByteArray(byteBuffer.array()).toString(StandardCharsets.UTF_8)
    }

    private fun packNStar(value: LongArray): String {
        val byteBuffer = ByteBuffer.allocate(Long.SIZE_BYTES).apply {
            order(ByteOrder.BIG_ENDIAN)
        }
        value.forEach(byteBuffer::putLong)
        return Binary.toPositiveByteArray(byteBuffer.array()).toString(StandardCharsets.UTF_8)
    }

    override fun readIntArray(): IntArray {
        val len = readInt()
        val unpacked = unpackNStar(buffer.get(len * Int.SIZE_BYTES), Int.SIZE_BYTES)
        return unpacked.asIntBuffer().array()
    }

    override fun readLongArray(): LongArray {
        val len = readInt()
        val unpacked = unpackNStar(buffer.get(len * Long.SIZE_BYTES), Long.SIZE_BYTES)
        return unpacked.asLongBuffer().array()
    }

    override fun writeShort(v: Int) {
        buffer.putShort(v)
    }

    override fun writeInt(v: Int) {
        buffer.putInt(v)
    }

    override fun writeLong(v: Long) {
        buffer.putLong(v)
    }

    override fun writeFloat(v: Float) {
        buffer.putFloat(v)
    }

    override fun writeDouble(v: Double) {
        buffer.putDouble(v)
    }

    override fun writeIntArray(v: IntArray) {
        writeInt(v.size)
        buffer.put(packNStar(v))
    }

    override fun writeLongArray(v: LongArray) {
        writeInt(v.size)
        buffer.put(packNStar(v))
    }
}
