package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.Event
import be.zvz.kookie.player.PlayerInfo
import com.koloboke.collect.map.hash.HashObjObjMaps

class PlayerPreLoginEvent(val info: PlayerInfo, val ip: String, val port: Int, var isAuthRequired: Boolean) :
    Event(),
    Cancellable {
    override var isCancelled: Boolean = false
        get() {
            return !isAllowed()
        }

    var kickReasons: MutableMap<Reason, String> = HashObjObjMaps.newMutableMap()

    fun isKickReasonSet(flag: Reason): Boolean = kickReasons.containsKey(flag)

    fun setKickReason(flag: Reason, message: String) {
        kickReasons[flag] = message
    }

    fun clearKickReason(flag: Reason) {
        if (kickReasons.containsKey(flag)) {
            kickReasons.remove(flag)
        }
    }

    fun clearKickReasons() {
        kickReasons = HashObjObjMaps.newMutableMap()
    }

    fun getKickMessage(flag: Reason): String? = kickReasons[flag]

    fun getFinalKickMessage(): String {
        REASON_PRIORITY.forEach {
            if (kickReasons.containsKey(it)) {
                return kickReasons.getValue(it)
            }
        }
        return ""
    }

    fun isAllowed(): Boolean = kickReasons.isNotEmpty()

    enum class Reason(reason: Int) {
        PLUGIN(0),
        SERVER_FULL(1),
        SERVER_WHITELISTED(2),
        BANNED(3)
    }

    companion object {
        val REASON_PRIORITY: List<Reason> = listOf(
            Reason.PLUGIN,
            Reason.SERVER_FULL,
            Reason.SERVER_WHITELISTED,
            Reason.BANNED
        )
    }
}
