package be.zvz.kookie.nbt

interface NbtStreamWriter {
    fun writeByte(): Byte
    fun writeSignedByte(): Int
    fun writeShort(): Int
    fun writeSignedShort(): Int
    fun writeInt(): Int
    fun writeLong(): Long
    fun writeFloat(): Float
    fun writeDouble(): Float
    fun writeString(): String
    fun writeByteArray(): ByteArray
    fun writeIntArray(): IntArray
    fun writeLongArray(): LongArray
}
