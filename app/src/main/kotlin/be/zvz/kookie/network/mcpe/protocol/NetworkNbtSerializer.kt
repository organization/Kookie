package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.nbt.BaseNbtSerializer

class NetworkNbtSerializer : BaseNbtSerializer() {

    override fun readShort(): Int = buffer.getLShort()

    override fun readSignedShort(): Int {
        return buffer.getSignedLShort()
    }

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

    override fun readString(): String {
        return buffer.get(checkReadStringLength(buffer.getUnsignedVarInt()))
    }

    override fun writeString(v: String) {
        buffer.putUnsignedVarInt(checkWriteStringLength(v.length))
        buffer.put(v)
    }

    override fun readFloat(): Float {
        return buffer.getLFloat()
    }

    override fun writeFloat(v: Float) {
        buffer.putLFloat(v)
    }

    override fun readDouble(): Double = buffer.getLDouble()

    override fun writeDouble(v: Double) {
        buffer.putLDouble(v)
    }

    override fun readIntArray(): IntArray {
        val len = readInt() // varInt
        val list: MutableList<Int> = mutableListOf()
        for (i in 0 until len) {
            list.add(readInt()) // varInt
        }

        return list.toIntArray()
    }

    override fun writeIntArray(v: IntArray) {
        writeInt(v.size) // varInt
        v.forEach { value -> writeInt(value) }
    }

    override fun readLongArray(): LongArray {
        val len = readInt() // varInt
        val list: MutableList<Long> = mutableListOf()
        for (i in 0 until len) {
            list.add(readLong()) // varInt
        }

        return list.toLongArray()
    }

    override fun writeLongArray(v: LongArray) {
        writeInt(v.size) // varInt
        v.forEach { value -> writeLong(value) }
    }
}
