package be.zvz.kookie.console

import org.slf4j.Logger

class PrefixedLogger(private val prefix: String, val delegate: Logger) {

    fun info(message: String) {
        delegate.info("[$prefix] $message")
    }

    fun debug(message: String) {
        delegate.debug("[$prefix] $message")
    }

    fun warn(message: String) {
        delegate.warn("[$prefix] $message")
    }

    fun error(message: String) {
        delegate.error("[$prefix] $message")
    }

    fun error(message: String, t: Throwable) {
        delegate.error("[$prefix] $message", t)
    }
}
