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
import be.zvz.kookie.event.player.PlayerLoginEvent
import be.zvz.kookie.event.server.CommandEvent
import be.zvz.kookie.event.server.DataPacketSendEvent
import be.zvz.kookie.event.server.QueryRegenerateEvent
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.Network
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.network.mcpe.NetworkSessionManager
import be.zvz.kookie.network.mcpe.PacketBroadcaster
import be.zvz.kookie.network.mcpe.convert.TypeConverter
import be.zvz.kookie.network.query.QueryInfo
import be.zvz.kookie.permission.BanList
import be.zvz.kookie.permission.DefaultPermissions
import be.zvz.kookie.player.GameMode
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import be.zvz.kookie.plugin.PluginEnableOrder
import be.zvz.kookie.plugin.PluginManager
import be.zvz.kookie.plugin.PluginOwned
import be.zvz.kookie.scheduler.AsyncPool
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler
import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.OS
import be.zvz.kookie.utils.Promise
import be.zvz.kookie.utils.TextFormat
import be.zvz.kookie.utils.config.ConfigBrowser
import be.zvz.kookie.utils.config.PropertiesBrowser
import be.zvz.kookie.world.World
import be.zvz.kookie.world.WorldManager
import ch.qos.logback.classic.Logger
import com.koloboke.collect.map.hash.HashObjObjMaps
import com.koloboke.collect.set.hash.HashObjSets
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockPong
import com.nukkitx.protocol.bedrock.BedrockServer
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler
import com.nukkitx.protocol.bedrock.BedrockServerSession
import com.nukkitx.protocol.bedrock.v475.Bedrock_v475
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
    val version: String get() = currentVersion.minecraftVersion

    val ip: String get() = configGroup.getConfigString("server-ip", "0.0.0.0").takeIf { it.isNotBlank() } ?: "0.0.0.0"
    val port: Int get() = configGroup.getConfigLong("server-port", 19132).toInt()
    val motd: String get() = configGroup.getConfigString("motd", VersionInfo.NAME + " Server")

    val viewDistance: Int get() = configGroup.getConfigLong("view-distance", 8).toInt()

    val banByName: BanList = TODO()
    val banById: BanList = TODO()

    val operators: Config = TODO()
    val whitelist: Config = TODO()
    val hasWhiteList: Boolean get() = configGroup.getConfigBoolean("white-list", false)

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
    val commandAliases: Map<String, List<String>>
        get() {
            val result: MutableMap<String, List<String>> = HashObjObjMaps.newMutableMap()

            val section = configGroup.getProperty("aliases")
            if (section.isList) {
                section.toMap<String, ConfigBrowser>().forEach { (name, node) ->
                    result[name] = if (node.isList) {
                        TODO("How to convert ArrayNode to List?")
                    } else {
                        listOf(node.toString())
                    }
                }
            }

            return result
        }

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
    val gamemode: GameMode get() = GameMode.from(configGroup.getConfigLong("gamemode", 0L).toInt())
    val forceGamemode: Boolean get() = configGroup.getConfigBoolean("force-gamemode", false)
    val difficulty: Int get() = configGroup.getConfigLong("difficulty", World.DIFFICULTY_NORMAL.toLong()).toInt()
    val hardcore: Boolean get() = configGroup.getConfigBoolean("hardcore", false)

    val dataPath: Path get() = CorePaths.PATH
    val pluginPath: String
    val shouldSavePlayerData: Boolean get() = configGroup.getConfigBoolean("player.save-player-data", true)

    private val uniquePlayers: MutableSet<UUID> = HashObjSets.newMutableSet()

    var queryInfo: QueryInfo = QueryInfo(this)

    val configGroup: ServerConfigGroup

    private val playerList: MutableMap<UUID, Player> = HashObjObjMaps.newMutableMap()

    private val broadcastSubscribers: MutableMap<String, MutableSet<CommandSender>> = HashObjObjMaps.newMutableMap()

    private val bedrockServer: BedrockServer

    val sessionManager: NetworkSessionManager = NetworkSessionManager()

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

        bedrockServer = BedrockServer(InetSocketAddress("0.0.0.0", port))
        bedrockServer.bind().whenComplete { _, throwable ->
            if (throwable != null) {
                logger.error("Failed to start server")
                throwable.printStackTrace()
                return@whenComplete
            }
        }

        bedrockServer.handler = object : BedrockServerEventHandler {

            override fun onConnectionRequest(address: InetSocketAddress): Boolean {
                return true // TODO: check IP bans
            }

            override fun onQuery(address: InetSocketAddress): BedrockPong {
                val pong = BedrockPong()
                pong.edition = "MCPE"
                pong.motd = motd
                pong.protocolVersion = currentVersion.protocolVersion
                pong.version = currentVersion.minecraftVersion
                pong.gameType = TypeConverter.protocolGameModeName(GameMode.SURVIVAL) // TODO
                pong.playerCount = playerList.size
                pong.maximumPlayerCount = maxPlayers
                /*
                val plugins: MutableList<String> = mutableListOf()
                val extraData: MutableMap<String, String> = mutableMapOf()
                extraData.put("spliitnum", 0x128.toChar().toString())
                extraData.put("hostname", motd) // TODO: Server Name
                extraData.put("gametype", TypeConverter.protocolGameModeName(GameMode.SURVIVAL)) // TODO
                extraData.put("game_id", "MINECRAFTPE")
                extraData.put("version", currentVersion.minecraftVersion)
                extraData.put("server_engine", )
                extraData.put("plugins", )
                 */
                // TODO
                return pong
            }

            override fun onSessionCreation(serverSession: BedrockServerSession) {
                sessionManager.add(
                    NetworkSession(this@Server, sessionManager, serverSession)
                )
            }
        }

        language.translateString(
            KnownTranslationKeys.POCKETMINE_SERVER_START,
            listOf(currentVersion.minecraftVersion.brightCyan())
        )

        thread(isDaemon = true, name = "${VersionInfo.NAME}-console") {
            console.start()
        }

        asyncPool = AsyncPool(
            configGroup.getProperty("settings.async-workers").text().run {
                var poolSize = 2
                if ((this ?: "auto") == "auto") {
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

    fun getOfflinePlayer(name: String) {
        TODO("Not yet implemented")
    }

    fun getPlayerDataPath(username: String): Path =
        dataPath.resolve("players/${username.lowercase()}.dat")

    fun hasOfflinePlayerData(username: String): Boolean = getPlayerDataPath(username).exists()

    private fun handleCorruptedPlayerData(username: String) {
        TODO("Not yet implemented")
    }

    fun getOfflinePlayerData(username: String): CompoundTag? {
        TODO("Not yet implemented")
    }

    fun saveOfflinePlayerData(username: String, nbtTag: CompoundTag) {
        TODO("Not yet implemented")
    }

    fun createPlayer(
        session: NetworkSession,
        playerInfo: PlayerInfo,
        authenticated: Boolean,
        offlinePlayerData: CompoundTag?
    ): Promise<Player> {
        TODO("Not yet implemented")
    }

    /**
     * Returns an online player with the given name (case insensitive), or null if not found.
     */
    fun getPlayerExact(username: String): Player? =
        playerList.values.find { username.lowercase() == it.name.lowercase() }

    /**
     * Returns an online player whose name begins with or equals the given string (case insensitive).
     * The closest match will be returned, or null if there are no online matches.
     *
     * @see getPlayerExact()
     */
    fun getPlayerByPrefix(username: String): Player? {
        var found: Player? = null
        var delta = Int.MAX_VALUE
        playerList.values.forEach { player ->
            if (player.name.startsWith(username, true)) {
                val currentDelta = player.name.length - name.length
                if (currentDelta < delta) {
                    found = player
                    delta = currentDelta
                }
                if (currentDelta == 0) {
                    return found
                }
            }
        }

        return found
    }

    /**
     * Returns the player online with a UUID equivalent to the specified UuidInterface object, or null if not found
     */
    fun getPlayerByUUID(uuid: UUID): Player? = playerList[uuid]

    fun getPluginCommand(name: String) = commandMap.getCommand(name).takeIf { it is PluginOwned }

    fun addOp(username: String) {
        operators.set(username.lowercase(), true)
        getPlayerExact(username)?.setBasePermission(DefaultPermissions.ROOT_OPERATOR, true)
        operators.save()
    }

    fun removeOp(username: String) {
        getPlayerExact(username)?.unsetBasePermission(DefaultPermissions.ROOT_OPERATOR)
        if (operators.remove(username.lowercase())) {
            operators.save()
        }
    }

    fun isOp(username: String): Boolean = operators.exists(username.lowercase())

    fun addWhitelist(username: String) {
        whitelist.set(username.lowercase(), true)
        whitelist.save()
    }

    fun removeWhitelist(username: String) {
        if (whitelist.remove(username.lowercase())) {
            whitelist.save()
        }
    }

    fun isWhitelisted(username: String): Boolean =
        !hasWhiteList || isOp(username) || whitelist.exists(username.lowercase())

    /**
     * Subscribes to a particular message broadcast channel.
     * The channel ID can be any arbitrary string.
     */
    fun subscribeToBroadcastChannel(channelId: String, subscriber: CommandSender) {
        broadcastSubscribers.getOrPut(channelId, HashObjSets::newMutableSet).add(subscriber)
    }

    /** Unsubscribes from a particular message broadcast channel. */
    fun unsubscribeFromBroadcastChannel(channelId: String, subscriber: CommandSender) {
        broadcastSubscribers[channelId]?.let {
            it.remove(subscriber)
            if (it.isEmpty()) {
                broadcastSubscribers.remove(channelId)
            }
        }
    }

    /** Unsubscribes from all broadcast channels. */
    fun unsubscribeFromAllBroadcastChannels(subscriber: CommandSender) {
        broadcastSubscribers.keys.forEach { unsubscribeFromBroadcastChannel(it, subscriber) }
    }

    /** Returns a list of all the CommandSenders subscribed to the given broadcast channel. */
    fun getBroadcastChannelSubscribers(channelId: String): List<CommandSender> =
        broadcastSubscribers[channelId]?.toList() ?: listOf()

    @JvmOverloads
    fun broadcastMessage(
        message: String,
        recipients: List<CommandSender> = getBroadcastChannelSubscribers(BROADCAST_CHANNEL_USERS)
    ): Int {
        recipients.forEach { it.sendMessage(message) }
        return recipients.size
    }

    @JvmOverloads
    fun broadcastMessage(
        message: TranslationContainer,
        recipients: List<CommandSender> = getBroadcastChannelSubscribers(BROADCAST_CHANNEL_USERS)
    ): Int {
        recipients.forEach { it.sendMessage(message) }
        return recipients.size
    }

    private fun getPlayerBroadcastSubscribers(channelId: String): List<Player> =
        getBroadcastChannelSubscribers(channelId).filterIsInstance<Player>()

    @JvmOverloads
    fun broadcastTip(
        tip: String,
        recipients: List<Player> = getPlayerBroadcastSubscribers(BROADCAST_CHANNEL_USERS)
    ): Int {
        recipients.forEach { TODO("Implements after implementing Player::sendTip()") }
        return recipients.size
    }

    @JvmOverloads
    fun broadcastPopup(
        popup: String,
        recipients: List<Player> = getPlayerBroadcastSubscribers(BROADCAST_CHANNEL_USERS)
    ): Int {
        recipients.forEach { TODO("Implements after implementing Player::sendPopup()") }
        return recipients.size
    }

    @JvmOverloads
    fun broadcastTitle(
        title: String,
        subtitle: String = "",
        fadeIn: Int = -1, // Duration in ticks for fade-in. If -1 is given, client-sided defaults will be used.
        stay: Int = -1, // Duration in ticks to stay on screen for
        fadeOut: Int = -1, // Duration in ticks for fade-out
        recipients: List<Player> = getPlayerBroadcastSubscribers(BROADCAST_CHANNEL_USERS)
    ): Int {
        recipients.forEach { TODO("Implements after implementing Player::sentTitle()") }
        return recipients.size
    }

    fun broadcastPackets(players: List<Player>, packets: List<BedrockPacket>): Boolean {
        if (players.isEmpty() || packets.isEmpty()) return true

        Timings.broadcastPackets.time {
            val ev = DataPacketSendEvent(players.map { it.networkSession }, packets)
            ev.call()
            if (ev.isCancelled) {
                return false
            }

            val broadcasterTargets: MutableMap<PacketBroadcaster, MutableList<NetworkSession>> =
                HashObjObjMaps.newMutableMap()
            ev.targets.forEach { recipient ->
                /** TODO: Implements after implementing NetworkSession::getBroadcaster()
                 * val broadcaster = recipient.getBroadcaster()
                 * broadcasterTargets.getOrPut(broadcaster, ::mutableListOf).add(recipient)
                 */
            }
            broadcasterTargets.forEach { (broadcaster, targets) ->
                broadcaster.broadcastPackets(targets, packets)
            }

            return true
        }
    }

    fun enablePlugins(type: PluginEnableOrder) {
        /** TODO: Implements after implementing PluginManager::getPlugins()
         * pluginManager.getPlugins().forEach { plugin ->
         *     if (!plugin.isEnabled() && plugin.getDescription().getOrder() == type) {
         *         pluginManager.enablePlugin(plugin)
         *     }
         * }
         */
        if (type == PluginEnableOrder.POSTWORLD) {
            /** TODO: Implements after implementing SimpleCommandMap::registerServerAliases()
             * commandMap.registerServerAliases()
             */
        }
    }

    @JvmOverloads
    fun dispatchCommand(sender: CommandSender, commandLine: String, internal: Boolean = false): Boolean {
        var commandLine = commandLine
        if (!internal) {
            val ev = CommandEvent(sender, commandLine)
            ev.call()
            if (ev.isCancelled) {
                return false
            }
            commandLine = ev.command
        }
        if (commandMap.dispatch(sender, commandLine)) {
            return true
        }
        sender.sendMessage(sender.language.translateString(TextFormat.RED + KnownTranslationKeys.COMMANDS_GENERIC_NOTFOUND.key))
        return false
    }

    /**  Shuts the server down correctly */
    fun shutdown() {
        isRunning = false
    }

    fun forceShutdown() {
        TODO("Not yet implemented")
    }

    private fun tickProcessor() {
        nextTick = System.currentTimeMillis().toDouble()

        while (isRunning) {
            tick()
        }
    }

    fun addOnlinePlayer(player: Player): Boolean {
        val ev = PlayerLoginEvent(player, "Plugin reason")
        ev.call()
        if (ev.isCancelled /* TODO: || !player.isConnected() */) {
            // TODO:  player.disconnect(ev.kickMessage)
            return false
        }

        val session = player.networkSession
        val position = player.getPosition()
        logger.info(
            language.translateString(
                KnownTranslationKeys.POCKETMINE_PLAYER_LOGIN,
                listOf(
                    TextFormat.AQUA + player.name + TextFormat.WHITE,
                    session.ip,
                    session.port.toString(),
                    player.getId().toString(),
                    position.world!!.displayName,
                    position.floorX.toString(),
                    position.floorY.toString(),
                    position.floorZ.toString()
                )
            )
        )
        playerList.values.forEach {
            /** TODO: Implements after implementing NetworkSession::onPlayerAdded()
             * it.networkSession.onPlayerAdded(player)
             */
        }
        playerList[player.uuid] = player
        if (sendUsageTicker > 0) {
            uniquePlayers.add(player.uuid)
        }
        return true
    }

    fun removeOnlinePlayer(player: Player) {
        if (playerList.remove(player.uuid) !== null) {
            playerList.values.forEach {
                /** TODO: Implements after implementing NetworkSession::onPlayerRemoved()
                 * it.networkSession.onPlayerRemoved(player)
                 */
            }
        }
    }

    fun sendUsage(type: Int = -1) {
        TODO("Not yet implemented")
    }

    private fun titleTick() {
        // TODO: title tick
    }

    private fun tick() {
        val tickTime: Long = System.currentTimeMillis()
        if (tickTime - nextTick < -0.0025) {
            return
        }

        Timings.serverTick.startTiming()

        ++tickCounter

        Timings.schedulerSync.startTiming()
        // TODO: pluginManager.tickSchedulers()
        Timings.schedulerSync.startTiming()

        Timings.schedulerAsync.startTiming()
        // TODO: asyncPool.collectTasks()
        Timings.schedulerAsync.stopTiming()

        worldManager.tick(tickCounter)

        Timings.connection.startTiming()
        // TODO: network.tick()
        Timings.connection.stopTiming()
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
            worldManager.worlds.forEach { (_, world) ->
                world.clearCache()
            }

            if (ticksPerSecondAverage < 12) {
                logger.warn(language.translateString("pocketmine.server.tickOverload"))
            }
        }

        memoryManager.check()

        Timings.serverTick.stopTiming()

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

    companion object {
        const val BROADCAST_CHANNEL_ADMINISTRATIVE = "kookie.broadcast.admin"
        const val BROADCAST_CHANNEL_USERS = "kookie.broadcast.user"

        @JvmStatic
        lateinit var instance: Server

        val currentVersion = Bedrock_v475.V475_CODEC
    }
}
