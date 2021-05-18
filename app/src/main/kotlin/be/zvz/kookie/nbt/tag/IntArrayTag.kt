package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class IntArrayTag(private val value: ArrayList<Int>) : Tag() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.INTARRAY
    }

    override fun getValue(): ArrayList<Int> {
        return value
    }
}