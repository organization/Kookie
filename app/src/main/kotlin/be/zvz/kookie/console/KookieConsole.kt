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

import be.zvz.kookie.Server
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder

class KookieConsole(private val server: Server) : SimpleTerminalConsole() {
    override fun isRunning(): Boolean = true

    override fun runCommand(command: String?) {
    }

    override fun shutdown() {
    }

    override fun buildReader(builder: LineReaderBuilder): LineReader {
        return super.buildReader(builder.appName("Kookie"))
    }
}
