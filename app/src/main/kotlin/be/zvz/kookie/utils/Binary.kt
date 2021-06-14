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
    @JvmStatic fun signByte(value: Int): Int = value shl 56 shr 56
    @JvmStatic fun unsignByte(value: Int): Int = value and 0xff
    @JvmStatic fun signShort(value: Int): Int = value shl 48 shr 48
    @JvmStatic fun unsignShort(value: Int): Int = value and 0xffff
    @JvmStatic fun signInt(value: Int): Int = value shl 32 shr 32
    @JvmStatic fun unsignInt(value: Int): Int = value and -1

    @JvmStatic fun flipShortEndianness(value: Int): Int = readLShort(writeShort(value))
    @JvmStatic fun flipIntEndianness(value: Int): Int = readLInt(writeInt(value))
    @JvmStatic fun flipLongEndianness(value: Long): Long = readLLong(writeLong(value))

    @JvmStatic fun readBoolean(b: String): Boolean = b != "\u0000"
    @JvmStatic fun writeBoolean(b: Boolean): String = if (b) {
        "\u0001"
    } else {
        "\u0000"
    }

    @JvmStatic fun readByte(c: String): Int = c[0].code
    @JvmStatic fun readSignedByte(c: String): Int = signByte(c[0].code)
    @JvmStatic fun writeByte(c: Int): String = c.toChar().toString()
    @JvmStatic fun readShort(str: String): Int {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readUnsignedShort()
            }
        }
    }

    @JvmStatic fun readSignedShort(str: String): Int = signShort(readShort(str))
    @JvmStatic fun writeShort(value: Int): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeShort(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readLShort(str: String): Int {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readUnsignedShort()
            }
        }
    }

    @JvmStatic fun readSignedLShort(str: String): Int = signShort(readLShort(str))
    @JvmStatic fun writeLShort(value: Int): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeShort(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun toPositiveByteArray(bytes: ByteArray): ByteArray = bytes.map {
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

    @JvmStatic fun readTriad(str: String): Int = unpackN("\u0000$str", 1)
    @JvmStatic fun writeTriad(value: Int): String = packN(value).substring(1)
    @JvmStatic fun readLTriad(str: String): Int = unpackV("$str\u0000", 1)
    @JvmStatic fun writeLTriad(value: Int): String = packV(value).dropLast(1)
    @JvmStatic fun readInt(str: String): Int = signInt(unpackN(str, 1))
    @JvmStatic fun writeInt(value: Int): String = packN(value)
    @JvmStatic fun readLInt(str: String): Int = unpackV(str, 1)
    @JvmStatic fun writeLInt(value: Int): String = packV(value)
    @JvmStatic fun readFloat(str: String): Float {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readFloat()
            }
        }
    }

    @JvmStatic fun readRoundedFloat(str: String): Float = round(readFloat(str))
    @JvmStatic fun writeFloat(value: Float): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeFloat(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readLFloat(str: String): Float {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readFloat()
            }
        }
    }

    @JvmStatic fun writeLFloat(value: Float): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeFloat(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readRoundedLFloat(str: String): Float = round(readLFloat(str))

    @JvmStatic fun readDouble(str: String): Double {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readDouble()
            }
        }
    }

    @JvmStatic fun writeDouble(value: Double): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeDouble(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readLDouble(str: String): Double {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readDouble()
            }
        }
    }

    @JvmStatic fun writeLDouble(value: Double): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeDouble(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readLong(str: String): Long {
        str.byteInputStream().use { bais ->
            DataInputStream(bais).use {
                return it.readLong()
            }
        }
    }

    @JvmStatic fun writeLong(value: Long): String {
        ByteArrayOutputStream().use { baos ->
            DataOutputStream(baos).use {
                it.writeLong(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readLLong(str: String): Long {
        str.byteInputStream().use { bais ->
            LittleEndianDataInputStream(bais).use {
                return it.readLong()
            }
        }
    }

    @JvmStatic fun writeLLong(value: Long): String {
        ByteArrayOutputStream().use { baos ->
            LittleEndianDataOutputStream(baos).use {
                it.writeLong(value)
            }
            return baos.toString()
        }
    }

    @JvmStatic fun readVarInt(buffer: String, offset: AtomicInteger): Int {
        val raw = readUnsignedVarInt(buffer, offset)
        val temp = raw shl 63 shr 63 xor raw shr 1
        return temp xor (raw and (1 shl 63))
    }

    @JvmStatic fun readUnsignedVarInt(buffer: String, offset: AtomicInteger): Int {
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

    @JvmStatic fun writeVarInt(v: Int): String {
        val x = v shl 32 shr 32
        return writeUnsignedVarInt((x shl 1) xor (x shr 31))
    }

    @JvmStatic fun writeUnsignedVarInt(value: Int): String {
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

    @JvmStatic fun readVarLong(buffer: String, offset: Int): Long {
        val raw = readUnsignedVarLong(buffer, offset)
        val temp = raw shl 63 shr 63 xor raw shr 1
        return temp xor (raw and (1 shl 63))
    }

    @JvmStatic fun readUnsignedVarLong(buffer: String, offset: Int): Long {
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

    @JvmStatic fun writeVarLong(v: Long): String {
        return writeUnsignedVarLong((v shl 1) xor (v shr 63))
    }

    @JvmStatic fun writeUnsignedVarLong(value: Long): String {
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
