package be.zvz.kookie.network.mcpe.protocol.types

data class MapTrackedObject(
    var type: Int = 0,
    var entityUniqueId: Long = 0,
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0
) {
    companion object {
        const val TYPE_ENTITY = 0
        const val TYPE_BLOCK = 1
    }
}
