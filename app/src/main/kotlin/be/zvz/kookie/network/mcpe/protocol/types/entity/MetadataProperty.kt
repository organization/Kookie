package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

abstract class MetadataProperty {

    abstract val id: Int

    abstract fun write(output: PacketSerializer)

    abstract override fun equals(other: Any?): Boolean

    override fun hashCode(): Int {
        return id
    }
}
