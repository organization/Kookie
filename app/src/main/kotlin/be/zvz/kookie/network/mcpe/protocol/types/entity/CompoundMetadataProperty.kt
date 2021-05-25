package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class CompoundMetadataProperty(var value: CompoundTag) : MetadataProperty() {

    override val id = EntityMetadataTypes.COMPOUND_TAG
    override fun write(output: PacketSerializer) {
        output.put((LittleEndianNbtSerializer()).write(TreeRoot(value)))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompoundMetadataProperty

        if (value != other.value) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + id
        return result
    }

    companion object {
        fun read(input: PacketSerializer): CompoundMetadataProperty {
            return CompoundMetadataProperty(input.getNbtCompoundRoot())
        }
    }
}