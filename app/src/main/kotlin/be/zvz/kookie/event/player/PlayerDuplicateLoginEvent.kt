package be.zvz.kookie.event.player

import be.zvz.kookie.event.Event
import be.zvz.kookie.network.mcpe.NetworkSession

class PlayerDuplicateLoginEvent(val connectingSession: NetworkSession, val existingSession: NetworkSession) : Event() {
    var disconnectMessage: String = "Logged in from another location"
}
