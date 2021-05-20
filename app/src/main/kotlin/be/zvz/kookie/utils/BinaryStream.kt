package be.zvz.kookie.utils

import java.util.concurrent.atomic.AtomicInteger

open class BinaryStream(buffer: String = "", offset: Int = 0) {
    val offset = AtomicInteger(offset)
    val buffer = StringBuilder(buffer)

    fun rewind() {
        offset.set(0)
    }

    fun get(len: Int): String {
        if (len == 0) {
            return ""
        }
        if (len < 0) {
            throw IllegalArgumentException("Length must be positive")
        }
        val remaining = buffer.length - offset.get()
        if (remaining < len) {
            throw BinaryDataException("Not enough bytes left in buffer: need $len, have $remaining")
        }

        return if (len == 1) {
            buffer[offset.getAndIncrement()].toString()
        } else {
            offset.getAndAdd(len)
            buffer.substring(offset.get() - len, len)
        }
    }

    fun getRemaining(): String {
        val bufferLength = buffer.length
        val offsetInt = offset.get()
        if (offsetInt >= bufferLength) {
            throw BinaryDataException("No bytes left to read")
        }
        val str = buffer.substring(offsetInt)
        offset.set(bufferLength)
        return str
    }

    fun put(str: String) {
        buffer.append(str)
    }

    fun getBoolean(): Boolean = get(1) == "\u0000"

    fun putBoolean(v: Boolean) {
        buffer.append(
            if (v) {
                "\u0001"
            } else {
                "\u0000"
            }
        )
    }

    fun getByte(): Int = get(1)[0].code

    fun putByte(v: Int) {
        buffer.append(v)
    }

    fun getShort(): Int = Binary.readShort(get(2))

    fun getSignedShort(): Int = Binary.readSignedShort(get(2))

    fun putShort(v: Int) {
        buffer.append(Binary.writeShort(v))
    }

    fun getLShort(): Int = Binary.readLShort(get(2))

    fun getSignedLShort(): Int = Binary.readSignedLShort(get(2))

    fun putLShort(v: Int) {
        buffer.append(Binary.writeLShort(v))
    }

    fun getTriad(): Int = Binary.readTriad(get(3))

    fun putTriad(v: Int) {
        buffer.append(Binary.writeTriad(v))
    }

    fun getLTriad(): Int = Binary.readLTriad(get(3))

    fun putLTriad(v: Int) {
        buffer.append(Binary.writeLTriad(v))
    }

    fun getInt(): Int = Binary.readInt(get(4))

    fun putInt(v: Int) {
        buffer.append(Binary.writeInt(v))
    }

    fun getLInt(): Int = Binary.readLInt(get(4))

    fun putLInt(v: Int) {
        buffer.append(Binary.writeLInt(v))
    }

    fun getFloat(): Float = Binary.readFloat(get(4))

    fun getRoundedFloat(): Float = Binary.readRoundedFloat(get(4))

    fun putFloat(v: Float) {
        buffer.append(Binary.writeFloat(v))
    }

    fun getLFloat(): Float = Binary.readLFloat(get(4))

    fun getRoundedLFloat(): Float = Binary.readRoundedLFloat(get(4))

    fun putLFloat(v: Float) {
        buffer.append(Binary.writeLFloat(v))
    }

    fun getDouble(): Double = Binary.readDouble(get(8))

    fun putDouble(v: Double) {
        buffer.append(Binary.writeDouble(v))
    }

    fun getLDouble(): Double = Binary.readLDouble(get(8))

    fun putLDouble(v: Double) {
        buffer.append(Binary.writeLDouble(v))
    }

    fun getLong(): Long = Binary.readLong(get(8))

    fun putLong(v: Long) {
        buffer.append(Binary.writeLong(v))
    }

    fun getLLong(): Long = Binary.readLLong(get(8))

    fun putLLong(v: Long) {
        buffer.append(Binary.writeLLong(v))
    }

    fun getUnsignedVarInt(): Int = Binary.readUnsignedVarInt(buffer.toString(), offset)

    fun putUnsignedVarInt(v: Int) {
        put(Binary.writeUnsignedVarInt(v))
    }

    fun getVarInt(): Int = Binary.readVarInt(buffer.toString(), offset)

    fun putVarInt(v: Int) {
        put(Binary.writeVarInt(v))
    }

    fun getUnsignedVarLong(): Long = Binary.readUnsignedVarLong(buffer.toString(), offset.get())

    fun putUnsignedVarLong(v: Long) {
        buffer.append(Binary.writeUnsignedVarLong(v))
    }

    fun getVarLong(): Long = Binary.readVarLong(buffer.toString(), offset.get())

    fun putVarLong(v: Long) {
        buffer.append(Binary.writeVarLong(v))
    }

    fun feof(): Boolean = buffer.getOrNull(offset.get()) === null
}
