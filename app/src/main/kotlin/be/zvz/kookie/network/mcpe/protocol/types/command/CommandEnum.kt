package be.zvz.kookie.network.mcpe.protocol.types.command

class CommandEnum(private val enumName: String, private val enumValues: MutableList<String>) {

    fun getEnumName(): String = enumName

    fun getEnumValues(): MutableList<String> = enumValues
}
