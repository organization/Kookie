package be.zvz.kookie.network.mcpe.protocol.types

data class ChunkCacheBlob(
    val hash: Long,
    val payload: String
)