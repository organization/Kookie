package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.PacketDecodeException

data class ScorePacketEntry @JvmOverloads constructor(
    val scoreboardId: Long,
    val objectiveName: String,
    val score: Int,
    var type: Type? = null,
    var entityUniqueId: Long = 0,
    var customName: String = ""
) {

    enum class Type(val id: Int) {
        PLAYER(1),
        ENTITY(2),
        FAKE_PLAYER(3);

        companion object {
            private val VALUES = values()
            fun from(value: Int) = VALUES.firstOrNull { it.id == value }
                ?: throw PacketDecodeException("Unhandled set score entry type $value!")
        }
    }
}
