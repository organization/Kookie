package be.zvz.kookie.nbt

interface NbtStreamReader {
    fun readByte(): Byte
    fun readSignedByte(): Int
    fun readShort(): Int
    fun readSignedShort(): Int
    fun readInt(): Int
    fun readLong(): Long
    fun readFloat(): Float
    fun readDouble(): Float
    fun readString(): String
    fun readByteArray(): ByteArray
    fun readIntArray(): IntArray
    fun readLongArray(): LongArray
}
