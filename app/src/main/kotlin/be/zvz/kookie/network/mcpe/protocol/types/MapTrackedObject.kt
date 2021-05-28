package be.zvz.kookie.network.mcpe.protocol.types

class MapTrackedObject(
    var type: Int? = null,
    var entityUniqueId: Long? = null,
    var x: Int? = null,
    var y: Int? = null,
    var z: Int? = null
) {
    companion object {
        const val TYPE_ENTITY = 0
        const val TYPE_BLOCK = 1
    }
}
