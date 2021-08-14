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
import be.zvz.kookie.command.ConsoleCommandSender
import be.zvz.kookie.command.SimpleCommandMap
import be.zvz.kookie.console.KookieConsole
import be.zvz.kookie.console.brightCyan
import be.zvz.kookie.constant.CorePaths
import be.zvz.kookie.constant.FilePermission
import be.zvz.kookie.crafting.CraftingManager
import be.zvz.kookie.event.server.CommandEvent
import be.zvz.kookie.event.server.QueryRegenerateEvent
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.network.Network
import be.zvz.kookie.network.mcpe.protocol.ProtocolInfo
import be.zvz.kookie.network.mcpe.raklib.RakLibInterface
import be.zvz.kookie.network.query.QueryInfo
import be.zvz.kookie.permission.BanList
import be.zvz.kookie.player.Player
import be.zvz.kookie.plugin.PluginManager
import be.zvz.kookie.scheduler.AsyncPool
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler
import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.OS
import be.zvz.kookie.utils.TextFormat
import be.zvz.kookie.utils.config.PropertiesBrowser
import be.zvz.kookie.world.World
import be.zvz.kookie.world.WorldManager
import ch.qos.logback.classic.Logger
import com.koloboke.collect.map.hash.HashObjObjMaps
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.net.InetSocketAddress
import java.nio.file.Path
import java.util.Date
import java.util.UUID
import kotlin.concurrent.thread
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.setPosixFilePermissions
import kotlin.math.max
import kotlin.math.min
import ch.qos.logback.classic.Level as LoggerLevel

class Server(dataPath: Path, pluginPath: Path) {
    val name: String get() = VersionInfo.NAME
    val kookieVersion: String = TODO()
    val apiVersion: String get() = VersionInfo.BASE_VERSION
    val version: String get() = ProtocolInfo.MINECRAFT_VERSION_NETWORK

    val ip: String get() = configGroup.getConfigString("server-ip", "0.0.0.0").takeIf { it.isNotBlank() } ?: "0.0.0.0"
    val port: Int get() = configGroup.getConfigLong("server-port", 19132).toInt()

    val viewDistance: Int get() = configGroup.getConfigLong("view-distance", 8).toInt()

    val banByName: BanList = TODO()
    val banById: BanList = TODO()

    val operators: Config = TODO()
    val whitelist: Config = TODO()

    var isRunning: Boolean = false
        private set
    private var hasStopped: Boolean = false

    val pluginManager: PluginManager = PluginManager()

    val profilingTickRate: Int = 20
    // TODO: val updater: AutoUpdater

    val asyncPool: AsyncPool

    /** Counts the ticks since the server start */
    var tickCounter: Long = 0
        private set
    private var nextTick: Double = 0.0
    private val tickAverage: FloatArray = FloatArray(20) { 20F }
    private val useAverage: FloatArray = FloatArray(20) { 0F }
    private var currentTPS: Float = 20F
    private var currentUse: Float = 0F

    @Deprecated("Unnecessary rounded", ReplaceWith("currentTPS"))
    val ticksPerSecond: Float
        get() = currentTPS
    val ticksPerSecondAverage: Float get() = tickAverage.average().toFloat()
    val tickUsage: Float get() = currentUse * 100
    val tickUsageAverage: Float get() = (useAverage.average() * 100).toFloat()

    var startTime: Date = Date()
        private set

    private var doTitleTick = true
    private var sendUsageTicker: Int = 0

    val logger = LoggerFactory.getLogger(Server::class.java)
    val memoryManager: MemoryManager = MemoryManager(this)

    private val console: KookieConsole = KookieConsole(this, consoleSender)

    val commandMap = SimpleCommandMap()

    val craftingManager: CraftingManager =
        CraftingManager.fromDataHelper(this::class.java.getResourceAsStream("/vanilla/recipes.json")!!)

    val consoleSender: ConsoleCommandSender = ConsoleCommandSender(this, language)

    // TODO: val resourceManager: ResourcePackManager

    val worldManager: WorldManager

    var maxPlayers: Int = 20
        private set
    var onlineMode = true
        private set

    val network: Network = Network(this, logger)
    private var networkCompressionAsync = true

    var language: Language
        private set
    var forceLanguage = false
        private set

    val serverId: UUID

    val dataPath: Path get() = CorePaths.PATH
    val pluginPath: String

    private val uniquePlayers: Set<UUID>

    var queryInfo: QueryInfo = QueryInfo(this)

    val configGroup: ServerConfigGroup

    private val playerList: MutableMap<UUID, Player> = HashObjObjMaps.newMutableMap()

    private val broadcastSubscribers: MutableMap<String, MutableSet<CommandSender>> = HashObjObjMaps.newMutableMap()

