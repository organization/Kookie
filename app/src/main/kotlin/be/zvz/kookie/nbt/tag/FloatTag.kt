package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class FloatTag(private val value: Float) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.FLOAT
    }

    override fun getValue(): Float {
        return value
    }
}