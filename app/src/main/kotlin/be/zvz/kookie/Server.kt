package be.zvz.kookie

import be.zvz.kookie.console.KookieConsole
import be.zvz.kookie.snooze.SleeperHandler
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

class Server(cwd: Path, dataPath: Path, pluginPath: Path) {
    private var tickSleeper = SleeperHandler()
    /**
     * Counts the ticks since the server start
     *
     */
    private var tickCounter: Int = 0
    private var nextTick: Float = 0F
    private val tickAverage: Array<Float> = arrayOf(20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F)
    private val useAverage: Array<Float> = arrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
    private var currentTPS: Float = 20F
    private var currentUse: Float = 0F
    private val startTime: Date

    private var doTitleTick = true
    private val logger = LoggerFactory.getLogger(Server::class.java)
    private val console = KookieConsole()

    init {
        instance = this
        startTime = Date()

        val worldsPath = dataPath.resolve("worlds")
        if (!worldsPath.exists()) {
            worldsPath.createDirectories()
        }
        val playersPath = dataPath.resolve("players")
        if (!playersPath.exists()) {
            playersPath.createDirectories()
        }

        thread(isDaemon = true, name = "Kookie-console") {
            console.start()
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: Server
    }
}
