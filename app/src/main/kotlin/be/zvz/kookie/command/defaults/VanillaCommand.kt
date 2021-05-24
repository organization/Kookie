package be.zvz.kookie.command.defaults

import be.zvz.kookie.command.Command

open class VanillaCommand(
    name: String,
    description: String = "",
    usageMessage: String = "/$name",
    aliases: MutableList<String> = mutableListOf(),
) : Command(name, description, usageMessage, aliases) {

    companion object {
        const val MAX_COORD = 30_000_000
        const val MIN_COORD = -30_000_000
    }
}
