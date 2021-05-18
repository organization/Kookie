package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class ShortTag(private val value: Short) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.SHORT
    }

    override fun getValue(): Short {
        return value
    }
}