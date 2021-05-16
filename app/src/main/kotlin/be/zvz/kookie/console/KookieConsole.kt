package be.zvz.kookie.console

import net.minecrell.terminalconsole.SimpleTerminalConsole

class KookieConsole : SimpleTerminalConsole() {
    override fun isRunning(): Boolean {
        return true
    }

    override fun runCommand(command: String?) {
    }

    override fun shutdown() {
    }
}
