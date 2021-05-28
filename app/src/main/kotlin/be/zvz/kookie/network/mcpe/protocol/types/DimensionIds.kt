package be.zvz.kookie.network.mcpe.protocol.types

enum class DimensionIds(val id: Int) {
    OVERWORLD(0),
    NETHER(1),
    THE_END(2);

    companion object {
        fun fromInt(id: Int) = values().first { it.id == id }
    }
}
