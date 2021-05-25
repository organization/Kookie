package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class Vec3MetadataProperty(val value: Vector3) : MetadataProperty() {

    override val id = EntityMetadataTypes.VECTOR3F

    override fun write(output: PacketSerializer) {
        output.putVector3(value)
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Vector3
        return other == value
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + id
        return result
    }

    companion object {
        fun read(input: PacketSerializer): Vec3MetadataProperty {
            return Vec3MetadataProperty(input.getVector3())
        }
    }
}
