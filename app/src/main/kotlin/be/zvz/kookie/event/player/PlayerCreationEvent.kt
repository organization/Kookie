package be.zvz.kookie.event.player

import be.zvz.kookie.event.Event
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.player.Player

class PlayerCreationEvent(val session: NetworkSession) : Event() {

    var baseClass: Class<out Player> = Player::class.java

    var playerClass: Class<out Player> = Player::class.java

    
}
