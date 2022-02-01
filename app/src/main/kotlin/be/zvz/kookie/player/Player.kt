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
package be.zvz.kookie.player

import be.zvz.kookie.Server
import be.zvz.kookie.command.CommandSender
import be.zvz.kookie.console.PrefixedLogger
import be.zvz.kookie.entity.Human
import be.zvz.kookie.entity.Location
import be.zvz.kookie.entity.Skin
import be.zvz.kookie.event.player.PlayerChangeSkinEvent
import be.zvz.kookie.event.player.PlayerDisplayNameChangeEvent
import be.zvz.kookie.event.player.PlayerJoinEvent
import be.zvz.kookie.form.Form
import be.zvz.kookie.inventory.CallbackInventoryListener
import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.inventory.PlayerCraftingInventory
import be.zvz.kookie.inventory.PlayerCursorInventory
import be.zvz.kookie.item.Item
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.permission.DefaultPermissionNames
import be.zvz.kookie.permission.DefaultPermissions
import be.zvz.kookie.permission.PermissibleBase
import be.zvz.kookie.permission.PermissibleDelegate
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.utils.TextFormat
import be.zvz.kookie.utils.Union
import be.zvz.kookie.world.ChunkHash
import be.zvz.kookie.world.ChunkListener
import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk
import com.koloboke.collect.map.hash.HashIntIntMaps
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashLongObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import com.koloboke.collect.set.hash.HashLongSets
import com.nukkitx.protocol.bedrock.packet.TextPacket
import org.slf4j.LoggerFactory
import java.util.UUID
import kotlin.math.min
import kotlin.math.pow

