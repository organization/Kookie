package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class StringMetadataProperty(val value: String) : MetadataProperty() {

    override val id = EntityMetadataTypes.STRING

    override fun write(output: PacketSerializer) {
        output.putString(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringMetadataProperty

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
        fun read(input: PacketSerializer): StringMetadataProperty {
            return StringMetadataProperty(input.getString())
        }
    }
}
