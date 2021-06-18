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
package be.zvz.kookie

import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.console.KookieConsole
import be.zvz.kookie.console.brightCyan
import be.zvz.kookie.constant.CorePaths
import be.zvz.kookie.constant.FilePermission
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.network.Network
import be.zvz.kookie.network.mcpe.protocol.ProtocolInfo
import be.zvz.kookie.network.mcpe.raklib.RakLibInterface
import be.zvz.kookie.network.query.QueryInfo
import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.config.PropertiesBrowser
import be.zvz.kookie.world.World
import be.zvz.kookie.world.WorldManager
import be.zvz.kookie.world.format.io.WorldProviderManager
import ch.qos.logback.classic.Logger
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.net.InetSocketAddress
import java.nio.file.Path
import java.util.Date
import kotlin.concurrent.thread
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.setPosixFilePermissions
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.system.exitProcess
import ch.qos.logback.classic.Level as LoggerLevel

class Server(cwd: Path, dataPath: Path, pluginPath: Path) {

    /**
     * Counts the ticks since the server start
     *
     */
    private var tickCounter: Int = 0
    private var nextTick: Long = 0
    private val tickAverage: FloatArray = FloatArray(20) { 20F }
    private val useAverage: FloatArray = FloatArray(20) { 0F }
    private var currentTPS: Float = 20F
    private var currentUse: Float = 0F
    private val startTime: Date
    var isRunning: Boolean = true
        private set
    private var hasStopped: Boolean = false
        private set

    private var doTitleTick = true
    private val logger = LoggerFactory.getLogger(Server::class.java)
    private val console = KookieConsole(this)
    private var maxPlayers: Int = 20
    private var onlineMode = true
    private var networkCompressionAsync = true
    val memoryManager: MemoryManager

    val worldManager: WorldManager

    private val network = Network(this, logger)

    var language: Language
        private set
    private var forceLanguage = false
    val configGroup: ServerConfigGroup

    val queryInfo = QueryInfo()

