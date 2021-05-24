package be.zvz.kookie.utils

object Terminal {

    var enableANSI = false

    const val RESET = "\u001b[0m"

    const val BLACK = "\u001b[0;30m"

    const val RED = "\u001b[0;31m"

    const val GREEN = "\u001b[0;32m"

    const val YELLOW = "\u001b[0;33m"

    const val BLUE = "\u001b[0;34m"

    const val PURPLE = "\u001b[0;35m"

    const val CYAN = "\u001b[0;36m"

    const val WHITE = "\u001b[0;37m"

    fun init(ansi: Boolean = true) {
        enableANSI = ansi
    }

    fun toANSI(message: String): String {
        val regex = "§[0-9a-fgoi]"
        return message
    }
}
