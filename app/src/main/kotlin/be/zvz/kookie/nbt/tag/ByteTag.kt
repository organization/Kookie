package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class ByteTag(private val value: Byte) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.BYTE
    }

    override fun getValue(): Byte {
        return value
    }
}