package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

abstract class Tag {

    abstract fun getTagType(): NBT.TagType

    open fun getValue(): Any? {
        return null
    }

    fun equals(tag: Tag): Boolean {
        // TODO: implement equals
        return true
    }
}