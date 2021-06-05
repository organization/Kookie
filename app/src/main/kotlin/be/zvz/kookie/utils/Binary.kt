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
package be.zvz.kookie.utils

import com.google.common.io.LittleEndianDataInputStream
import com.google.common.io.LittleEndianDataOutputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.round

object Binary {
    fun signByte(value: Int): Int = value shl 56 shr 56
    fun unsignByte(value: Int): Int = value and 0xff
    fun signShort(value: Int): Int = value shl 48 shr 48
    fun unsignShort(value: Int): Int = value and 0xffff
    fun signInt(value: Int): Int = value shl 32 shr 32
    fun unsignInt(value: Int): Int = value and -1

    fun flipShortEndianness(value: Int): Int = readLShort(writeShort(value))
    fun flipIntEndianness(value: Int): Int = readLInt(writeInt(value))
    fun flipLongEndianness(value: Long): Long = readLLong(writeLong(value))

    fun readBoolean(b: String): Boolean = b != "\u0000"
    fun writeBoolean(b: Boolean): String = if (b) {
        "\u0001"
    } else {
        "\u0000"
    }
    fun readByte(c: String): Int = c[0].code
    fun readSignedByte(c: String): Int = signByte(c[0].code)
    fun writeByte(c: Int): String = c.toChar().toString()
    fun readShort(str: String): Int {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readUnsignedShort()
            }
        }
    }
    fun readSignedShort(str: String): Int = signShort(readShort(str))
    fun writeShort(value: Int): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeShort(value)
            }
            return baos.toString()
        }
    }
    fun readLShort(str: String): Int {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readUnsignedShort()
            }
        }
    }
    fun readSignedLShort(str: String): Int = signShort(readLShort(str))
    fun writeLShort(value: Int): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeShort(value)
            }
            return baos.toString()
        }
    }

    fun toPositiveByteArray(bytes: ByteArray): ByteArray = bytes.map {
        if (it < 0) {
            (256 + it).toByte()
        } else {
            it
        }
    }.toByteArray()

    private fun packN(value: Int): String = toPositiveByteArray(
        ByteBuffer.allocate(Int.SIZE_BYTES).putInt(value).array()
    ).toString(StandardCharsets.UTF_8)

    private fun unpackN(value: String, index: Int): Int {
        val bytes = value.toByteArray()
        val buf = ByteBuffer.allocate(Int.SIZE_BYTES)
        buf.order(ByteOrder.BIG_ENDIAN)
        buf.put(bytes)
        buf.flip()
        return buf.getInt(index)
    }

    private fun packV(value: Int): String {
        val bytes = toPositiveByteArray(
            ByteBuffer.allocate(Int.SIZE_BYTES).apply {
                order(ByteOrder.LITTLE_ENDIAN)
                putInt(value)
            }.array()
        )
        return bytes.toString(StandardCharsets.UTF_8)
    }

    private fun unpackV(value: String, index: Int): Int {
        val bytes = value.toByteArray()
        val buf = ByteBuffer.allocate(Int.SIZE_BYTES)
        buf.order(ByteOrder.LITTLE_ENDIAN)
        buf.put(bytes)
        buf.flip()
        return buf.getInt(index)
    }

    fun readTriad(str: String): Int = unpackN("\u0000" + str, 1)
    fun writeTriad(value: Int): String = packN(value).substring(1)
    fun readLTriad(str: String): Int = unpackV(str + "\u0000", 1)
    fun writeLTriad(value: Int): String = packV(value).dropLast(1)
    fun readInt(str: String): Int = signInt(unpackN(str, 1))
    fun writeInt(value: Int): String = packN(value)
    fun readLInt(str: String): Int = unpackV(str, 1)
    fun writeLInt(value: Int): String = packV(value)
    fun readFloat(str: String): Float {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readFloat()
            }
        }
    }

    fun readRoundedFloat(str: String): Float = round(readFloat(str))

    fun writeFloat(value: Float): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeFloat(value)
            }
            return baos.toString()
        }
    }

    fun readLFloat(str: String): Float {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readFloat()
            }
        }
    }

    fun readRoundedLFloat(str: String): Float = round(readLFloat(str))

    fun writeLFloat(value: Float): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeFloat(value)
            }
            return baos.toString()
        }
    }

    fun readDouble(str: String): Double {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readDouble()
            }
        }
    }

    fun writeDouble(value: Double): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeDouble(value)
            }
            return baos.toString()
        }
    }

    fun readLDouble(str: String): Double {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readDouble()
            }
        }
    }

    fun writeLDouble(value: Double): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeDouble(value)
            }
            return baos.toString()
        }
    }

    fun readLong(str: String): Long {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readLong()
            }
        }
    }

    fun writeLong(value: Long): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeLong(value)
            }
            return baos.toString()
        }
    }

    fun readLLong(str: String): Long {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readLong()
            }
        }
    }

    fun writeLLong(value: Long): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeLong(value)
            }
            return baos.toString()
        }
    }

    fun readVarInt(buffer: String, offset: AtomicInteger): Int {
        val raw = readUnsignedVarInt(buffer, offset)
        val temp = raw shl 63 shr 63 xor raw shr 1
        return temp xor (raw and (1 shl 63))
    }

    fun readUnsignedVarInt(buffer: String, offset: AtomicInteger): Int {
        var value = 0
        var i = 0
        while (i <= 28) {
            if (buffer.getOrNull(offset.get()) === null) {
                throw BinaryDataException("No bytes left in buffer")
            }
            val b = buffer[offset.getAndIncrement()].code
            value = value or (b and 0x7f shl i)

            if (b and 0x80 == 0) {
                return value
            }
            i += 7
        }

        throw BinaryDataException("VarInt did not terminate after 5 bytes!")
    }

    fun writeVarInt(v: Int): String {
        val x = v shl 32 shr 32
        return writeUnsignedVarInt((x shl 1) xor (x shr 31))
    }

    fun writeUnsignedVarInt(value: Int): String {
        val buf = StringBuilder()
        var x = value and -1
        for (i in 0 until 5) {
            if ((x shr 7) != 0) {
                buf.append(x and 0x80)
            } else {
                buf.append(x and 0x7f)
                return buf.toString()
            }

            x = (x shr 7) and (Int.MAX_VALUE shr 6)
        }

        throw IllegalArgumentException("Value too large to be encoded as a VarInt")
    }

    fun readVarLong(buffer: String, offset: Int): Long {
        val raw = readUnsignedVarLong(buffer, offset)
        val temp = raw shl 63 shr 63 xor raw shr 1
        return temp xor (raw and (1 shl 63))
    }

    fun readUnsignedVarLong(buffer: String, offset: Int): Long {
        var value = 0L
        var i = 0
        while (i <= 63) {
            if (buffer.getOrNull(offset) === null) {
                throw BinaryDataException("No bytes left in buffer")
            }
            val b = buffer[offset].code.toLong()
            offset.inc()
            value = value or (b and 0x7f shl i)

            if (b and 0x80 == 0L) {
                return value
            }
            i += 7
        }

        throw BinaryDataException("VarInt did not terminate after 5 bytes!")
    }

    fun writeVarLong(v: Long): String {
        return writeUnsignedVarLong((v shl 1) xor (v shr 63))
    }

    fun writeUnsignedVarLong(value: Long): String {
        val buf = StringBuilder()
        var x = value and -1
        for (i in 0 until 10) {
            if ((x shr 7) != 0L) {
                buf.append(x and 0x80)
            } else {
                buf.append(x and 0x7f)
                return buf.toString()
            }

            x = (x shr 7) and (Long.MAX_VALUE shr 6)
        }

        throw IllegalArgumentException("Value too large to be encoded as a VarLong")
    }
}
