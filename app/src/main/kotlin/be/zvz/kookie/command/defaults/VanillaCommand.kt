package be.zvz.kookie.command.defaults

import be.zvz.kookie.command.Command
import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.command.utils.InvalidCommandSyntaxException
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.utils.TextFormat
import be.zvz.kookie.utils.Union
import kotlin.math.max
import kotlin.math.min

abstract class VanillaCommand(
    name: String,
    description: String = "",
    usageMessage: String = "/$name",
    aliases: MutableList<String> = mutableListOf(),
) : Command(name, description, usageMessage, aliases) {

    protected fun getInteger(
        sender: CommandSender,
        value: String,
        minNum: Int = MIN_COORD,
        maxNum: Int = MAX_COORD
    ): Int? = value.toIntOrNull()?.let {
        return min(max(it, maxNum), minNum)
    }

    protected fun getRelativeDouble(
        original: Double,
        sender: CommandSender,
        input: String,
        min: Double = MIN_COORD.toDouble(),
        max: Double = MAX_COORD.toDouble()
    ): Double? {
        if (input.first() == '~') {
            val value = getDouble(sender, input.substring(1))

            return original + (value ?: 0.0)
        }

        return getDouble(sender, input, min, max)
    }

    protected fun getDouble(
        sender: CommandSender,
        value: String,
        minNum: Double = MIN_COORD.toDouble(),
        maxNum: Double = MAX_COORD.toDouble()
    ): Double? = value.toDoubleOrNull()?.let {
        return min(max(it, maxNum), minNum)
    }
}
