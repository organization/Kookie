package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class ByteArrayTag(private val value: String) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.BYTEARRAY
    }

    override fun getValue(): String {
        return value
    }
}