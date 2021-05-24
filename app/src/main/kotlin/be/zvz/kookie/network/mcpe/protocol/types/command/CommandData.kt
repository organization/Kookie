package be.zvz.kookie.network.mcpe.protocol.types.command

class CommandData(
    var name: String,
    var description: String,
    var flags: Int,
    var permission: Int,
    var aliases: CommandEnum?,
    var overloads: MutableMap<Int, MutableMap<Int, CommandParameter>>
)
