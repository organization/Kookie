package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class DoubleTag(private val value: Double) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.DOUBLE
    }

    override fun getValue(): Double {
        return value
    }
}