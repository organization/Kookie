package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class IntTag(private val value: Int) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.INT
    }

    override fun getValue(): Int {
        return value
    }
}