    init {
        instance = this
        startTime = Date()

        val worldsPath = dataPath.resolve("worlds")
        if (!worldsPath.exists()) {
            worldsPath.createDirectories()
            worldsPath.setPosixFilePermissions(FilePermission.perm777)
        }
        val playersPath = dataPath.resolve("players")
        if (!playersPath.exists()) {
            playersPath.createDirectories()
            playersPath.setPosixFilePermissions(FilePermission.perm777)
        }
        if (pluginPath.exists()) {
            pluginPath.createDirectories()
            pluginPath.setPosixFilePermissions(FilePermission.perm777)
        }

        logger.info("Loading server configuration")
        val kookieDataPath = pluginPath.resolve("kookie.yml")
        if (!kookieDataPath.exists()) {
            kookieDataPath.toFile().outputStream().use { fos ->
                BufferedOutputStream(fos).use {
                    IOUtils.copy(this::class.java.getResourceAsStream("kookie.yml"), it)
                }
            }
        }
        configGroup = ServerConfigGroup(
            Config(dataPath.resolve("kookie.yml"), Config.Type.YAML),
            Config(
                dataPath.resolve(App.SERVER_PROPERTIES_NAME),
                Config.Type.PROPERTIES,
                PropertiesBrowser().apply {
                    put("motd", "${VersionInfo.NAME} Server")
                    put("server-port", 19132)
                    put("white-list", false)
                    put("max-players", 20)
                    put("gamemode", 0)
                    put("force-gamemode", false)
                    put("hardcore", false)
                    put("pvp", true)
                    put("difficulty", World.DIFFICULTY_NORMAL)
                    put("generator-settings", "")
                    put("level-name", "world")
                    put("level-seed", "")
                    put("level-type", "DEFAULT")
                    put("enable-query", true)
                    put("auto-save", true)
                    put("view-distance", 8)
                    put("xbox-auth", true)
                    put("language", "eng")
                }
            )
        )

        val debugLogLevel = configGroup.getProperty("debug.level").asLong(1)
        if (debugLogLevel > 1) {
            (LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as Logger).level = LoggerLevel.DEBUG
        }

        forceLanguage = configGroup.getProperty("settings.force-language").asBoolean(false)
        val selectedLang = configGroup.getConfigString(
            "language",
            configGroup.getProperty("settings.language").text() ?: Language.FALLBACK_LANGUAGE
        )
        language = try {
            Language(selectedLang)
        } catch (e: Language.LanguageNotFoundException) {
            logger.error(e.message)
            try {
                Language(Language.FALLBACK_LANGUAGE)
            } catch (e: Language.LanguageNotFoundException) {
                logger.error("Fallback language ${Language.FALLBACK_LANGUAGE} not found", e)
                throw e
            }
        }

        logger.info(
            language.translateString(
                "language.selected",
                listOf(
                    language.name,
                    language.lang
                )
            )
        )

        if (VersionInfo.IS_DEVELOPMENT_BUILD) {
            if (configGroup.getProperty("settings.enable-dev-builds").asBoolean(false)) {
                logger.error(
                    language.translateString(
                        "pocketmine.server.devBuild.error1",
                        listOf(
                            VersionInfo.NAME
                        )
                    )
                )
                logger.error(language.translateString("pocketmine.server.devBuild.error2"))
                logger.error(language.translateString("pocketmine.server.devBuild.error3"))
                logger.error(
                    language.translateString(
                        "pocketmine.server.devBuild.error4",
                        listOf(
                            "settings.enable-dev-builds"
                        )
                    )
                )
                logger.error(
                    language.translateString(
                        "pocketmine.server.devBuild.error5",
                        listOf(
                            "https://github.com/organization/Kookie/releases"
                        )
                    )
                )
                throw RuntimeException("settings.enable-dev-builds")
            }
        }

        memoryManager = MemoryManager(this)
        // TODO: provider manager
        val providerManager = WorldProviderManager()
        worldManager = WorldManager(this, worldsPath.toString(), providerManager)

        network.addInterface(
            RakLibInterface(
                this,
                network.getSessionManager(),
                InetSocketAddress("0.0.0.0", configGroup.getConfigLong("server-port").toInt())
            )
        )

        language.translateString("pocketmine.server.start", listOf(ProtocolInfo.MINECRAFT_VERSION_NETWORK.brightCyan()))

        thread(isDaemon = true, name = "${VersionInfo.NAME}-console") {
            console.start()
        }
        tickProcessor()
        forceShutdown()
    }

    fun getDataPath(): Path = CorePaths.PATH

    fun broadcastMessage(message: String): Int {
        TODO("Not yet implemented")
    }

    fun broadcastMessage(message: TranslationContainer): Int {
        TODO("Not yet implemented")
    }

    fun broadcastMessage(message: String, recipients: List<CommandSender>): Int {
        TODO("Not yet implemented")
    }

    fun broadcastMessage(message: TranslationContainer, recipients: List<CommandSender>): Int {
        TODO("Not yet implemented")
    }

    private fun tickProcessor() {
        nextTick = System.currentTimeMillis()

        while (isRunning) {
            tick()
        }
    }

    fun forceShutdown() {
        if (hasStopped) {
            return
        }
        if (doTitleTick) {
            print(0x1b.toChar() + "]0;" + 0x17.toChar())
        }
        try {
            if (!isRunning) {
                // TODO: sendUsage(SendUsageTask.Type.CLOSE)
            }
            hasStopped = true
            shutdown()

            /*
            TODO:

            logger.debug("Disabling all plugins")
            pluginManager.disablePlugins()

            logger.debug("Unloading all worlds")
            worldManager.worlds.apply {
                val iterator = iterator()
                while (iterator.hasNext()) {
                    worldManager.unloadWorld(iterator.next(), true)
                }
            }

            logger.debug("Removing event handlers")
            HandlerListManager.global().unregisterAll()

            logger.debug("Shutting down async task worker pool")
            asyncPool.shutdown()
             */

            network.getSessionManager().close(configGroup.getProperty("settomgs.shutdown-message").toString())
            configGroup.save()
            // console.shutdown()
            logger.debug("Stopping network interfaces")
            network.interfaces.apply {
                val iterator = iterator()
                while (iterator.hasNext()) {
                    network.removeInterface(iterator.next())
                }
            }
        } catch (e: Throwable) {
            logger.error("Crashed while crashing, killing process...", e)
            exitProcess(1)
        }
    }

