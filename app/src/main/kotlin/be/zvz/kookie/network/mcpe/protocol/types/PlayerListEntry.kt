package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData
import java.util.*

class PlayerListEntry {
    lateinit var uuid: UUID
    var entityUniqueId: Long = 0
    lateinit var username: String
    lateinit var skinData: SkinData
    lateinit var xboxUserId: String
    var platformChatId: String = ""
    var buildPlatform: Int = DeviceOS.UNKNOWN.id
    var isTeacher: Boolean = false
    var isHost: Boolean = false

    companion object {
        fun createRemovalEntry(uuid: UUID) = PlayerListEntry().apply {
            this.uuid = uuid
        }

        fun createAdditionEntry(
            uuid: UUID,
            entityUniqueId: Long,
            username: String,
            skinData: SkinData,
            xboxUserId: String = "",
            platformChatId: String = "",
            buildPlatform: Int = -1,
            isTeacher: Boolean = false,
            isHost: Boolean = false
        ) = PlayerListEntry().apply {
            this.uuid = uuid
            this.entityUniqueId = entityUniqueId
            this.username = username
            this.skinData = skinData
            this.xboxUserId = xboxUserId
            this.platformChatId = platformChatId
            this.buildPlatform = buildPlatform
            this.isTeacher = isTeacher
            this.isHost = isHost
        }
    }
}
