package be.zvz.kookie.form

import be.zvz.kookie.player.Player

abstract class Form {

    abstract fun jsonSerialize(): Map<*, *>

    // TODO: Implement FormResponse
    abstract fun handleResponse(player: Player, response: Any)
}
