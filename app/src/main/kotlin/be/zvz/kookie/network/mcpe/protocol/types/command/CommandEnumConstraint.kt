package be.zvz.kookie.network.mcpe.protocol.types.command

class CommandEnumConstraint(private val enum: CommandEnum, private val valueOffset: Int, private val constraints: MutableList<Int>) {

    fun getEnum(): CommandEnum = enum

    fun getValueOffset(): Int = valueOffset

    fun getConstraints(): MutableList<Int> = constraints

    fun getAffectedValue(): String {
        return enum.getEnumValues()[valueOffset]
    }
}