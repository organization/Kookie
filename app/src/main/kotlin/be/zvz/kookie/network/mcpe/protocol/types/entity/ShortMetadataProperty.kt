package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class ShortMetadataProperty(override var value: Int) : MetadataProperty(), IntegerishMetadataProperty {

    override val id = EntityMetadataTypes.SHORT

    init {
        initialize(value)
    }

    override fun write(output: PacketSerializer) {
        output.putLShort(value)
    }

    override fun min(): Int {
        return Short.MIN_VALUE.toInt()
    }

    override fun max(): Int {
        return Short.MAX_VALUE.toInt()
    }

    override fun buildFromFlags(flags: Map<Int, Boolean>): IntegerishMetadataProperty {
        var value = 0
        flags.forEach { (flag, v) ->
            if (v) {
                value = value or (1 shl flag)
            }
        }
        return LongMetadataProperty(value)
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + id
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShortMetadataProperty

        if (value != other.value) return false
        if (id != other.id) return false

        return true
    }

    companion object {
        fun read(input: PacketSerializer): ShortMetadataProperty {
            return ShortMetadataProperty(input.getSignedLShort())
        }
    }
}
