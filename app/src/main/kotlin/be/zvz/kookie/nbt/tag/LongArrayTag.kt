package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT

class LongArrayTag(override val value: LongArray) : Tag<LongArray>() {

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.BYTE_ARRAY
    }

    override fun makeCopy(): Tag<LongArray> = LongArrayTag(value)
}
