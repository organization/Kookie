/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
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