open class Player(
    override var server: Server,
    val networkSession: NetworkSession,
    playerInfo: PlayerInfo,
    val authenticated: Boolean,
    spawnLocation: Location,
    namedTag: CompoundTag?,
    skin: Skin,
    location: Location
) : Human(skin, location, namedTag), CommandSender, ChunkListener, PermissibleDelegate {
    override val language: Language get() = server.language

    protected var spawned: Boolean = false
    protected val username: String = playerInfo.username
    var displayName: String = playerInfo.username
        set(value) {
            val ev = PlayerDisplayNameChangeEvent(this, displayName, value)
            ev.call()
            field = ev.newName
        }
    var xuid: String = ""
    var currentWindow: Inventory? = null
    val permanentWindows: MutableMap<Int, Inventory> = HashIntObjMaps.newMutableMap()
    lateinit var cursorInventory: PlayerCursorInventory
    lateinit var craftingGrid: PlayerCraftingInventory

    var messageCounter: Int = 2

    var firstPlayed: Long = 0
    var lastPlayed: Long = 0

    lateinit var gameMode: GameMode

    val usedChunks: MutableMap<ChunkHash, UsedChunkStatus> = HashLongObjMaps.newMutableMap()
    val activeChunkGenerationRequest: MutableMap<ChunkHash, Boolean> = HashLongObjMaps.newMutableMap()
    private val loadQueue: MutableSet<ChunkHash> = HashLongSets.newMutableSet()
    var viewDistance: Int = -1
        set(value) {
            field = server.getAllowedViewDistance(value)
            spawnThreshold = min(
                value,
                (
                    server.configGroup.getProperty("chunk-sending.spawn-radius").asLong(4).toDouble()
                        .pow(2) * Math.PI
                    ).toInt()
            )
            nextChunkOrderRun = 0
            networkSession.syncViewRadius(viewDistance)
            logger.debug("Setting view distance to $viewDistance (requested $value)")
        }
    var spawnThreshold: Int =
        (server.configGroup.getProperty("chunk-sending.spawn-radius").asLong(4).toDouble().pow(2) * Math.PI).toInt()
    var spawnChunkLoadCount: Int = 0
    var chunkSelector: ChunkSelector = ChunkSelector()
    lateinit var chunkLoader: PlayerChunkLoader

    val hiddenPlayers: MutableMap<UUID, Boolean> = HashObjObjMaps.newMutableMap()

    var moveRateLimit: Float = 10f * MOVES_PER_TICK
    var lastMovementProcess: Float? = null

    var inAirTicks: Long = 0
    override var stepHeight: Float = 0.6f

    var sleeping: Vector3? = null
    var spawnPosition: Position? = null

    var respawnLocked: Boolean = false

    var autoJump: Boolean = true
        set(value) {
            field = value
            networkSession.syncAdventureSettings(this)
        }
    var allowFlight: Boolean = false
        set(value) {
            field = value
            networkSession.syncAdventureSettings(this)
        }
    var flying: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                networkSession.syncAdventureSettings(this)
            }
        }

    var lineHeight: Int? = null
    var locale: String = "en_US"

    var startAction: Long = -1
    val usedItemsCooldown: MutableMap<Int, Long> = HashIntIntMaps.newMutableMap()

    var lastEmoteTick: Int = 0

    var formIdCounter: Long = 0
    val forms: MutableMap<Int, Form> = HashIntObjMaps.newMutableMap()

    private val logger =
        PrefixedLogger("Player: ${TextFormat.clean(username.lowercase())}", LoggerFactory.getLogger(Player::class.java))

    override val name: String get() = username
    override val permissionRecalculationCallbacks: MutableSet<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>
        get() = TODO("Not yet implemented")

    private var nextChunkOrderRun: Int = 5
    private var chunksPerTick: Int = server.configGroup.getProperty("chunk-sending.per-tick").asLong(4).toInt()

    override lateinit var perm: PermissibleBase

    init {
        val username = TextFormat.clean(username)

        xuid = if (playerInfo is XboxLivePlayerInfo) playerInfo.xuid else ""

        val rootPermissions: MutableMap<String, Boolean> = mutableMapOf(
            DefaultPermissions.ROOT_USER to true
        )
        if (server.isOp(username)) {
            rootPermissions[DefaultPermissions.ROOT_OPERATOR] = true
        }

        perm = PermissibleBase(rootPermissions)
        chunkLoader = PlayerChunkLoader(spawnLocation)
        val world = spawnLocation.world!!
        val xSpawnChunk = spawnLocation.x.toInt() shr 4
        val zSpawnChunk = spawnLocation.z.toInt() shr 4
        world.registerChunkLoader(chunkLoader, xSpawnChunk, zSpawnChunk)
        @Suppress("LeakingThis")
        world.registerChunkListener(this, xSpawnChunk, zSpawnChunk)
        usedChunks[World.chunkHash(xSpawnChunk, zSpawnChunk)] = UsedChunkStatus.NEEDED
    }

    override fun initHumanData(nbt: CompoundTag) {
        nameTag = username
    }

    override fun initEntity(nbt: CompoundTag) {
        super.initEntity(nbt)
        // TODO: addDefaultWindows()
        inventory.getListeners().add(
            CallbackInventoryListener(
                (
                    { _, slot, _ ->
                        if (slot == inventory.getHeldItemIndex()) {
                            // TODO: setUsingItem(false)
                        }
                    }
                    ),
                (
                    { inventory, oldContents ->
                        // TODO: setUsingItem(false)
                    }
                    )
            )
        )
        val now = System.currentTimeMillis() * 1000
        firstPlayed = nbt.getLong("firstPlayed", now)
        lastPlayed = nbt.getLong("lastPlayed", now)
        val gameModeTag = nbt.getTag("playerGameType")
        if (!server.forceGamemode && gameModeTag is IntTag) {
            // TODO: internalSetGameMode(GameModeIdMap.getInstance().fromId(gameModeTag.value) ?: GameMode.SURVIVAL)
        } else {
            // TODO: internalSetGameMode(server.gamemode)
        }
        keepMovement = true
        nameTagVisible = true
        alwaysShowNameTag = true
        canClimb = true

        val world = server.worldManager.getWorldByName(nbt.getString("SpawnLevel", ""))
        if (world is World) {
            spawnPosition = Position(nbt.getInt("SpawnX"), nbt.getInt("SpawnY"), nbt.getInt("SpawnZ"), world)
        }
    }

    fun getLeaveMessage(): TranslationContainer {
        return TranslationContainer(
            KnownTranslationKeys.MULTIPLAYER_PLAYER_LEFT.key,
            listOf(
                Union.U3.ofA(username)
            )
        )
    }

    fun hasPlayedBefore(): Boolean = lastPlayed - firstPlayed > 1

    override fun spawnTo(player: Player) {
        if (isAlive() && player.isAlive() && canSee(player)/* TODO: && !isSpectator()*/) {
            super.spawnTo(player)
        }
    }

    override fun getScreenLineHeight(): Int {
        return lineHeight ?: 7
    }

    override fun setScreenLineHeight(height: Int?) {
        if (height != null && height < 1) {
            throw IllegalArgumentException("Line height must be at least 1")
        }
        lineHeight = height
    }

    fun canSee(player: Player): Boolean = !hiddenPlayers.containsKey(player.uuid)

    fun hidePlayer(player: Player) {
        if (player == this) {
            return
        }
        hiddenPlayers[player.uuid] = true
        player.despawnFrom(this)
    }

    fun showPlayer(player: Player) {
        if (player == this) {
            return
        }
        hiddenPlayers.remove(player.uuid)
        if (player.isOnline()) {
            player.spawnTo(this)
        }
    }

    override fun canBeCollidedWith(): Boolean {
        return /* TODO: !isSpectator() &&*/ super.canBeCollidedWith()
    }

    override fun resetFallDistance() {
        super.resetFallDistance()
        inAirTicks = 0
    }

    fun isOnline(): Boolean {
        return isConnected()
    }

    fun isConnected(): Boolean {
        // TODO: networkSession should be null, but I hate null checks
        // This might need a new property to implement this
        return networkSession.isConnected()
    }

    fun changeSkin(skin: Skin, newSkinName: String, oldSkinName: String): Boolean {
        val ev = PlayerChangeSkinEvent(this, this.skin, skin)
        ev.call()
        if (ev.isCancelled) {
            sendSkin(listOf(this))
        }
        // TODO: setSkin(ev.newSkin)
        // TODO: sendSkin(server.onlinePlayers)
        return true
    }

    override fun sendSkin(targets: List<Player>) {
        // TODO: super.sendSkin(if (targets.isEmpty()) server.onlinePlayers else targets)
    }

    fun isUsingItem(): Boolean = startAction > -1

    fun setUsingItem(value: Boolean) {
        startAction = if (value) server.tickCounter else -1
        networkPropertiesDirty = true
    }

    fun getItemUseDuration(): Long = if (startAction == -1L) -1 else server.tickCounter - startAction

    // TODO: getItemCooldownExpiry

    fun getItemCooldownExpiry(item: Item): Long {
        checkItemCooldowns()
        return usedItemsCooldown[item.getId()] ?: -1L
    }

    fun hasItemCooldown(item: Item): Boolean {
        checkItemCooldowns()
        return usedItemsCooldown.containsKey(item.getId())
    }

    @JvmOverloads
    fun resetItemCooldown(item: Item, ticks: Long? = null) {
        val ticks = ticks ?: server.tickCounter
        if (ticks > 0) {
            usedItemsCooldown[item.getId()] = server.tickCounter + ticks
        }
    }

    protected fun checkItemCooldowns() {
        val serverTick = server.tickCounter
        val iterator = usedItemsCooldown.iterator()
        while (iterator.hasNext()) {
            val (_, cooldownUntil) = iterator.next()
            if (cooldownUntil <= serverTick) {
                iterator.remove()
            }
        }
    }

    override fun setPosition(pos: Vector3): Boolean {
        val oldWorld = if (location.isValid()) location.world else null
        if (super.setPosition(pos)) {
            val newWorld = world
            if (oldWorld != newWorld) {
                if (oldWorld != null) {
                    for (index in usedChunks.keys) {
                        val (X, Z) = World.getXZ(index)
                        unloadChunk(X, Z, oldWorld)
                    }
                }

                usedChunks.clear()
                loadQueue.clear()
                networkSession.onEnterWorld()
            }

            return true
        }
        return false
    }

    private fun unloadChunk(chunkX: Int, chunkZ: Int, world: World = this.world) {
        val chunkHash = World.chunkHash(chunkX, chunkZ)
        if (usedChunks.containsKey(chunkHash)) {
            world.getChunk(chunkX, chunkZ)?.entities?.values?.forEach {
                if (it !== this) {
                    it.despawnFrom(this)
                }
            }
            // stopUsingChunk() always empty method...Why call it?
            // TODO: networkSession.stopUsingChunk(chunkX, chunkZ)
            usedChunks.remove(chunkHash)
        }
        world.unregisterChunkLoader(chunkLoader, chunkX, chunkZ)
        world.unregisterChunkListener(this, chunkX, chunkZ)
        loadQueue.remove(chunkHash)
    }

    private fun spawnEntitiesOnAllChunks() {
        usedChunks.forEach { (chunkHash, status) ->
            if (status == UsedChunkStatus.SENT) {
                val (chunkX, chunkZ) = World.parseChunkHash(chunkHash)
                spawnEntitiesOnChunk(chunkX, chunkZ)
            }
        }
    }

    private fun spawnEntitiesOnChunk(chunkX: Int, chunkZ: Int) {
        world.getChunk(chunkX, chunkZ)?.let {
            it.entities.values.forEach { entity ->
                if (entity !== this && !entity.isFlaggedForDespawn()) {
                    entity.spawnTo(this)
                }
            }
        }
    }

    /**
     * Requests chunks from the world to be sent, up to a set limit every tick.
     * This operates on the results of the most recent chunk order.
     */
    private fun requestChunks() {
        if (!isConnected()) return

        Timings.playerChunkSend.startTiming()

        var count = 0

        loadQueue.forEach { chunkHash ->
            if (count >= chunksPerTick) {
                return@forEach
            }

            val (X, Z) = World.parseChunkHash(chunkHash)
            ++count

            usedChunks[chunkHash] = UsedChunkStatus.NEEDED
            world.registerChunkLoader(chunkLoader, X, Z, true)
            world.registerChunkListener(this, X, Z)

            val originWorld = world
            world.requestChunkPopulation(X, Z, chunkLoader).onCompletion(
                { chunk ->
                    val usedChunk = usedChunks[chunkHash]
                    if (/* TODO: !isConnected || */ usedChunk == null || world != originWorld) {
                        return@onCompletion
                    }
                    if (usedChunk != UsedChunkStatus.NEEDED) {
                        // TODO: make this an error
                        // we may have added multiple completion handlers, since the Player keeps re-requesting chunks
                        // it doesn't have yet (a relic from the old system, but also currently relied on for chunk resends).
                        // in this event, make sure we don't try to send the chunk multiple times.
                        return@onCompletion
                    }
                    loadQueue.remove(chunkHash)
                    usedChunks[chunkHash] = UsedChunkStatus.REQUESTED_SENDING

                    networkSession.startUsingChunk(X, Z) {
                        usedChunks[chunkHash] = UsedChunkStatus.SENT
                        if (spawnChunkLoadCount == -1) {
                            spawnEntitiesOnChunk(X, Z)
                        } else if (spawnChunkLoadCount++ == spawnThreshold) {
                            spawnChunkLoadCount = -1

                            spawnEntitiesOnAllChunks()

                            networkSession.notifyTerrainReady()
                        }
                    }
                },
                { }
            )
        }

        Timings.playerChunkSend.stopTiming()
    }

    private fun recheckBroadcastPermissions() {
        mapOf(
            DefaultPermissionNames.BROADCAST_ADMIN to Server.BROADCAST_CHANNEL_ADMINISTRATIVE,
            DefaultPermissionNames.BROADCAST_USER to Server.BROADCAST_CHANNEL_USERS
        ).forEach { (permission, channel) ->
            if (hasPermission(permission)) {
                server.subscribeToBroadcastChannel(channel, this)
            } else {
                server.unsubscribeFromBroadcastChannel(channel, this)
            }
        }
    }

    /**
     * Called by the network system when the pre-spawn sequence is completed (e.g. after sending spawn chunks).
     * This fires join events and broadcasts join messages to other online players.
     */
    fun doFirstSpawn() {
        if (spawned) {
            return
        }
        spawned = true
        recheckBroadcastPermissions()
        permissionRecalculationCallbacks.add { changedPermissionOldValues ->
            if (changedPermissionOldValues.containsKey(Server.BROADCAST_CHANNEL_ADMINISTRATIVE) ||
                changedPermissionOldValues.containsKey(Server.BROADCAST_CHANNEL_USERS)
            ) {
                recheckBroadcastPermissions()
            }
        }
        val ev = PlayerJoinEvent(
            this,
            TranslationContainer(KnownTranslationKeys.MULTIPLAYER_PLAYER_JOINED.key, listOf(Union.U3.ofA(displayName)))
        )
        ev.call()
        if (ev.joinMessage is TranslationContainer || ev.joinMessage is String) {
            if (ev.joinMessage is TranslationContainer) {
                server.broadcastMessage(ev.joinMessage as TranslationContainer)
            } else if (ev.joinMessage is String) {
                server.broadcastMessage(ev.joinMessage as String)
            }
        }

        noDamageTicks = 60

        spawnToAll()

        if (getHealth() <= 0) {
            logger.debug("Quit while dead, forcing respawn")
            // TODO: actuallyRespawn()
        }
    }

    /**
     * Calculates which new chunks this player needs to use, and which currently-used chunks it needs to stop using.
     * This is based on factors including the player's current render radius and current position.
     */
    private fun orderChunks() {
        if (/* TODO: !isConnected || */ viewDistance == -1) {
            return
        }

        Timings.playerChunkOrder.startTiming()

        loadQueue.clear()
        val unloadChunks = HashLongObjMaps.newMutableMap(usedChunks)

        chunkSelector.selectChunks(viewDistance, location.x.toInt() shr 4, location.z.toInt() shr 4)
            .forEach { chunkHash ->
                val status = usedChunks[chunkHash]?.equals(UsedChunkStatus.NEEDED)
                if (status === null || status == true) {
                    loadQueue.add(chunkHash)
                }
                unloadChunks.remove(chunkHash)
            }

        unloadChunks.keys.forEach { chunkHash ->
            val (chunkX, chunkZ) = World.parseChunkHash(chunkHash)
            unloadChunk(chunkX, chunkZ)
        }

        if (loadQueue.isNotEmpty() || unloadChunks.isNotEmpty()) {
            chunkLoader.currentLocation = location
            // TODO: networkSession.syncViewAreaCenterPoint(location, viewDistance)
        }

        Timings.playerChunkOrder.stopTiming()
    }

    /**
     * Returns whether the player is using the chunk with the given coordinates,
     *  irrespective of whether the chunk has been sent yet.
     */
    open fun isUsingChunk(chunkX: Int, chunkZ: Int): Boolean = usedChunks.containsKey(World.chunkHash(chunkX, chunkZ))

    /** Returns a usage status of the given chunk, or null if the player is not using the given chunk.  */
    open fun getUsedChunkStatus(chunkX: Int, chunkZ: Int): UsedChunkStatus? =
        usedChunks[World.chunkHash(chunkX, chunkZ)]

    open fun doChunkRequest() {
        if (nextChunkOrderRun != Int.MAX_VALUE && nextChunkOrderRun-- <= 0) {
            nextChunkOrderRun = Int.MAX_VALUE
            orderChunks()
        }

        if (loadQueue.isNotEmpty()) {
            requestChunks()
        }
    }

    /** Returns whether the target chunk has been sent to this player. */
    open fun hasReceivedChunk(chunkX: Int, chunkZ: Int): Boolean =
        getUsedChunkStatus(chunkX, chunkZ) == UsedChunkStatus.SENT

    fun getSpawn(): Position {
        if (hasValidSpawn()) {
            return spawnPosition!!
        }
        val world = server.worldManager.defaultWorld!!
        return world.spawnLocation
    }

    fun hasValidSpawn(): Boolean {
        return spawnPosition != null && spawnPosition!!.isValid()
    }

    // TODO: setSpawn()

    override fun sendMessage(message: String) {
        networkSession.sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.RAW
                this.message = message
            }
        )
    }

    override fun sendMessage(message: TranslationContainer) {
        networkSession.sendDataPacket(
            TextPacket().apply {
                type = TextPacket.Type.TRANSLATION
                this.message = message.text
                parameters = message.params
            }
        )
    }

    override fun onChunkChanged(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        if (hasReceivedChunk(chunkX, chunkZ)) {
            usedChunks[World.chunkHash(chunkX, chunkZ)] = UsedChunkStatus.NEEDED
            nextChunkOrderRun = 0
        }
    }

    override fun onChunkUnloaded(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        if (isUsingChunk(chunkX, chunkZ)) {
            logger.debug("Detected forced unload of chunk $chunkX $chunkZ")
            unloadChunk(chunkX, chunkZ)
        }
    }

    companion object {
        @JvmStatic
        fun isValidUserName(name: String?): Boolean {
            if (name == null) {
                return false
            }
            val lname = name.lowercase()
            val len = name.length
            return lname != "rcon" && lname != "console" && len in 1..16 && name.matches(Regex("[a-zA-Z0-9_ ]+"))
        }

        @JvmStatic
        private fun populateInventoryFromListTag(inventory: Inventory, items: Map<Int, Item>) {
            val listeners = inventory.getListeners().toTypedArray()
            inventory.getListeners().clear()
            inventory.setContents(items)
            inventory.getListeners().addAll(listeners)
        }

        private const val MOVES_PER_TICK = 2
        private const val MOVE_BACKLOG_SIZE = 100 * MOVES_PER_TICK; // 100 ticks backlog (5 seconds)
    }
}
