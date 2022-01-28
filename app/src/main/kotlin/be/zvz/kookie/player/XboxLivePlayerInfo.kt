package be.zvz.kookie.player

import be.zvz.kookie.entity.Skin
import java.util.UUID

class XboxLivePlayerInfo(
    val xuid: String,
    username: String,
    uuid: UUID,
    skin: Skin,
    locale: String
) : PlayerInfo(username, uuid, skin, locale) {

    fun withoutXboxData(): PlayerInfo {
        return PlayerInfo(username, uuid, skin, locale)
    }
}
