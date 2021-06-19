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
package be.zvz.kookie.network.mcpe.protocol.serializer

import be.zvz.kookie.nbt.BaseNbtSerializer

class NetworkNbtSerializer : BaseNbtSerializer() {

    override fun readShort(): Int = buffer.getLShort()
    override fun readSignedShort(): Int = buffer.getSignedLShort()
    override fun writeShort(v: Int) {
        buffer.putLShort(v)
    }

    override fun readInt(): Int = buffer.getVarInt()
    override fun writeInt(v: Int) {
        buffer.putVarInt(v)
    }

    override fun readLong(): Long = buffer.getVarLong()
    override fun writeLong(v: Long) {
        buffer.putVarLong(v)
    }

    override fun readString(): String = buffer.get(checkReadStringLength(buffer.getUnsignedVarInt()))
    override fun writeString(v: String) {
        buffer.putUnsignedVarInt(checkWriteStringLength(v.length))
        buffer.put(v)
    }

    override fun readFloat(): Float = buffer.getLFloat()
    override fun writeFloat(v: Float) {
        buffer.putLFloat(v)
    }

    override fun readDouble(): Double = buffer.getLDouble()
    override fun writeDouble(v: Double) {
        buffer.putLDouble(v)
    }

    override fun readIntArray(): IntArray {
        val list = mutableListOf<Int>()
        repeat(readInt()) { list.add(readInt()) }

        return list.toIntArray()
    }

    override fun writeIntArray(v: IntArray) {
        writeInt(v.size) // varInt
        v.forEach(this::writeInt)
    }

    override fun readLongArray(): LongArray {
        val list = mutableListOf<Long>()
        repeat(readInt()) { list.add(readLong()) }

        return list.toLongArray()
    }

    override fun writeLongArray(v: LongArray) {
        writeInt(v.size) // varInt
        v.forEach(this::writeLong)
    }
}
