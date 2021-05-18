package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.NBTException

class ListTag(private var tags: ArrayList<Tag> = ArrayList()) : Tag() {

    private var tagType: NBT.TagType? = null

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.LIST
    }

    init {
        for (tag in tags) {
            if (tagType == null) {
                tagType = tag.getTagType()
            } else {
                if (tagType != tag.getTagType()) {
                    throw NBTException("Expected TagType " + tagType!!.name + ", got " + tag.getTagType().name)
                }
            }
        }
    }

    fun push(tag: Tag) {
        if (tag.getTagType() != tagType) {
            throw NBTException("Expected TagType " + tagType!!.name + ", got " + tag.getTagType().name)
        }
        tags.add(tag)
    }

    fun get(index: Int): Tag? {
        return tags.getOrNull(index)
    }

    override fun getValue(): ArrayList<Tag> {
        return tags
    }
}