package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.NBTException

class StringTag(private val value: String) : Tag() {

    init {
        if (value.length > 32767) {
            throw NBTException("StringTag cannot hold more than 32767 bytes, got " + value.length)
        }
    }

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.STRING
    }

    override fun getValue(): String {
        return value
    }
}