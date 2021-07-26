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
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.network.Network
import be.zvz.kookie.network.mcpe.protocol.ProtocolInfo
import be.zvz.kookie.network.mcpe.raklib.RakLibInterface
import be.zvz.kookie.network.query.QueryInfo
import be.zvz.kookie.player.Player
import be.zvz.kookie.plugin.PluginManager
import be.zvz.kookie.scheduler.AsyncPool
import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.OS
import be.zvz.kookie.utils.config.PropertiesBrowser
import be.zvz.kookie.world.World
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
import ch.qos.logback.classic.Level as LoggerLevel

class Server(dataPath: Path, pluginPath: Path) {

    /**
     * Counts the ticks since the server start
     *
     */
    private var tickCounter: Int = 0
    private var nextTick: Float = 0F
    private val tickAverage: FloatArray = FloatArray(20) { 20F }
    private val useAverage: FloatArray = FloatArray(20) { 0F }
    private var currentTPS: Float = 20F
    private var currentUse: Float = 0F
    private val startTime: Date

    private var doTitleTick = true
    private val logger = LoggerFactory.getLogger(Server::class.java)
    private val console = KookieConsole(this)
    private var maxPlayers: Int = 20
    private var onlineMode = true
    private var networkCompressionAsync = true
    val memoryManager: MemoryManager
    val pluginManager: PluginManager
    val asyncPool: AsyncPool

    private val network: Network

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
            if (OS.isPosixCompliant) {
                worldsPath.setPosixFilePermissions(FilePermission.perm777)
            }
        }
        val playersPath = dataPath.resolve("players")
        if (!playersPath.exists()) {
            playersPath.createDirectories()
            if (OS.isPosixCompliant) {
                playersPath.setPosixFilePermissions(FilePermission.perm777)
            }
        }
        if (!pluginPath.exists()) {
            pluginPath.createDirectories()
            if (OS.isPosixCompliant) {
                pluginPath.setPosixFilePermissions(FilePermission.perm777)
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

        memoryManager = MemoryManager(this)

        network = Network(this, logger)

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

        pluginManager = PluginManager()

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

    fun getPlayerByPrefix(prefix: String): Player? {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        lateinit var instance: Server
    }
}
