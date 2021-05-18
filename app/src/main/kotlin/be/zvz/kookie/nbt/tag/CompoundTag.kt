package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.NBTException

class CompoundTag : Tag() {

    private val values = HashMap<String, Tag>()

    override fun getTagType(): NBT.TagType {
        return NBT.TagType.COMPOUND
    }

    fun setTag(name: String, tag: Tag) {
        values[name] = tag
    }

    fun getTag(name: String, default: Tag? = null): Tag? {
        return values[name] ?: return default
    }

    fun getTagValue(name: String, default: Any? = null): Any? {
        return values[name]?.getValue() ?: default
    }

    fun setString(name: String, value: String) {
        setTag(name, StringTag(value))
    }

    fun setByte(name: String, value: Byte) {
        setTag(name, ByteTag(value))
    }

    fun setFloat(name: String, value: Float) {
        setTag(name, FloatTag(value))
    }

    fun setDouble(name: String, value: Double) {
        setTag(name, DoubleTag(value))
    }

    fun setInt(name: String, value: Int) {
        setTag(name, IntTag(value))
    }

    fun setLong(name: String, value: Long) {
        setTag(name, LongTag(value))
    }

    fun setShort(name: String, value: Short) {
        setTag(name, ShortTag(value))
    }

    fun setCompound(name: String, value: CompoundTag) {
        setTag(name, value)
    }

    fun getString(name: String): String {
        val tag = getTag(name)
        if (tag !is StringTag) {
            throw NBTException("Found tag $name but tag is not StringTag")
        }
        return tag.getValue()
    }

    fun getByte(name: String): Byte {
        val tag = getTag(name)
        if (tag !is ByteTag) {
            throw NBTException("Found tag $name but tag is not ByteTag")
        }
        return tag.getValue()
    }

    fun getFloat(name: String): Float {
        val tag = getTag(name)
        if (tag !is FloatTag) {
            throw NBTException("Found tag $name but tag is not FloatTag")
        }
        return tag.getValue()
    }

    fun getDouble(name: String): Double {
        val tag = getTag(name)
        if (tag !is DoubleTag) {
            throw NBTException("Found tag $name but tag is not DoubleTag")
        }
        return tag.getValue()
    }

    fun getInt(name: String): Int {
        val tag = getTag(name)
        if (tag !is IntTag) {
            throw NBTException("Found tag $name but tag is not IntTag")
        }
        return tag.getValue()
    }

    fun getLong(name: String): Long {
        val tag = getTag(name)
        if (tag !is LongTag) {
            throw NBTException("Found tag $name but tag is not LongTag")
        }
        return tag.getValue()
    }

    fun getShort(name: String): Short {
        val tag = getTag(name)
        if (tag !is ShortTag) {
            throw NBTException("Found tag $name but tag is not ShortTag")
        }
        return tag.getValue()
    }

    fun getCompoundTag(name: String): CompoundTag {
        val tag = getTag(name)
        if (tag !is CompoundTag) {
            throw NBTException("Found tag $name but tag is not CompoundTag")
        }
        return tag
    }

    fun getListTag(name: String): ListTag {
        val tag = getTag(name)
        if (tag !is ListTag) {
            throw NBTException("Found tag $name but tag is not ListTag")
        }
        return tag
    }

    companion object {
        fun create(): CompoundTag {
            return CompoundTag()
        }
    }
}
