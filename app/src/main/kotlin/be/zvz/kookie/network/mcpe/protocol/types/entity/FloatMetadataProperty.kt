package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class FloatMetadataProperty(val value: Float) : MetadataProperty() {

    override val id = EntityMetadataTypes.FLOAT
    override fun write(output: PacketSerializer) {
        output.putLFloat(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatMetadataProperty

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
        fun read(input: PacketSerializer): FloatMetadataProperty {
            return FloatMetadataProperty(input.getLFloat())
        }
    }
}
