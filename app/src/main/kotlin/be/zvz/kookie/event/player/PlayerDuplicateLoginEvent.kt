package be.zvz.kookie.event.player

import be.zvz.kookie.event.Event
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.player.Player

class PlayerDuplicateLoginEvent(val connectingSession: NetworkSession, val existingSession: NetworkSession) : Event() {
    var disconnectMessage: String = "Logged in from another location"
}
