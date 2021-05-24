package be.zvz.kookie.command.utils

open class InvalidCommandSyntaxException(
    message: String? = null,
    cause: Throwable? = null
) : CommandException(message, cause)
