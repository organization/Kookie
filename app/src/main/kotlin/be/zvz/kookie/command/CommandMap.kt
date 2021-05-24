package be.zvz.kookie.command

interface CommandMap {
    fun registerAll(fallbackPrefix: String, commands: List<Command>)

    fun register(fallbackPrefix: String, command: Command, label: String?): Boolean

    fun dispatch(sender: CommandSender, cmdLine: String): Boolean

    fun clearCommands()

    fun getCommand(name: String): Command?
}