    fun shutdown() {
        isRunning = false
    }

    private fun tick() {
        val tickTime = System.currentTimeMillis()
        if ((tickTime - nextTick) < -0.025) { // 1 tick
            return
        }

        // TODO: Timings.serverTick.startTiming()

        // TODO: Timings.scheduler.startTiming()
        // TODO: pluginManager.tickSchedulers(tickCounter)
        // TODO: Timings.scheduler.stopTiming()

        // TODO: Timings.scheduleAsync.startTiming()
        // TODO: asyncPool.collectTasks()
        // TODO: Timings.scheduleAsync.stopTiming()

        // TODO: worldManager.tick()

        // TODO: Timings.connection.startTiming()
        // TODO: network.tick()

        if (tickCounter % 20 == 0) {
            if (doTitleTick) {
                doTitleTick()
            }

            /*
            TODO:
            val ev = QueryRegenerateEvent(QueryInfo(this))
            ev.call()
            queryInfo = ev.getQueryInfo()
             */

            // TODO: network.updateName()
            // TODO: network.bandwidthTracker.rotateAverageHistory()
        }

        /*
        TODO:
        if (sendUsageTicker > 0 && --sendUsageTicker == 0) {
            sendUsageTicker = 6000
            sendUsage(SendUsageTask.Type.STATUS) // TODO: this should be enum
        }

         */
        if (tickCounter % 100 == 0) {
            worldManager.worlds.forEach { (_, world) ->
                // TODO: world.clearCache()
            }
            /*
            TODO:
             if (getTicksPerSecondAverage() < 12) {
                logger.warn(language.translateString("pocketmine.server.tickOverload"))
            }
             */
        }

        memoryManager.check()

        // TODO: Timings.serverTick.stopTiming()

        val now = System.currentTimeMillis()
        currentTPS = min(20F, 1 / max(0.001F, (now - tickTime).toFloat()))
        currentUse = min(1F, (now - tickTime) / 0.05F)

        // TODO: TimingsHandler.tick(currentTPS <= profileingTickRate)

        val idx = tickCounter % 20
        tickAverage[idx] = currentTPS
        useAverage[idx] = currentUse

        if ((nextTick - tickTime) < -1) {
            nextTick = tickTime
        } else {
            nextTick += 0.05.toLong()
        }
    }

    private fun doTitleTick() {
        // TODO: Timings.titleTick.startTiming()
        val runtime = Runtime.getRuntime()
        val used = round((runtime.totalMemory() - runtime.freeMemory()).toDouble() / 1024 / 1024)

        val online = 0 // TODO: playerList
        val connecting = 0 // TODO: network.getConnectionCount() - online
        // val bandwidthStats = network.getBandwidthTracker()
        print(
            0x1b.toChar() + "]0;" + VersionInfo.NAME +
                " | Online $online/$maxPlayers" +
                (if (connecting > 0) "(+$connecting connecting)" else "") +
                " | Memory $used" +
                // TODO: " | U " + round(bandwidthStats.send.getAverageBytes() / 1024) +
                // TODO: " D " + round(bandwidthStats.receive.getAverageBytes() / 1024) +
                // TODO: " kB/s "
                // TODO: " | TPS " + getTicksPerSecondAverage()
                // TODO: " | Load " + getTickUsageAverage() +
                0x07.toChar()
        )
    }

    companion object {
        @JvmStatic
        lateinit var instance: Server
    }
}
