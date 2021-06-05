package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

class BlockPosMetadataProperty(val value: Vector3) : MetadataProperty() {
    override val id = EntityMetadataTypes.POS
    override fun write(output: PacketSerializer) {
        val floor = value.floor()
        output.putBlockPosition(floor.x.toInt(), floor.y.toInt(), floor.z.toInt())
    }

    override fun equals(other: Any?): Boolean {
        if (other != null) {
            other as BlockPosMetadataProperty
            return other.javaClass == this.javaClass && other.hashCode() == hashCode() && other.value == value
        }
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + id
        return result
    }

    companion object {
        fun read(input: PacketSerializer): BlockPosMetadataProperty {
            val x = AtomicInteger()
            val y = AtomicInteger()
            val z = AtomicInteger()
            input.getBlockPosition(x, y, z)
            return BlockPosMetadataProperty(Vector3(x.get(), y.get(), z.get()))
        }
    }
}
