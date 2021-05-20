package be.zvz.kookie.player

import java.util.*

class PlayerInfo(private val username: String, private val uuid: UUID, private val skin: String, private val locale: String, private val extraData: Nothing? = null) {

    fun getUsername(): String {
        return username
    }

    fun getUUID(): UUID {
        return uuid
    }

    fun getSkin(): String {
        return skin // TODO: change this to its own class when it is possible
    }

    fun getLocale(): String {
        return locale
    }

    fun getExtraData(): Nothing? {
        return extraData // TODO: change this to extra data class
    }
}
