package be.zvz.kookie.command.utils

open class CommandException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