    init {
        instance = this

        arrayOf(
            dataPath,
            pluginPath,
            dataPath.resolve("worlds"),
            dataPath.resolve("players")
        ).forEach { path ->
            if (!path.exists()) {
                path.createDirectories()
                if (OS.isPosixCompliant) {
                    path.setPosixFilePermissions(FilePermission.perm777)
                }
            }
        }

        logger.info("Loading server configuration")
        val kookieDataPath = dataPath.resolve("kookie.yml")
        if (!kookieDataPath.exists()) {
            kookieDataPath.toFile().outputStream().use { fos ->
                BufferedOutputStream(fos).use {
                    IOUtils.copy(this::class.java.getResourceAsStream("/kookie.yml"), it)
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
                KnownTranslationKeys.LANGUAGE_SELECTED,
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
                        KnownTranslationKeys.POCKETMINE_SERVER_DEVBUILD_ERROR1,
                        listOf(
                            VersionInfo.NAME
                        )
                    )
                )
                logger.error(language.translateString(KnownTranslationKeys.POCKETMINE_SERVER_DEVBUILD_ERROR2))
                logger.error(language.translateString(KnownTranslationKeys.POCKETMINE_SERVER_DEVBUILD_ERROR3))
                logger.error(
                    language.translateString(
                        KnownTranslationKeys.POCKETMINE_SERVER_DEVBUILD_ERROR4,
                        listOf(
                            "settings.enable-dev-builds"
                        )
                    )
                )
                logger.error(
                    language.translateString(
                        KnownTranslationKeys.POCKETMINE_SERVER_DEVBUILD_ERROR5,
                        listOf(
                            "https://github.com/organization/Kookie/releases"
                        )
                    )
                )
                throw RuntimeException("settings.enable-dev-builds")
            }
        }

        network.addInterface(
            RakLibInterface(
                this,
                network.getSessionManager(),
                InetSocketAddress("0.0.0.0", configGroup.getConfigLong("server-port").toInt())
            )
        )

        language.translateString(
            KnownTranslationKeys.POCKETMINE_SERVER_START,
            listOf(ProtocolInfo.MINECRAFT_VERSION_NETWORK.brightCyan())
        )

        thread(isDaemon = true, name = "${VersionInfo.NAME}-console") {
            console.start()
        }

        asyncPool = AsyncPool(
            configGroup.getProperty("settings.async-workers").text().run {
                var poolSize = 2
                if (this ?: "auto" == "auto") {
                    val processors = Runtime.getRuntime().availableProcessors() - 2

                    if (processors > 0) {
                        poolSize = max(1, processors)
                    }
                } else {
                    poolSize = max(1, poolSize)
                }
                poolSize
            }
        )

        tickProcessor()
    }

    /** Returns a view distance up to the currently-allowed limit. */
    fun getAllowedViewDistance(distance: Int): Int = max(2, min(distance, memoryManager.getViewDistance(viewDistance)))

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

    fun getPlayerByPrefix(prefix: String): Player? {
        TODO("Not yet implemented")
    }

    private fun tickProcessor() {
        nextTick = System.currentTimeMillis().toDouble()

        while (isRunning) {
            tick()
        }
    }

    private fun tick() {
        val tickTime: Long = System.currentTimeMillis()
        if (tickTime - nextTick < -0.0025) {
            return
        }

        // TODO: Timings.serverTick.startTiming()

        ++tickCounter

        Timings.schedulerSync.startTiming()
        // TODO: pluginManager.tickSchedulers()
        Timings.schedulerSync.startTiming()

        // TODO: Timings.schedulerAsync.startTiming()
        // TODO: asyncPool.collectTasks()
        // TODO: Timings.schedulerAsync.stopTiming()

        // TODO: worldManager.tick(tickCounter)

        // TODO: Timings.connection.startTiming()
        // TODO: network.tick()
        // TODO: Timings.connection.stopTiming()
        if ((tickCounter % 20) == 0L) {
            if (doTitleTick) {
                titleTick()
            }
            currentTPS = 20F
            currentUse = 0F

            val queryRegenerateEvent = QueryRegenerateEvent(QueryInfo(this))
            queryRegenerateEvent.call()
            queryInfo = queryRegenerateEvent.queryInfo

            // TODO: network.updateName()
            // TODO: network.bandwidthTracker.rotateAverageHistory()
        }

        if (sendUsageTicker > 0 && --sendUsageTicker == 0) {
            sendUsageTicker = 6000
            // TODO: sendUsage(SendUsageTask.Type.STATUS)
        }

        if ((tickCounter % 100) == 0L) {
            /*
            TODO:
            worldManager.worlds.forEach {
                it.clearCache()
            }

             */

            if (ticksPerSecondAverage < 12) {
                logger.warn(language.translateString("pocketmine.server.tickOverload"))
            }
        }

        memoryManager.check()

        // TODO: Timings.serverTick.stopTiming()

        val now = System.currentTimeMillis()

        currentTPS = min(20F, 1 / max(0.001F, (now - tickTime).toFloat()))
        currentUse = min(1F, (now - tickTime) / 0.05F)

        TimingsHandler.tick(currentTPS <= profilingTickRate)

        val idx = tickCounter % 20

        tickAverage[idx.toInt()] = currentTPS
        useAverage[idx.toInt()] = tickTime.toFloat()

        if ((nextTick - tickTime) < -1) {
            nextTick = tickTime.toDouble()
        } else {
            nextTick += 0.05
        }
    }

    private fun titleTick() {
        // TODO: title tick
    }

    @JvmOverloads
    fun dispatchCommand(sender: CommandSender, commandLine: String, internal: Boolean = false): Boolean {
        if (!internal) {
            val ev = CommandEvent(sender, commandLine)
            ev.call()
            if (ev.isCancelled) {
                return false
            }
            val commandLine = ev.command
        }
        if (commandMap.dispatch(sender, commandLine)) {
            return true
        }
        sender.sendMessage(sender.language.translateString(TextFormat.RED + "%commands.generic.notFound"))
        return false
    }

    companion object {
        @JvmStatic
        lateinit var instance: Server
    }
}
