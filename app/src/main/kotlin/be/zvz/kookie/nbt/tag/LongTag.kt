package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class LongTag(private val value: Long) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.LONG
    }

    override fun getValue(): Long {
        return value
    }
}