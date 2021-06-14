package be.zvz.kookie.network.mcpe.protocol.types.command

import java.util.UUID

class CommandOriginData {
    var type: Origin = Origin.UNKNOWN
    lateinit var uuid: UUID
    lateinit var requestId: String
    var playerEntityUniqueId: Long = 0

    enum class Origin(val id: Int) {
        PLAYER(0),
        BLOCK(1),
        MINECART_BLOCK(2),
        DEV_CONSOLE(3),
        TEST(4),
        AUTOMATION_PLAYER(5),
        CLIENT_AUTOMATION(6),
        DEDICATED_SERVER(7),
        ENTITY(8),
        VIRTUAL(9),
        GAME_ARGUMENT(10),
        ENTITY_SERVER(11), // ???
        UNKNOWN(-1);

        companion object {
            private val VALUES = values()
            @JvmStatic
            fun from(findValue: Int): Origin = VALUES.firstOrNull { it.id == findValue } ?: UNKNOWN
        }
    }
}
