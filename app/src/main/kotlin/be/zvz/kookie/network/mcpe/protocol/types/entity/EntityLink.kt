package be.zvz.kookie.network.mcpe.protocol.types.entity

class EntityLink(
    val fromEntityId: Long,
    val toEntityId: Long,
    val type: Int,
    val immediate: Boolean,
    val causedByRider: Boolean
) {

    companion object {
        const val TYPE_REMOVE = 0
        const val TYPE_RIDER = 1
        const val TYPE_PASSENGER = 2
    }
}
