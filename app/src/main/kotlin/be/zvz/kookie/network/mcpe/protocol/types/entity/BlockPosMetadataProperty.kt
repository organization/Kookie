package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class BlockPosMetadataProperty(val value: PacketSerializer.BlockPosition) : MetadataProperty() {
    override val id = EntityMetadataTypes.POS

    override fun write(output: PacketSerializer) {
        output.putBlockPosition(value)
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
        fun read(input: PacketSerializer): BlockPosMetadataProperty = BlockPosMetadataProperty(input.getBlockPosition())
    }
}
