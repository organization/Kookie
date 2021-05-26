package be.zvz.kookie.nbt

import be.zvz.kookie.utils.Binary
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class LittleEndianNbtSerializer() : BaseNbtSerializer() {
    override fun readShort(): Int = buffer.getLShort()
    override fun readSignedShort(): Int = buffer.getSignedLShort()
    override fun readInt(): Int = buffer.getLInt()
    override fun readLong(): Long = buffer.getLLong()
    override fun readFloat(): Float = buffer.getLFloat()
    override fun readDouble(): Double = buffer.getLDouble()

    private fun unpackVStar(value: String, byteSize: Int): ByteBuffer {
        val bytes = value.toByteArray()
        val buf = ByteBuffer.allocate(byteSize)
        buf.order(ByteOrder.LITTLE_ENDIAN)
        buf.put(bytes)
        buf.flip()
        return buf
    }
    private fun packVStar(value: IntArray): String {
        val byteBuffer = ByteBuffer.allocate(Int.SIZE_BYTES).apply {
            order(ByteOrder.LITTLE_ENDIAN)
        }
        value.forEach {
            byteBuffer.putInt(it)
        }
        return Binary.toPositiveByteArray(byteBuffer.array()).toString(StandardCharsets.UTF_8)
    }
    private fun packVStar(value: LongArray): String {
        val byteBuffer = ByteBuffer.allocate(Long.SIZE_BYTES).apply {
            order(ByteOrder.LITTLE_ENDIAN)
        }
        value.forEach {
            byteBuffer.putLong(it)
        }
        return Binary.toPositiveByteArray(byteBuffer.array()).toString(StandardCharsets.UTF_8)
    }

    override fun readIntArray(): IntArray {
        val len = readInt()
        val unpacked = unpackVStar(buffer.get(len * Int.SIZE_BYTES), Int.SIZE_BYTES)
        return unpacked.asIntBuffer().array()
    }

    override fun readLongArray(): LongArray {
        val len = readInt()
        val unpacked = unpackVStar(buffer.get(len * Long.SIZE_BYTES), Long.SIZE_BYTES)
        return unpacked.asLongBuffer().array()
    }

    override fun writeShort(v: Int) {
        buffer.putLShort(v)
    }

    override fun writeInt(v: Int) {
        buffer.putLInt(v)
    }

    override fun writeLong(v: Long) {
        buffer.putLLong(v)
    }

    override fun writeFloat(v: Float) {
        buffer.putLFloat(v)
    }

    override fun writeDouble(v: Double) {
        buffer.putLDouble(v)
    }

    override fun writeIntArray(v: IntArray) {
        writeInt(v.size)
        buffer.put(packVStar(v))
    }

    override fun writeLongArray(v: LongArray) {
        writeInt(v.size)
        buffer.put(packVStar(v))
    }
}
