package be.zvz.kookie.nbt

import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.Tag

data class TreeRoot(
    val tag: Tag<*>,
    val name: String = ""
) {
    fun mustGetCompoundTag(): CompoundTag {
        if (tag is CompoundTag) {
            return tag
        }
        throw NbtDataException("Root is not a TAG_Compound")
    }
}
