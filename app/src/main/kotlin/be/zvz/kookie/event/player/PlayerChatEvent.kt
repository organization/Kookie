package be.zvz.kookie.event.player

import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.player.Player

class PlayerChatEvent(
    player: Player,
    var message: String,
    var recipients: List<CommandSender> = mutableListOf(),
    var format: String = "chat.type.text"
) : PlayerEvent(player)
