package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class SpawnSettings(private val biomeType: Int, private val biomeName: String, private val dimension: Int) {

    fun getBiomeType(): Int = biomeType

    fun getBiomeName(): String = biomeName

    fun getDimension(): Int = dimension

    fun write(output: PacketSerializer) {
        output.putLShort(biomeType)
        output.putString(biomeName)
        output.putVarInt(dimension)
    }

    companion object {
        const val BIOME_TYPE_DEFAULT = 0
        const val BIOME_TYPE_USER_DEFINED = 1

        fun read(input: PacketSerializer): SpawnSettings {
            val biomeType = input.getLShort()
            val biomeName = input.getString()
            val dimension = input.getVarInt()
            return SpawnSettings(biomeType, biomeName, dimension)
        }
    }
}
