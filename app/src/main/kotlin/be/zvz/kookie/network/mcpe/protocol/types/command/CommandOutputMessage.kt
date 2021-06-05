package be.zvz.kookie.network.mcpe.protocol.types.command

class CommandOutputMessage {
    var isInternal: Boolean = false
    var messageId: String = ""
    var parameters: MutableList<String> = mutableListOf()
}
