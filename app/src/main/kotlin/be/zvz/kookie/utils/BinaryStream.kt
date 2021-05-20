package be.zvz.kookie.utils

class BinaryStream(buffer: String = "", var offset: Int = 0) {
    val buffer = StringBuilder(buffer)

    fun rewind() {
        offset = 0
    }

    fun get(len: Int): String {
        if (len == 0) {
            return ""
        }
        if (len < 0) {
            throw IllegalArgumentException("Length must be positive")
        }
        val remaining = buffer.length - offset
        if (remaining < len) {
            throw BinaryDataException("Not enough bytes left in buffer: need $len, have $remaining")
        }

        return if (len == 1) {
            buffer[offset++].toString()
        } else {
            offset += len
            buffer.substring(offset - len, len)
        }
    }

    fun getRemaining(): String {
        val bufferLength = buffer.length
        if (offset >= bufferLength) {
            throw BinaryDataException("No bytes left to read")
        }
        val str = buffer.substring(offset)
        offset = bufferLength
        return str
    }

    fun put(str: String) {
        buffer.append(str)
    }

    fun getBoolean(): Boolean {
        return get(1) == "\u0000"
    }

    fun putBoolean(v: Boolean) {
        buffer.append(
            if (v) {
                "\u0001"
            } else {
                "\u0000"
            }
        )
    }

    fun getByte(): Int {
        return get(1)[0].code
    }

    fun putByte(v: Int) {
        buffer.append(v)
    }

    fun getShort(): Int {
        return Binary.readShort(get(2))
    }

    fun getSignedShort(): Int {
        return Binary.readSignedShort(get(2))
    }

    fun putShort(v: Int) {
        buffer.append(Binary.writeShort(v))
    }

    fun getLShort(): Int {
        return Binary.readLShort(get(2))
    }

    fun getSignedLShort(): Int {
        return Binary.readSignedLShort(get(2))
    }

    fun putLShort(v: Int) {
        buffer.append(Binary.writeLShort(v))
    }

    fun getTriad(): Int {
        return Binary.readTriad(get(3))
    }

    fun putTriad(v: Int) {
        buffer.append(Binary.writeTriad(v))
    }

    fun getLTriad(): Int {
        return Binary.readLTriad(get(3))
    }

    fun putLTriad(v: Int) {
        buffer.append(Binary.writeLTriad(v))
    }

    fun getInt(): Int {
        return Binary.readInt(get(4))
    }

    fun putInt(v: Int) {
        buffer.append(Binary.writeInt(v))
    }

    fun getLInt(): Int {
        return Binary.readLInt(get(4))
    }

    fun putLInt(v: Int) {
        buffer.append(Binary.writeLInt(v))
    }

    fun getFloat(): Float {
        return Binary.readFloat(get(4))
    }

    fun getRoundedFloat(): Float {
        return Binary.readRoundedFloat(get(4))
    }

    fun putFloat(v: Float) {
        buffer.append(Binary.writeFloat(v))
    }

    fun getLFloat(): Float {
        return Binary.readLFloat(get(4))
    }

    fun getRoundedLFloat(): Float {
        return Binary.readRoundedLFloat(get(4))
    }

    fun putLFloat(v: Float) {
        buffer.append(Binary.writeLFloat(v))
    }

    fun getDouble(): Double {
        return Binary.readDouble(get(8))
    }

    fun putDouble(v: Double) {
        buffer.append(Binary.writeDouble(v))
    }

    fun getLDouble(): Double {
        return Binary.readLDouble(get(8))
    }

    fun putLDouble(v: Double) {
        buffer.append(Binary.writeLDouble(v))
    }

    fun getLong(): Long {
        return Binary.readLong(get(8))
    }

    fun putLong(v: Long) {
        buffer.append(Binary.writeLong(v))
    }

    fun getLLong(): Long {
        return Binary.readLLong(get(8))
    }

    fun putLLong(v: Long) {
        buffer.append(Binary.writeLLong(v))
    }

    fun getUnsignedVarInt(): Int {
        return Binary.readUnsignedVarInt(buffer.toString(), offset)
    }

    fun getUnsignedVarInt(v: Int) {
        put(Binary.writeUnsignedVarInt(v))
    }

    fun getVarInt(): Int {
        return Binary.readVarInt(buffer.toString(), offset)
    }

    fun putVarInt(v: Int) {
        put(Binary.writeVarInt(v))
    }

    fun getUnsignedVarLong(): Long {
        return Binary.readUnsignedVarLong(buffer.toString(), offset)
    }

    fun putUnsignedVarLong(v: Long) {
        buffer.append(Binary.writeUnsignedVarLong(v))
    }

    fun getVarLong(): Long {
        return Binary.readVarLong(buffer.toString(), offset)
    }

    fun putVarLong(v: Long) {
        buffer.append(Binary.writeVarLong(v))
    }

    fun feof(): Boolean {
        return buffer.getOrNull(offset) === null
    }
}
