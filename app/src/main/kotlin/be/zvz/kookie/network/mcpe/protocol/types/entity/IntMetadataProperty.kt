package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class IntMetadataProperty(override var value: Int) : MetadataProperty(), IntegerishMetadataProperty {

    init {
        initialize(value)
    }

    override val id = EntityMetadataTypes.INT

    override fun write(output: PacketSerializer) {
        output.putVarInt(value)
    }

    override fun min(): Int {
        return Int.MIN_VALUE
    }

    override fun max(): Int {
        return Int.MAX_VALUE
    }

    override fun buildFromFlags(flags: Map<Int, Boolean>): IntegerishMetadataProperty {
        var value = 0
        flags.forEach { (flag, v) ->
            if (v) {
                value = value or (1 shl flag)
            }
        }
        return IntMetadataProperty(value)
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + id
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntMetadataProperty

        if (value != other.value) return false
        if (id != other.id) return false

        return true
    }

    companion object {
        fun read(input: PacketSerializer): IntMetadataProperty {
            return IntMetadataProperty(input.getVarInt())
        }
    }
}
