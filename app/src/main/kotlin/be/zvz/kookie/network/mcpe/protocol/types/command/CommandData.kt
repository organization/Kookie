package be.zvz.kookie.network.mcpe.protocol.types.command

class CommandData(
    var name: String,
    var description: String,
    var flags: Int,
    var permission: Int,
    alias: CommandEnum?,
    overloads: MutableMap<Int, MutableMap<Int, CommandParameter>>
)
