package be.zvz.kookie.event.server

import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.event.Cancellable

class CommandEvent(var command: String, val sender: CommandSender) : ServerEvent(), Cancellable {
    override var isCancelled: Boolean = false
}
