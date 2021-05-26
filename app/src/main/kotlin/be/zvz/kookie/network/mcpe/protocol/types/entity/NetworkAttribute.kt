package be.zvz.kookie.network.mcpe.protocol.types.entity

data class NetworkAttribute(
    val id: String,
    val min: Float,
    val max: Float,
    val current: Float,
    val default: Float
)
