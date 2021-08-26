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
package be.zvz.kookie.world

import be.zvz.kookie.Server
import be.zvz.kookie.block.Air
import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.block.UnknownBlock
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.block.tile.Spawnable
import be.zvz.kookie.block.tile.Tile
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.block.BlockBreakEvent
import be.zvz.kookie.event.block.BlockPlaceEvent
import be.zvz.kookie.event.block.BlockUpdateEvent
import be.zvz.kookie.event.player.PlayerInteractEvent
import be.zvz.kookie.event.world.ChunkLoadEvent
import be.zvz.kookie.event.world.ChunkPopulateEvent
import be.zvz.kookie.event.world.ChunkUnloadEvent
import be.zvz.kookie.event.world.SpawnChangeEvent
import be.zvz.kookie.event.world.WorldSaveEvent
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.item.ItemUseResult
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Morton2D
import be.zvz.kookie.math.Morton3D
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.BlockActorDataPacket
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket
import be.zvz.kookie.network.mcpe.protocol.UpdateBlockPacket
import be.zvz.kookie.player.Player
import be.zvz.kookie.scheduler.AsyncPool
import be.zvz.kookie.scheduler.AsyncTask
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.utils.Promise
import be.zvz.kookie.utils.inline.repeat2
import be.zvz.kookie.world.biome.Biome
import be.zvz.kookie.world.biome.BiomeIds
import be.zvz.kookie.world.biome.BiomeRegistry
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.io.WritableWorldProvider
import be.zvz.kookie.world.format.io.exception.CorruptedChunkException
import be.zvz.kookie.world.generator.Generator
import be.zvz.kookie.world.generator.GeneratorManager
import be.zvz.kookie.world.light.BlockLightUpdate
import be.zvz.kookie.world.light.SkyLightUpdate
import be.zvz.kookie.world.particle.BlockBreakParticle
import be.zvz.kookie.world.particle.Particle
import be.zvz.kookie.world.sound.BlockPlaceSound
import be.zvz.kookie.world.sound.Sound
import be.zvz.kookie.world.utils.SubChunkExplorer
import com.koloboke.collect.map.hash.HashLongLongMaps
import com.koloboke.collect.map.hash.HashLongObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps
import com.koloboke.collect.set.hash.HashIntSets
import com.koloboke.collect.set.hash.HashLongSets
import com.koloboke.collect.set.hash.HashObjSets
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random
import kotlin.reflect.KClass

typealias EntityId = Long
typealias ChunkHash = Long
typealias BlockHash = Long
typealias ChunkBlockHash = Long

class World(
    val server: Server,
    val folderName: String,
    private val provider: WritableWorldProvider,
    private val workerPool: AsyncPool
) :
    ChunkManager {
    val players: MutableMap<EntityId, Player> = HashLongObjMaps.newMutableMap()
    val entities: MutableMap<EntityId, Entity> = HashLongObjMaps.newMutableMap()

    private val entityLastKnownPositions: MutableMap<EntityId, Vector3> = HashLongObjMaps.newMutableMap()

    val updateEntities: MutableMap<EntityId, Entity> = HashLongObjMaps.newMutableMap()
    private var blockCache: MutableMap<ChunkHash, MutableMap<BlockHash, Block>> = HashLongObjMaps.newMutableMap()

    private var sendTimeTicker: Int = 0

    val id: Int = worldIdCounter++

    private var providerGarbageCollectionTicker: Int = 0

    override val minY: Int = provider.worldMinY
    override val maxY: Int = provider.worldMaxY
    var difficulty: Int
        get() = provider.worldData.difficulty
        set(value) {
            if (value < 0 || value > 3) {
                throw IllegalArgumentException("Invalid difficulty level $value")
            }
            provider.worldData.difficulty = value
            players.values.forEach {
                /** TODO: Implements after implemented NetworkSession::syncWorldDifficulty
                 * it.networkSession.syncWorldDifficulty(value)
                 */
            }
        }

    private val tickingLoaders: MutableMap<TickingChunkLoader, Int> = HashObjIntMaps.newMutableMap()
    private val chunkLoaders: MutableMap<ChunkHash, MutableSet<ChunkLoader>> = HashLongObjMaps.newMutableMap()

    private val chunkListeners: MutableMap<ChunkHash, MutableSet<ChunkListener>> = HashLongObjMaps.newMutableMap()
    private val playerChunkListeners: MutableMap<ChunkHash, MutableSet<Player>> = HashLongObjMaps.newMutableMap()

    private var packetBuffersByChunk: MutableMap<ChunkHash, MutableList<ClientboundPacket>> = HashLongObjMaps.newMutableMap()
    private var unloadQueue: MutableMap<ChunkHash, Long> = HashLongLongMaps.newMutableMap()

    var time: Long = provider.worldData.time
        set(value) {
            field = value
            sendTime()
        }
    val timeOfDay: Long get() = time % TIME_FULL

    private var stopTime: Boolean = false

    var sunAnglePercentage: Double = 0.0
        private set(value) {
            field = value
            sunAngleRadians = value * 2 * Math.PI
            sunAngleDegrees = value * 360.0
        }
    var sunAngleRadians: Double = 0.0
        private set
    var sunAngleDegrees: Double = 0.0
        private set

    var skyLightReduction: Int = 0
        private set

    val displayName: String = provider.worldData.name

    val chunks: MutableMap<ChunkHash, Chunk> = HashLongObjMaps.newMutableMap()
    private var changedBlocks: MutableMap<ChunkHash, MutableMap<BlockHash, Vector3>> = HashLongObjMaps.newMutableMap()

    private val scheduledBlockUpdateQueue: PriorityQueue<Pair<Long, Vector3>> = PriorityQueue { first, second ->
        -(first.first - second.first).toInt()
    }
    private val scheduledBlockUpdateQueueIndex: MutableMap<BlockHash, Long> = HashLongLongMaps.newMutableMap()

    private val neighbourBlockUpdateQueue: Queue<ChunkHash> = LinkedList()

    private val activeChunkPopulationTasks: MutableSet<ChunkHash> = HashLongSets.newMutableSet()
    private val chunkLock: MutableSet<ChunkHash> = HashLongSets.newMutableSet()
    private val maxConcurrentChunkPopulationTasks: Int =
        server.configGroup.getProperty("chunk-generation.population-queue-size").asLong(2).toInt()
    private var chunkPopulationRequestMap: MutableMap<ChunkHash, Promise<Chunk>> = HashLongObjMaps.newMutableMap()
    private var chunkPopulationRequestQueue: Queue<ChunkHash> = LinkedList()

    var autoSave: Boolean = true

    var sleepTicks: Int = 0

    val chunkTickRadius: Int =
        min(
            max(2, server.getViewDistance()),
            max(1, server.configGroup.getProperty("chunk-ticking.tick-radius").asLong(4).toInt())
        )
    val chunksPerTick: Int = server.configGroup.getProperty("chunk-ticking.per-tick").asLong(40).toInt()
    val tickedBlocksPerSubchunkPerTick: Int = server.configGroup.getProperty("chunk-ticking.blocks-per-subchunk-per-tick")
        .asLong(DEFAULT_TICKED_BLOCKS_PER_SUBCHUNK_PER_TICK.toLong()).toInt()
    private val randomTickBlocks: MutableSet<Long> = HashLongSets.newMutableSet()

    val timings = WorldTimings(this)
    var tickRateTime: Float = 0f
    var doingTick: Boolean = false
        private set

    private val generator: KClass<out Generator> = GeneratorManager.getGenerator(provider.worldData.generatorName, true)

    var unloaded: Boolean = false
        private set
    val unloadCallbacks: MutableSet<() -> Unit> = HashObjSets.newMutableSet()

    private var blockLightUpdate: BlockLightUpdate? = null
    private var skyLightUpdate: SkyLightUpdate? = null

    val logger: Logger = LoggerFactory.getLogger("World: $displayName")

    /** state property for [World.getBlockAt] method */
    private var dynamicStateRead: Boolean = false

    /** Position pointing to the spawn */
    var spawnLocation: Position = Position()
        get() = Position.fromObject(provider.worldData.spawn, this)
        set(value) {
            val previousSpawn = field
            provider.worldData.spawn = value
            (SpawnChangeEvent(this, previousSpawn)).call()
        }

    val randomTickedBlocks: List<Long> get() = randomTickBlocks.toList()

    init {
        logger.info(server.language.translateString("pocketmine.level.preparing", listOf(displayName)))
        addOnUnloadCallback {
            logger.debug("Cancelling unfulfilled generation requests")
            chunkPopulationRequestMap.values.forEach { it.reject() }
            chunkPopulationRequestMap.clear()
        }

        val dontTickBlocks: MutableSet<Int> = HashIntSets.newMutableSet()
        server.configGroup.getProperty("chunk-ticking.disable-block-ticking")
            .toMap<Int, String>().keys.forEach(dontTickBlocks::add)

        BlockFactory.getAllKnownStates().forEach { (_, state) ->
            if (dontTickBlocks.contains(state.getId()) && state.ticksRandomly()) {
                randomTickBlocks.add(state.getFullId())
            }
        }
    }

    @Deprecated("Replace 'isLoaded()' with negated 'unloaded'", ReplaceWith("!unloaded"))
    fun isLoaded(): Boolean = !unloaded

    internal fun onUnload() {
        if (unloaded) {
            throw IllegalStateException("Tried to unload a world which is already unloaded")
        }

        unloadCallbacks.forEach { it() }
        unloadCallbacks.clear()

        chunks.keys.forEach { chunkHash ->
            val (chunkX, chunkZ) = parseChunkHash(chunkHash)
            unloadChunk(chunkX, chunkZ, false)
        }

        save()

        provider.close()
        blockCache.clear()

        unloaded = true
    }

    fun addOnUnloadCallback(callback: () -> Unit) = unloadCallbacks.add(callback)
    fun removeOnUnloadCallback(callback: () -> Unit) = unloadCallbacks.remove(callback)

    @JvmOverloads
    fun addSound(pos: Vector3, sound: Sound, players: List<Player>? = null) {
        sound.encode(pos).takeIf(List<ClientboundPacket>::isNotEmpty)?.let {
            if (players === null) {
                it.forEach { pk ->
                    broadcastPacketToViewers(pos, pk)
                }
            } else {
                /** TODO: Implements after implemented Server::broadcastPackets()
                 * server.broadcastPackets(players, it)
                 */
            }
        }
    }

    @JvmOverloads
    fun addParticle(pos: Vector3, particle: Particle, players: List<Player>? = null) {
        particle.encode(pos).takeIf(List<ClientboundPacket>::isNotEmpty)?.let {
            if (players === null) {
                it.forEach { pk ->
                    broadcastPacketToViewers(pos, pk)
                }
            } else {
                /** TODO: Implements after implemented Server::broadcastPackets()
                 * server.broadcastPackets(players, it)
                 */
            }
        }
    }

    /** @see getChunkViewers */
    @Deprecated("It has problem with the naming", ReplaceWith("getChunkViewers(chunkX, chunkZ)"))
    fun getChunkPlayers(chunkX: Int, chunkZ: Int): Set<Player> = getChunkViewers(chunkX, chunkZ)

    /** @see getChunkViewers */
    @Deprecated("It has problem with the naming", ReplaceWith("getChunkViewers(pos)"))
    fun getViewersForPosition(pos: Vector3): Set<Player> = getChunkViewers(pos)

    fun getChunkViewers(chunkX: Int, chunkZ: Int): Set<Player> = playerChunkListeners[chunkHash(chunkX, chunkZ)] ?: setOf()

    fun getChunkViewers(pos: Vector3): Set<Player> = playerChunkListeners[chunkHash(pos)] ?: setOf()

    fun getChunkLoaders(chunkX: Int, chunkZ: Int): Set<ChunkLoader> =
        chunkLoaders[chunkHash(chunkX, chunkZ)] ?: setOf()

    /** @see broadcastPacketToViewers */
    @Deprecated("It has problem with the naming", ReplaceWith("broadcastPacketToViewers(chunkX, chunkZ)"))
    fun broadcastPacketToPlayersUsingChunk(chunkX: Int, chunkZ: Int, packet: ClientboundPacket) {
        broadcastPacketToViewers(chunkX, chunkZ, packet)
    }

    fun broadcastPacketToViewers(pos: Vector3, packet: ClientboundPacket) {
        packetBuffersByChunk.getOrPut(chunkHash(pos), ::mutableListOf).add(packet)
    }

    fun broadcastPacketToViewers(chunkX: Int, chunkZ: Int, packet: ClientboundPacket) {
        packetBuffersByChunk.getOrPut(chunkHash(chunkX, chunkZ), ::mutableListOf).add(packet)
    }

    @JvmOverloads
    fun registerChunkLoader(loader: ChunkLoader, chunkX: Int, chunkZ: Int, autoLoad: Boolean = true) {
        if (chunkLoaders.getOrPut(chunkHash(chunkX, chunkZ), HashObjSets::newMutableSet).add(loader)) {
            if (loader is TickingChunkLoader) {
                tickingLoaders[loader] = tickingLoaders.getOrDefault(loader, 0) + 1
            }
            cancelUnloadChunkRequest(chunkX, chunkZ)

            if (autoLoad) {
                loadChunk(chunkX, chunkZ)
            }
        }
    }

    fun unregisterChunkLoader(loader: ChunkLoader, chunkX: Int, chunkZ: Int) {
        val chunkHash = chunkHash(chunkX, chunkZ)
        chunkLoaders[chunkHash]?.let { loaders ->
            if (loaders.remove(loader) && loaders.isEmpty()) {
                unloadChunkRequest(chunkX, chunkZ, true)
                chunkPopulationRequestMap[chunkHash]
                    ?.takeIf { !activeChunkPopulationTasks.contains(chunkHash) }
                    ?.let {
                        it.reject()
                        chunkPopulationRequestMap.remove(chunkHash)
                    }
            }
        }
        if (loader is TickingChunkLoader) {
            tickingLoaders[loader]?.let { count ->
                if (count <= 1) {
                    tickingLoaders.remove(loader)
                } else {
                    tickingLoaders[loader] = count - 1
                }
            }
        }
    }

    /** Registers a listener to receive events on a chunk. */
    fun registerChunkListener(listener: ChunkListener, chunkX: Int, chunkZ: Int) {
        val chunkHash = chunkHash(chunkX, chunkZ)
        chunkListeners.getOrPut(chunkHash, HashObjSets::newMutableSet).add(listener)
        if (listener is Player) {
            playerChunkListeners.getOrPut(chunkHash, HashObjSets::newMutableSet).add(listener)
        }
    }

    fun unregisterChunkListener(listener: ChunkListener, chunkX: Int, chunkZ: Int) {
        val chunkHash = chunkHash(chunkX, chunkZ)
        chunkListeners[chunkHash]?.let {
            if (it.remove(listener) && it.isEmpty()) {
                chunkListeners.remove(chunkHash)
            }
        }
        if (listener is Player) {
            playerChunkListeners[chunkHash]?.let {
                it.remove(listener)
                if (it.isEmpty()) {
                    playerChunkListeners.remove(chunkHash)
                }
            }
        }
    }

    /** Unregisters a chunk listener from all chunks it is listening on in this World. */
    fun unregisterChunkListenerFromAll(listener: ChunkListener) {
        chunkListeners.values.iterator().let {
            while (it.hasNext()) {
                val listeners = it.next()
                if (listeners.remove(listener) && listeners.isEmpty()) {
                    it.remove()
                }
            }
        }

        playerChunkListeners.values.iterator().let {
            while (it.hasNext()) {
                val listeners = it.next()
                if (listeners.remove(listener) && listeners.isEmpty()) {
                    it.remove()
                }
            }
        }
    }

    /** Returns all the listeners attached to this chunk. */
    fun getChunkListeners(chunkX: Int, chunkZ: Int): Set<ChunkListener> = chunkListeners[chunkHash(chunkX, chunkZ)] ?: setOf()

    internal fun sendTime() {
        players.forEach {
            /** TODO: Implements after implemented NetworkSession::syncWorldTime
             * it.networkSession.syncWorldTime(time)
             */
        }
    }

    internal fun doTick(currentTick: Long) {
        if (unloaded) {
            throw IllegalStateException("Attempted to tick a world which has been closed")
        }

        timings.doTick.startTiming()
        doingTick = true
        try {
            actuallyDoTick(currentTick)
        } finally {
            doingTick = false
            timings.doTick.stopTiming()
        }
    }

    private fun actuallyDoTick(currentTick: Long) {
        if (!stopTime) {
            // this simulates an overflow, as would happen in any language which doesn't do stupid things to var types
            if (time == Long.MAX_VALUE) {
                time = Long.MIN_VALUE
            } else {
                ++time
            }
        }

        sunAnglePercentage = computeSunAnglePercentage() // Sun angle depends on the current time
        skyLightReduction = computeSkyLightReduction() // Sky light reduction depends on the sun angle

        if (++sendTimeTicker == 200) {
            sendTime()
            sendTimeTicker = 0
        }

        unloadChunks()
        if (++providerGarbageCollectionTicker >= 6000) {
            provider.doGarbageCollection()
            providerGarbageCollectionTicker = 0
        }

        // Do block updates
        timings.scheduledBlockUpdates.startTiming()

        // Delayed updates
        while (scheduledBlockUpdateQueue.isNotEmpty() && scheduledBlockUpdateQueue.peek().first <= currentTick) {
            val vec = scheduledBlockUpdateQueue.poll().second
            if (isInLoadedTerrain(vec)) {
                getBlock(vec).onScheduledUpdate()
            }
        }

        // Normal updates
        while (neighbourBlockUpdateQueue.isNotEmpty()) {
            val blockHash = neighbourBlockUpdateQueue.poll()
            val (x, y, z) = parseBlockHash(blockHash)
            if (!isChunkLoaded(x shr 4, z shr 4)) {
                continue
            }

            val block = getBlockAt(x, y, z)
            block.readStateFromWorld() // for blocks like fences, force recalculation of connected AABBs

            val ev = BlockUpdateEvent(block)
            ev.call()
            if (!ev.isCancelled) {

                getNearbyEntities(AxisAlignedBB.one().offset(x, y, z)).forEach(Entity::onNearByBlockChange)
                block.onNearbyBlockChange()
            }
        }

        timings.scheduledBlockUpdates.stopTiming()

        Timings.tickEntity.startTiming()
        timings.entityTick.startTiming()
        // Update entities that need update
        updateEntities.iterator().let {
            while (it.hasNext()) {
                val entity = it.next().value
                if (entity.isClosed() || !entity.onUpdate(currentTick)) {
                    it.remove()
                } else if (entity.isFlaggedForDespawn()) {
                    entity.close()
                }
            }
        }
        timings.entityTick.stopTiming()
        Timings.tickEntity.stopTiming()

        timings.randomChunkUpdates.startTiming()
        tickChunks()
        timings.randomChunkUpdates.stopTiming()

        executeQueuedLightUpdates()
        if (players.isNotEmpty()) {
            changedBlocks.forEach { (chunkHash, blocks) ->
                if (blocks.isNotEmpty()) {
                    val (chunkX, chunkZ) = parseChunkHash(chunkHash)
                    if (blocks.size > 512) {
                        val chunk =
                            getChunk(chunkX, chunkZ) ?: throw IllegalArgumentException("Chunk $chunkX $chunkZ does not exist")
                        getChunkViewers(chunkX, chunkZ).forEach { it.onChunkChanged(chunkX, chunkZ, chunk) }
                    } else {
                        createBlockUpdatePackets(blocks.values.toList()).forEach {
                            TODO("broadcastPacketToPlayers(chunkX, chunkZ, it) ")
                        }
                    }
                }
            }
        }
        changedBlocks.clear()

        if (sleepTicks > 0 && --sleepTicks <= 0) {
            checkSleep()
        }

        packetBuffersByChunk.forEach { (chunkHash, packets) ->
            val (chunkX, chunkZ) = parseChunkHash(chunkHash)
            getChunkViewers(chunkX, chunkZ).takeIf(Set<Player>::isNotEmpty)?.let {
                TODO("server.broadcastPackets(it, packets)")
            }
        }
        packetBuffersByChunk.clear()
    }

    fun checkSleep() {
        if (players.values.find { TODO("!it.isSleeping()") } === null) {
            val dayTime = timeOfDay

            if (dayTime in TIME_NIGHT until TIME_SUNRISE) {
                time = time + TIME_FULL - dayTime

                players.forEach { TODO("it.stopSleep()") }
            }
        }
    }

    fun createBlockUpdatePackets(blocks: List<Vector3>): List<ClientboundPacket> {
        val packets = mutableListOf<ClientboundPacket>()

        blocks.forEach { vec ->
            val fullBlock = getBlockAt(vec.floorX, vec.floorY, vec.floorZ)
            packets.add(
                UpdateBlockPacket().apply {
                    pos.x = vec.floorX
                    pos.y = vec.floorY
                    pos.z = vec.floorZ
                    blockRuntimeId = 0 // TODO: RuntimeBlockMapping.getBlockRuntimeId(fullBlock.getFullId())
                }
            )

            val tile = getTileAt(vec.floorX, vec.floorY, vec.floorZ)
            if (tile is Spawnable) {
                packets.add(
                    BlockActorDataPacket.create(
                        vec.floorX,
                        vec.floorY,
                        vec.floorZ,
                        tile.serializedSpawnCompound
                    )
                )
            }
        }

        return packets
    }

    @JvmOverloads
    fun clearCache(force: Boolean = false) {
        if (force) {
            blockCache.clear()
        } else {
            var count = 0
            blockCache.values.forEach {
                count += it.size
                if (count > 2048) {
                    blockCache.clear()
                    return
                }
            }
        }
    }

    fun addRandomTickedBlock(block: Block) {
        if (block is UnknownBlock) {
            throw IllegalArgumentException("Cannot do random-tick on unknown block")
        }
        randomTickBlocks.add(block.getFullId())
    }

    fun removeRandomTickedBlock(block: Block) {
        randomTickBlocks.remove(block.getFullId())
    }

    private fun tickChunks() {
        if (chunksPerTick <= 0 || tickingLoaders.isEmpty()) {
            return
        }

        timings.randomChunkUpdatesChunkSelection.startTiming()

        /** @var bool[] chunkTickList chunkhash => dummy */
        val chunkTickList: MutableSet<Long> = HashObjSets.newMutableSet()

        val chunksPerLoader = min(200, max(1, (((chunksPerTick - tickingLoaders.size) / tickingLoaders.size) + 0.5).toInt()))
        val randRange = min(3 + chunksPerLoader / 30, chunkTickRadius)

        tickingLoaders.keys.forEach { loader ->
            val chunkX = loader.x shr 4
            val chunkZ = loader.z shr 4

            repeat(chunksPerLoader) {
                val dx = Random.nextInt(-randRange, randRange)
                val dz = Random.nextInt(-randRange, randRange)
                val hash = chunkHash(dx + chunkX, dz + chunkZ)
                if (!chunkTickList.contains(hash) && chunks.contains(hash) && isChunkTickable(dx + chunkX, dz + chunkZ)) {
                    chunkTickList.add(hash)
                }
            }
        }

        timings.randomChunkUpdatesChunkSelection.stopTiming()

        chunkTickList.forEach { chunkHash ->
            val (chunkX, chunkZ) = parseChunkHash(chunkHash)
            tickChunk(chunkX, chunkZ)
        }
    }

    private fun isChunkTickable(chunkX: Int, chunkZ: Int): Boolean {
        repeat2(3) { xx, zz ->
            val cX = xx + chunkX - 1
            val cZ = zz + chunkZ - 1

            if (isChunkLocked(cX, cZ) || getChunk(cX, cZ)?.terrainPopulated != true) {
                return false
            }
            val lightPopulatedState = getChunk(cX, cZ)?.lightPopulated
            if (lightPopulatedState != true) {
                if (lightPopulatedState == false) {
                    orderLightPopulation(cX, cZ)
                }
                return false
            }
        }

        return true
    }

    private fun orderLightPopulation(chunkX: Int, chunkZ: Int) {
        val clonedChunk =
            getChunk(chunkX, chunkZ)?.clone() ?: throw IllegalArgumentException("Chunk $chunkX $chunkZ does not exist")
        val lightPopulatedState = clonedChunk.lightPopulated
        if (lightPopulatedState != false) {
            return
        }

        clonedChunk.lightPopulated = null

        workerPool.submit(
            AsyncTask(
                {
                    val manager = SimpleChunkManager(Y_MIN, Y_MAX)
                    manager.setChunk(0, 0, clonedChunk)
                    mapOf(
                        "Block" to BlockLightUpdate(SubChunkExplorer(manager), BlockFactory.lightFilter, BlockFactory.light),
                        "Sky" to SkyLightUpdate(
                            SubChunkExplorer(manager),
                            BlockFactory.lightFilter,
                            BlockFactory.blocksDirectSkyLight
                        )
                    ).values.forEach { update ->
                        update.recalculateChunk(0, 0)
                        update.execute()
                    }

                    clonedChunk.lightPopulated = null

                    val chunk = getChunk(chunkX, chunkZ)
                    if (unloaded || chunk === null || chunk.lightPopulated == true) {
                        return@AsyncTask
                    }
                    // TODO: calculated light information might not be valid if the terrain changed during light calculation

                    clonedChunk.heightMap.let {
                        chunk.heightMap = it
                    }
                    clonedChunk.subChunks.forEachIndexed { y, subChunk ->
                        chunk.getSubChunk(y).skyLight = subChunk.skyLight
                        chunk.getSubChunk(y).blockLight = subChunk.blockLight
                    }
                    chunk.lightPopulated = true
                },
                Unit
            )
        )
    }

    private fun tickChunk(chunkX: Int, chunkZ: Int) {
        val chunk = getChunk(chunkX, chunkZ) ?: throw IllegalArgumentException("Chunk is not loaded")
        chunk.entities.values.forEach { it.onRandomUpdate() }
        chunk.subChunks.forEachIndexed { subChunkY, subChunk ->
            if (subChunk.isEmptyFast()) {
                return@forEachIndexed
            }
            var k = 0
            repeat(tickedBlocksPerSubchunkPerTick) { i ->
                if (i % 5 == 0) {
                    // 60 bits will be used by 5 blocks (12 bits each)
                    k = Random.nextInt(0, (1 shl 60) - 1)
                }
                val x = k and 0x0f
                val y = (k shr 4) and 0x0f
                val z = (k shr 8) and 0x0f
                k = k shr 12

                val state = subChunk.getFullBlock(x, y, z)
                if (randomTickBlocks.contains(state)) {
                    val block = BlockFactory.fromFullBlock(state)
                    block.position(this, chunkX * 16 + x, subChunkY shl 4 + y, chunkZ * 16 + z)
                    block.onRandomTick()
                }
            }
        }
    }

    @JvmOverloads
    fun save(force: Boolean = false): Boolean {
        if (autoSave && !force) {
            return false
        }

        WorldSaveEvent(this).call()

        provider.worldData.time = time
        saveChunks()
        provider.worldData.save()

        return true
    }

    fun saveChunks() {
        timings.syncChunkSave.startTiming()
        try {
            chunks.forEach { (chunkHash, chunk) ->
                if (chunk.isDirty) {
                    val (chunkX, chunkZ) = parseChunkHash(chunkHash)
                    provider.saveChunk(chunkX, chunkZ, chunk)
                    chunk.clearDirtyFlags()
                }
            }
        } finally {
            timings.syncChunkSave.stopTiming()
        }
    }

    /**
     * Schedules a block update to be executed after the specified number of ticks.
     * Blocks will be updated with the scheduled update type.
     */
    fun scheduleDelayedBlockUpdate(pos: Vector3, delay: Long) {
        if (isInWorld(pos)) {
            val blockHash = blockHash(pos)
            val queueDelay = scheduledBlockUpdateQueueIndex[blockHash]
            if (queueDelay !== null && queueDelay <= delay) {
                return
            }

            scheduledBlockUpdateQueueIndex[blockHash] = delay
            scheduledBlockUpdateQueue.add(
                Pair(
                    delay
                    /** TODO: server.getTick() */,
                    pos.floor()
                )
            )
        }
    }

    private fun tryAddToNeighbourUpdateQueue(pos: Vector3) {
        if (isInWorld(pos)) {
            neighbourBlockUpdateQueue.add(blockHash(pos))
        }
    }

    @JvmOverloads
    fun getCollisionBlocks(bb: AxisAlignedBB, targetFirst: Boolean = false): List<Block> {
        val minX = (bb.minX - 1).toInt()
        val minY = (bb.minY - 1).toInt()
        val minZ = (bb.minZ - 1).toInt()
        val maxX = (bb.maxX + 1).toInt()
        val maxY = (bb.maxY + 1).toInt()
        val maxZ = (bb.maxZ + 1).toInt()

        val result: MutableList<Block> = mutableListOf()
        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val block = getBlockAt(x, y, z)
                    if (block.collidesWithBB(bb)) {
                        if (targetFirst) {
                            return listOf(block)
                        } else {
                            result.add(block)
                        }
                    }
                }
            }
        }

        return result
    }

    @JvmOverloads
    fun getCollisionBoxes(entity: Entity, bb: AxisAlignedBB, entities: Boolean = false): List<AxisAlignedBB> {
        val minX = (bb.minX - 1).toInt()
        val minY = (bb.minY - 1).toInt()
        val minZ = (bb.minZ - 1).toInt()
        val maxX = (bb.maxX + 1).toInt()
        val maxY = (bb.maxY + 1).toInt()
        val maxZ = (bb.maxZ + 1).toInt()

        val result: MutableList<AxisAlignedBB> = mutableListOf()
        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val block = getBlockAt(x, y, z)
                    /** TODO: Implements after implemented Block::getCollisionBoxes()
                     * block.getCollisionBoxes().forEach{blockBB ->
                     *     if(blockBB.intersectsWith(bb)){
                     *         result.add(blockBB)
                     *     }
                     * }
                     */
                }
            }
        }

        if (entities) {
            getCollidingEntities(bb.expandedCopy(0.25, 0.25, 0.25), entity).forEach { ent ->
                result.add(ent.boundingBox.clone())
            }
        }

        return result
    }

    /**
     * Computes the percentage of a circle away from noon the sun is currently at.
     * This can be multiplied by 2 * Math.PI to get an angle in radians, or by 360 to get an angle in degrees.
     */
    fun computeSunAnglePercentage(): Double {
        val timeProgress = (time.toDouble() % 24000) / 24000

        // 0.0 needs to be high noon, not dusk
        val sunProgress = timeProgress + if (timeProgress < 0.25) {
            0.75
        } else {
            -0.25
        }

        // Offset the sun progress to be above the horizon longer at dusk and dawn
        // this is roughly an inverted sine curve, which pushes the sun progress back at dusk and forwards at dawn
        val diff = (((1 - ((cos(sunProgress * Math.PI) + 1) / 2)) - sunProgress) / 3)

        return sunProgress + diff
    }

    /**
     * Computes how many points of sky light is subtracted based on the current time.
     * Used to offset raw chunk sky light to get a real light value.
     */
    fun computeSkyLightReduction(): Int {
        val percentage = max(0.0, min(1.0, -(cos(sunAngleRadians) * 2 - 0.5)))

        // TODO: check rain and thunder level
        return (percentage * 11).toInt()
    }

    /**
     * Returns the highest available level of any type of light at the given coordinates,
     * adjusted for the current weather and time of day.
     */
    fun getFullLight(pos: Vector3): Int = getFullLight(pos.floorX, pos.floorY, pos.floorZ)
    fun getFullLight(x: Int, y: Int, z: Int): Int {
        val skyLight = getRealBlockSkyLightAt(x, y, z)
        return if (skyLight < 15) {
            max(skyLight, getBlockLightAt(0xf, y, z))
        } else {
            skyLight
        }
    }

    /**
     * Returns the highest available level of any type of light at, or adjacent to, the given coordinates, adjusted for
     * the current weather and time of day.
     */
    fun getHighestAdjacentFullLightAt(x: Int, y: Int, z: Int): Int {
        return getHighestAdjacentLight(x, y, z, this::getFullLight)
    }

    /**
     * Returns the highest potential level of sky light at the target coordinates, regardless of the time of day or
     * weather conditions.
     * You usually don't want to use this for vanilla gameplay logic; prefer the real sky light instead.
     * @see World::getRealBlockSkyLightAt()
     *
     * @return int 0-15
     */
    fun getPotentialBlockSkyLightAt(x: Int, y: Int, z: Int): Int =
        if (!isInWorld(x, y, z)) {
            if (y >= Y_MAX) 15 else 0
        } else {
            getBlockLightAt(x, y, z)
        }

    /**
     * Returns the sky light level at the specified coordinates, offset by the current time and weather.
     *
     * @return int 0-15
     */
    fun getRealBlockSkyLightAt(x: Int, y: Int, z: Int): Int =
        max(0, getPotentialBlockSkyLightAt(x, y, z) - skyLightReduction)

    /**
     * Gets the raw block light level
     *
     * @return int 0-15
     */
    fun getBlockLightAt(x: Int, y: Int, z: Int): Int {
        if (!isInWorld(x, y, z)) {
            return 0
        }
        val chunk = getChunk(x shr 4, z shr 4)
        if (chunk?.lightPopulated == true) {
            return chunk.getSubChunk(y shr 4).skyLight.get(x and 0x0f, y and 0x0f, z and 0x0f)
        }
        return 0 // TODO: this should probably throw instead (light not calculated yet)
    }

    fun updateAllLight(x: Int, y: Int, z: Int) {
        val chunk = getChunk(x shr 4, z shr 4)
        if (chunk?.lightPopulated != true) {
            logger.debug(
                "Skipped runtime light update of x=$x,y=$y,z=$z, " +
                    "because the target area has not received base light calculation"
            )
            return
        }

        timings.doBlockSkyLightUpdates.startTiming()
        if (skyLightUpdate === null) {
            skyLightUpdate = SkyLightUpdate(SubChunkExplorer(this), BlockFactory.lightFilter, BlockFactory.blocksDirectSkyLight)
        }
        skyLightUpdate?.recalculateNode(x, y, z)
        timings.doBlockSkyLightUpdates.stopTiming()

        timings.doBlockLightUpdates.startTiming()
        if (blockLightUpdate === null) {
            blockLightUpdate = BlockLightUpdate(SubChunkExplorer(this), BlockFactory.lightFilter, BlockFactory.light)
        }
        blockLightUpdate?.recalculateNode(x, y, z)
        timings.doBlockLightUpdates.stopTiming()
    }

    private inline fun getHighestAdjacentLight(x: Int, y: Int, z: Int, lightGetter: (Int, Int, Int) -> Int): Int {
        var max = 0
        listOf(
            Triple(x + 1, y, z),
            Triple(x - 1, y, z),
            Triple(x, y + 1, z),
            Triple(x, y - 1, z),
            Triple(x, y, z + 1),
            Triple(x, y, z - 1)
        ).forEach { (x1, y1, z1) ->
            if (!isInWorld(x1, y1, z1)) {
                return@forEach
            }
            val chunk = getChunk(x1 shr 4, z1 shr 4)
            if (chunk?.lightPopulated != true) {
                return@forEach
            }
            max = max(max, lightGetter(x1, y1, z1))
        }
        return max
    }

    /**
     * Returns the highest potential level of sky light in the positions adjacent to the specified block coordinates.
     */
    fun getHighestAdjacentPotentialBlockSkyLight(x: Int, y: Int, z: Int): Int =
        getHighestAdjacentLight(x, y, z, this::getPotentialBlockSkyLightAt)

    /**
     * Returns the highest block sky light available in the positions adjacent to the given coordinates, adjusted for
     * the world's current time of day and weather conditions.
     */
    fun getHighestAdjacentRealBlockSkyLight(x: Int, y: Int, z: Int): Int =
        getHighestAdjacentPotentialBlockSkyLight(x, y, z) - skyLightReduction

    /**
     * Returns the highest block light level available in the positions adjacent to the specified block coordinates.
     */
    fun getHighestAdjacentBlockLight(x: Int, y: Int, z: Int): Int =
        getHighestAdjacentLight(x, y, z, this::getBlockLightAt)

    private fun executeQueuedLightUpdates() {
        blockLightUpdate?.let {
            timings.doBlockLightUpdates.startTiming()
            it.execute()
            blockLightUpdate = null
            timings.doBlockLightUpdates.stopTiming()
        }
        skyLightUpdate?.let {
            timings.doBlockSkyLightUpdates.startTiming()
            it.execute()
            skyLightUpdate = null
            timings.doBlockSkyLightUpdates.stopTiming()
        }
    }

    fun isInWorld(pos: Vector3): Boolean = isInWorld(pos.floorX, pos.floorY, pos.floorZ)
    override fun isInWorld(x: Int, y: Int, z: Int): Boolean =
        x in Int.MIN_VALUE..Int.MAX_VALUE &&
            y in Int.MIN_VALUE..Int.MAX_VALUE &&
            z in Int.MIN_VALUE..Int.MAX_VALUE

    @JvmOverloads
    fun getBlock(pos: Vector3, cached: Boolean = true, addToCache: Boolean = true): Block =
        getBlockAt(pos.floorX, pos.floorY, pos.floorZ, cached, addToCache)

    override fun getBlockAt(x: Int, y: Int, z: Int) = getBlockAt(x, y, z, true)
    fun getBlockAt(x: Int, y: Int, z: Int, cached: Boolean = true, addToCache: Boolean = true): Block {
        var doCache = addToCache

        var relativeBlockHash: ChunkBlockHash? = null
        val chunkHash = chunkHash(x shr 4, z shr 4)

        val block = if (isInWorld(x, y, z)) {
            relativeBlockHash = chunkBlockHash(x, y, z)

            if (cached) {
                val cache = blockCache[chunkHash]?.get(relativeBlockHash)
                if (cache !== null) {
                    return cache
                }
            }

            val chunk = chunks[chunkHash]
            if (chunk !== null) {
                BlockFactory.fromFullBlock(chunk.getFullBlock(x and 0x0f, y, z and 0x0f))
            } else {
                doCache = false
                VanillaBlocks.AIR.block
            }
        } else {
            VanillaBlocks.AIR.block
        }

        block.position(this, x, y, z)
        if (dynamicStateRead) {
            // this call was generated by a parent getBlock() call calculating dynamic stateinfo
            // don't calculate dynamic state and don't add to block cache (since it won't have dynamic state calculated).
            // this ensures that it's impossible for dynamic state properties to recursively depend on each other.
            doCache = false
        } else {
            dynamicStateRead = true
            block.readStateFromWorld()
            dynamicStateRead = false
        }

        if (doCache && relativeBlockHash !== null) {
            blockCache.getOrPut(chunkHash, HashLongObjMaps::newMutableMap)[relativeBlockHash] = block
        }
        return block
    }

    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun setBlock(pos: Vector3, block: Block, update: Boolean = true) {
        setBlockAt(pos.floorX, pos.floorY, pos.floorZ, block, update)
    }

    /**
     * Sets the block at the given coordinates.
     *
     * If $update is true, it'll get the neighbour blocks (6 sides) and update them, and also update local lighting.
     * If you are doing big changes, you might want to set this to false, then update manually.
     *
     * @throws IllegalArgumentException if the position is out of the world bounds
     */
    @Throws(IllegalArgumentException::class)
    override fun setBlockAt(x: Int, y: Int, z: Int, block: Block) {
        setBlockAt(x, y, z, block, true)
    }

    @Throws(IllegalArgumentException::class)
    fun setBlockAt(x: Int, y: Int, z: Int, block: Block, update: Boolean = true) {
        if (!isInWorld(x, y, z)) {
            throw IllegalArgumentException("Pos x=$x,y=$y,z=$z is outside of the world bounds")
        }
        val chunkX = x shr 4
        val chunkZ = z shr 4
        if (isChunkLocked(chunkX, chunkZ)) {
            throw WorldException("Terrain is locked for generation/population")
        } else if (loadChunk(chunkX, chunkZ) === null) { // current expected behaviour is to try to load the terrain synchronously
            throw WorldException("Cannot set a block in un-generated terrain")
        }

        timings.setBlock.startTiming()

        val oldBlock = getBlockAt(x, y, z, cached = true, addToCache = false)

        val newBlock = block.clone()
        newBlock.position(this, x, y, z)
        newBlock.writeStateToWorld()

        val chunkHash = chunkHash(chunkX, chunkZ)
        val relativeBlockHash = chunkBlockHash(x, y, z)

        blockCache[chunkHash]?.remove(relativeBlockHash)
        changedBlocks.getOrPut(chunkHash, HashLongObjMaps::newMutableMap)[relativeBlockHash] = newBlock.pos

        getChunkListeners(chunkX, chunkZ).forEach { listener ->
            listener.onBlockChanged(newBlock.pos)
        }

        if (update) {
            if (
                oldBlock.getLightFilter() != newBlock.getLightFilter() ||
                oldBlock.getLightLevel() != newBlock.getLightLevel()
            ) {
                updateAllLight(x, y, z)
            }
            tryAddToNeighbourUpdateQueue(newBlock.pos)
            newBlock.pos.sides().forEach { side ->
                tryAddToNeighbourUpdateQueue(side)
            }
        }

        timings.setBlock.stopTiming()
    }

    @JvmOverloads
    fun dropItem(pos: Vector3, item: Item, motion: Vector3? = null, delay: Int = 10): Any? {
        /** TODO: replace return type to ItemEntity? */
        if (item.isNull()) {
            return null
        }

        TODO("Implements after implemented ItemEntity")
        /**
         * return ItemEntity(Location.fromObject(pos, this, Random.nextDouble() * 360, 0.0), item).apply{
         * setPickupDelay(delay)
         * setMotion(motion ?: Vector3(Random.nextDouble() * 0.2 - 0.1, 0.2, Random.nextDouble() * 0.2 - 0.1))
         * spawnToAll()
         * }
         */
    }

    /** Drops XP orbs into the world for the specified amount, splitting the amount into several orbs if necessary.*/
    fun dropExperience(pos: Vector3, amount: Int): List<Any> {
        /** TODO: replace return type to List<ExperienceOrb> */
        TODO("Implements after implemented ExperienceOrb")
    }

    /**
     * Tries to break a block using a item, including Player time checks if available
     * It'll try to lower the durability if Item is a tool, and set it to Air if broken.
     *
     * @param Item $item, reference parameter (if null, can break anything)
     */
    @JvmOverloads
    fun useBreakOn(
        pos: Vector3,
        item: Item = ItemFactory.air(),
        player: Player? = null,
        createParticles: Boolean = false
    ): Boolean {
        val vector = pos.floor()

        val chunkX = pos.chunkX
        val chunkZ = pos.chunkZ
        if (!isChunkLoaded(chunkX, chunkZ) || !isChunkGenerated(chunkX, chunkZ) || isChunkLocked(chunkX, chunkZ)) {
            return false
        }

        val target = getBlock(vector)
        val affectedBlocks = target.getAffectedBlocks()
        var drops: MutableList<Item> = mutableListOf()
        if (player == null /* TODO: || player.hasFiniteResources */) {
            affectedBlocks.forEach { block ->
                drops.addAll(block.getDrops(item))
            }
        }

        var xpDrop = 0
        if (player != null /* TODO: && player.hasFiniteResources */) {
            affectedBlocks.forEach { block ->
                xpDrop += block.getXpDropForTool(item)
            }
        }

        if (player != null) {
            val ev = BlockBreakEvent(player, target, item, false/* TODO: && player.isCreative */, drops, xpDrop)

            if (target is Air ||
                (
                    /* TODO: player.isSurvival() && */
                    !target.breakInfo.isBreakable()
                    )
                /* TODO: || player.isSpectator() */
            ) {
                ev.isCancelled = true
            }

            if (/* TODO: player.isAdventure(true) && */ !ev.isCancelled) {
                /** TODO: Implements after implemented Item::getCanDestroy
                 * var canBreak = false
                 *
                 * item.getCanDestroy().values.let {
                 *     it.forEach { v ->
                 *         val entry = LegacyStringToItemParser.parse(v)
                 *         if (entry.getBlock().isSameType(target)) {
                 *             canBreak = true
                 *             return@let
                 *         }
                 *     }
                 *     if (!canBreak) {
                 *         ev.isCancelled = true
                 *     }
                 * }
                 */
            }

            ev.call()
            if (ev.isCancelled) {
                return false
            }

            drops = ev.drops
            xpDrop = ev.xpDropAmount
        } else if (!target.breakInfo.isBreakable()) {
            return false
        }

        affectedBlocks.forEach {
            destroyBlockInternal(it, item, player, createParticles)
        }

        item.onDestroyBlock(target)

        if (drops.isNotEmpty()) {
            val dropPos = vector.add(0.5, 0.5, 0.5)
            drops.forEach { drop ->
                if (!drop.isNull()) {
                    dropItem(dropPos, drop)
                }
            }
        }

        if (xpDrop > 0) {
            dropExperience(vector.add(0.5, 0.5, 0.5), xpDrop)
        }

        return true
    }

    private fun destroyBlockInternal(target: Block, item: Item, player: Player? = null, createParticles: Boolean = false) {
        if (createParticles) {
            addParticle(target.pos.add(0.5, 0.5, 0.5), BlockBreakParticle(target))
        }
        target.onBreak(item, player)
        getTile(target.pos)?.onBlockDestroyed()
    }

    /**
     * Uses a item on a position and face, placing it or activating the block
     *
     * @param Player?  player default null
     * @param Boolean  playSound Whether to play a block-place sound if the block was placed successfully.
     */
    @JvmOverloads
    fun useItemOn(
        pos: Vector3,
        item: Item,
        face: Facing,
        clickVector: Vector3 = Vector3(),
        player: Player? = null,
        playSound: Boolean = false
    ): Boolean {
        val blockClicked = getBlock(pos)
        var blockReplace = blockClicked.getSide(face)

        if (!isInWorld(blockReplace.pos)) {
            // TODO: build height limit messages for custom world heights and mcregion cap
            return false
        }
        val chunkX = blockReplace.pos.chunkX
        val chunkZ = blockReplace.pos.chunkZ
        if (!isChunkLoaded(chunkX, chunkZ) || !isChunkGenerated(chunkX, chunkZ) || isChunkLocked(chunkX, chunkZ)) {
            return false
        }

        if (blockClicked.getId() == VanillaBlocks.AIR.id) {
            return false
        }

        if (player !== null) {
            val ev =
                PlayerInteractEvent(player, item, blockClicked, clickVector, face, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            /** TODO: if (player.isSpectator()) ev.isCancelled = true */

            ev.call()
            if (!ev.isCancelled) {
                if ((!player.isSneaking() or item.isNull()) and blockClicked.onInteract(item, face, clickVector, player)) {
                    return true
                }

                val result = item.onInteractBlock(player, blockReplace, blockClicked, face, clickVector)
                if (!result.equals(ItemUseResult.NONE)) {
                    return result.equals(ItemUseResult.SUCCESS)
                }
            } else {
                return false
            }
        } else if (blockClicked.onInteract(item, face, clickVector, player)) {
            return true
        }

        val hand = if (item.canBePlaced()) {
            val hand = item.getBlock(face)
            hand.position(this, blockReplace.pos)
            hand
        } else {
            return false
        }

        if (hand.canBePlacedAt(blockClicked, clickVector, face, true)) {
            blockReplace = blockClicked
            hand.position(this, blockReplace.pos)
        } else if (!hand.canBePlacedAt(blockReplace, clickVector, face, false)) {
            return false
        }

        val tx = BlockTransaction(this)
        if (!hand.place(tx, item, blockReplace, blockClicked, face, clickVector, player)) {
            return false
        }

        tx.getBlocks().forEach { (vec, block) ->
            block.position(this, vec)
            block.collisionBoxes.forEach { collisionBox ->
                if (getCollidingEntities(collisionBox).isNotEmpty()) {
                    return false // Entity in block
                }
            }
        }

        if (player !== null) {
            val ev = BlockPlaceEvent(player, hand, blockReplace, blockClicked, item)
            /** TODO: if (player.isSpectator()) ev.isCancelled = true */

            if (/* TODO: player.isAdventure(true) && */ !ev.isCancelled) {
                /** TODO: Implements after implemented Item::getCanPlaceOn
                 * var canPlace = false
                 *
                 * item.getCanPlaceOn().values.let{
                 * it.forEach{ v->
                 *     val entry = LegacyStringToItemParser.parse(v)
                 *     if (entry.getBlock().isSameType(blockClicked)) {
                 *         canPlace = true
                 *         return@let
                 *     }
                 * }}
                 *
                 * if (!canPlace) {
                 *     ev.cancel()
                 * }
                 */
            }

            ev.call()
            if (ev.isCancelled) {
                return false
            }
        }

        if (!tx.apply()) {
            return false
        }
        tx.getBlocks().forEach { (vec) ->
            getTile(vec)?.copyDataFromItem(item)

            getBlock(vec).onPostPlace()
        }

        if (playSound) {
            addSound(hand.pos, BlockPlaceSound(hand))
        }

        item.pop()

        return true
    }

    fun getEntity(entityId: EntityId): Entity? = entities[entityId]

    /** Returns the entities colliding the current one inside the AxisAlignedBB */
    @JvmOverloads
    fun getCollidingEntities(bb: AxisAlignedBB, entity: Entity? = null): List<Entity> {
        val nearby = mutableListOf<Entity>()

        if (entity?.canCollide == false) {
            return nearby
        }
        chunkRepeat(bb, 2F) { chunkX, chunkZ ->
            this.getChunk(chunkX, chunkZ)?.entities?.values?.forEach { ent ->
                if (
                    ent.canBeCollidedWith() &&
                    (entity === null || (ent !== entity && entity.canCollideWith(ent))) &&
                    ent.boundingBox.intersectsWith(bb)
                ) {
                    nearby.add(ent)
                }
            }
        }

        return nearby
    }

    /** Returns the entities near the current one inside the AxisAlignedBB */
    @JvmOverloads
    fun getNearbyEntities(bb: AxisAlignedBB, entity: Entity? = null): List<Entity> {
        val nearby = mutableListOf<Entity>()

        chunkRepeat(bb, 2F) { chunkX, chunkZ ->
            this.getChunk(chunkX, chunkZ)?.entities?.values?.forEach { ent ->
                if (ent !== entity && ent.boundingBox.intersectsWith(bb)) {
                    nearby.add(ent)
                }
            }
        }

        return nearby
    }

    /** Returns the closest Entity to the specified position, within the given radius. */
    @JvmOverloads
    fun getNearestEntity(
        pos: Vector3,
        maxDistance: Float,
        entityType: Class<out Entity> = Entity::class.java,
        includeDead: Boolean = false
    ): Entity? {
        var currentTargetDistSq: Double = maxDistance.toDouble().pow(2)
        var currentTarget: Entity? = null

        chunkRepeat(pos, maxDistance) { chunkX, chunkZ ->
            this.getChunk(chunkX, chunkZ)?.entities?.values?.forEach { entity ->
                if (
                    entityType.isInstance(entity) &&
                    !entity.isFlaggedForDespawn() &&
                    (includeDead || entity.isAlive())
                ) {
                    val distSq = entity.getPosition().distanceSquared(pos)
                    if (distSq < currentTargetDistSq) {
                        currentTargetDistSq = distSq
                        currentTarget = entity
                    }
                }
            }
        }

        return currentTarget
    }

    /** Returns the Tile in a position, or null if not found. */
    fun getTile(pos: Vector3): Tile? =
        loadChunk(pos.chunkX, pos.chunkZ)?.getTile(pos.x.toInt() and 0x0f, pos.y.toInt(), pos.z.toInt() and 0x0f)

    /** Returns the tile at the specified x,y,z coordinates, or null if it does not exist. */
    fun getTileAt(x: Int, y: Int, z: Int): Tile? =
        loadChunk(x shr 4, z shr 4)?.getTile(x and 0x0f, y, z and 0x0f)

    fun getBiomeId(x: Int, z: Int): Int =
        loadChunk(x shr 4, z shr 4)?.getBiomeId(x and 0x0f, z and 0x0f) ?: BiomeIds.OCEAN.id

    fun getBiome(x: Int, z: Int): Biome = BiomeRegistry.get(getBiomeId(x, z))

    fun setBiomeId(x: Int, z: Int, biomeId: Int) {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        if (isChunkLocked(chunkX, chunkZ)) {
            // the changes would be overwritten when the generation finishes
            throw WorldException("Chunk is currently locked for async generation/population")
        }
        val chunk = loadChunk(chunkX, chunkZ)
        if (chunk !== null) {
            chunk.setBiomeId(x and 0x0f, z and 0x0f, biomeId)
        } else {
            // if we allowed this, the modifications would be lost when the chunk is created
            throw WorldException("Cannot set biome in a non-generated chunk")
        }
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): Chunk? = chunks[chunkHash(chunkX, chunkZ)]

    /** Returns the chunk containing the given Vector3 position. */
    fun getOrLoadChunkAtPosition(pos: Vector3): Chunk? = loadChunk(pos.chunkX, pos.chunkZ)

    /** Returns the chunks adjacent to the specified chunk. */
    fun getAdjacentChunks(chunkX: Int, chunkZ: Int): MutableList<Chunk?> {
        val result: MutableList<Chunk?> = mutableListOf()
        repeat2(3) { xx, zz ->
            val i = zz * 3 + xx
            if (i != 4) { // skip center chunk
                result[i] = loadChunk(chunkX + xx - 1, chunkZ + zz - 1)
            }
        }

        return result
    }

    fun lockChunk(chunkX: Int, chunkZ: Int) {
        if (!chunkLock.add(chunkHash(chunkX, chunkZ))) {
            throw IllegalArgumentException("Chunk $chunkX $chunkZ is already locked")
        }
    }

    fun unlockChunk(chunkX: Int, chunkZ: Int) = chunkLock.remove(chunkHash(chunkX, chunkZ))

    fun isChunkLocked(chunkX: Int, chunkZ: Int): Boolean = chunkLock.contains(chunkHash(chunkX, chunkZ))

    private fun drainPopulationRequestQueue() {
        val failed: MutableList<Long> = mutableListOf()
        while (activeChunkPopulationTasks.size < maxConcurrentChunkPopulationTasks && chunkPopulationRequestQueue.isNotEmpty()) {
            val chunkHash = chunkPopulationRequestQueue.poll()
            val (chunkX, chunkZ) = parseChunkHash(chunkHash)

            if (chunkPopulationRequestMap.containsKey(chunkHash)) {
                assert(!activeChunkPopulationTasks.contains(chunkHash)) { "Population for chunk $chunkX $chunkZ already running" }
                orderChunkPopulation(chunkX, chunkZ, null)
                if (!activeChunkPopulationTasks.contains(chunkHash)) {
                    failed.add(chunkHash)
                }
            }
        }

        // these requests failed even though they weren't rate limited; we can't directly re-add them to the back of the
        // queue because it would result in an infinite loop
        failed.forEach(chunkPopulationRequestQueue::add)
    }

    override fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        setChunk(chunkX, chunkZ, chunk, true)
    }

    /**
     * @param Boolean $deleteEntitiesAndTiles Whether to delete entities and tiles from the old chunk or
     * transfer them to a new chunk.
     */
    fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk, deleteEntitiesAndTiles: Boolean) {
        val chunkHash = chunkHash(chunkX, chunkZ)
        val oldChunk = loadChunk(chunkX, chunkZ)
        if (oldChunk !== null && oldChunk !== chunk) {
            if (deleteEntitiesAndTiles) {
                oldChunk.entities.values.forEach { entity ->
                    if (entity is Player) {
                        chunk.addEntity(entity)
                        oldChunk.removeEntity(entity)
                    } else {
                        entity.close()
                    }
                }
                oldChunk.tiles.values.forEach { tile ->
                    tile.close()
                }
            } else {
                oldChunk.entities.values.forEach { entity ->
                    chunk.addEntity(entity)
                    oldChunk.removeEntity(entity)
                }
                oldChunk.tiles.values.forEach { tile ->
                    chunk.addTile(tile)
                    oldChunk.removeTile(tile)
                }
            }
        }

        chunks[chunkHash] = chunk

        blockCache.remove(chunkHash)
        changedBlocks.remove(chunkHash)
        chunk.setDirty()

        if (!this.isChunkInUse(chunkX, chunkZ)) {
            this.unloadChunkRequest(chunkX, chunkZ)
        }

        if (oldChunk === null) {
            (ChunkLoadEvent(this, chunkX, chunkZ, chunk, true)).call()

            getChunkListeners(chunkX, chunkZ).forEach { listener ->
                listener.onChunkLoaded(chunkX, chunkZ, chunk)
            }
        } else {
            getChunkListeners(chunkX, chunkZ).forEach { listener ->
                listener.onChunkChanged(chunkX, chunkZ, chunk)
            }
        }
    }

    /**
     * Gets the highest block Y value at a specific $x and $z
     *
     * @return int|null 0-255, or null if the column is empty
     * @throws WorldException if the terrain is not generated
     */
    fun getHighestBlockAt(x: Int, z: Int): Int? {
        loadChunk(x shr 4, z shr 4)?.let {
            return it.getHighestBlockAt(x and 0x0f, z and 0x0f)
        }
        throw WorldException("Cannot get highest block in an ungenerated chunk")
    }

    fun isInLoadedTerrain(pos: Vector3): Boolean = isChunkLoaded(pos.chunkX, pos.chunkZ)
    fun isChunkLoaded(chunkX: Int, chunkZ: Int): Boolean = chunks[chunkHash(chunkX, chunkZ)] !== null
    fun isChunkGenerated(chunkX: Int, chunkZ: Int): Boolean = loadChunk(chunkX, chunkZ) !== null
    fun isChunkPopulated(chunkX: Int, chunkZ: Int): Boolean = loadChunk(chunkX, chunkZ)?.terrainPopulated == true

    @Throws(IllegalArgumentException::class)
    fun addEntity(entity: Entity) {
        if (entity.isClosed()) {
            throw IllegalArgumentException("Attempted to add a garbage closed Entity to world")
        } else if (entity.world !== this) {
            throw IllegalArgumentException("Invalid Entity world")
        }

        val entityId = entity.getId()
        entities[entityId]?.let { oldEntity ->
            throw IllegalArgumentException(
                if (oldEntity === entity) {
                    "Entity $entityId has already been added to this world"
                } else {
                    "Found two different entities sharing entity ID $entityId"
                }
            )
        }
        val pos = entity.getPosition().asVector3()
        val chunk = getOrLoadChunkAtPosition(pos)
            ?: throw IllegalArgumentException("Cannot add an Entity in an ungenerated chunk")

        chunk.addEntity(entity)
        this.entityLastKnownPositions[entityId] = pos

        if (entity is Player) {
            this.players[entityId] = entity
        }
        this.entities[entityId] = entity
    }

    /**
     * Removes the entity from the world index
     *
     * @throws IllegalArgumentException
     */
    fun removeEntity(entity: Entity) {
        if (entity.world !== this) {
            throw IllegalArgumentException("Invalid Entity world")
        }
        val entityId = entity.getId()
        if (!entities.containsKey(entityId)) {
            throw IllegalArgumentException("Entity is not tracked by this world (possibly already removed?)")
        }
        val pos = this.entityLastKnownPositions[entityId] ?: entity.getPosition()
        val chunk = getChunk(pos.chunkX, pos.chunkX)
        if (chunk !== null) { // we don't care if the chunk already went out of scope
            chunk.removeEntity(entity)
        }

        entityLastKnownPositions.remove(entityId)
        if (entity is Player) {
            players.remove(entityId)
            this.checkSleep()
        }
        entities.remove(entityId)
        updateEntities.remove(entityId)
    }

    internal fun onEntityMoved(entity: Entity) {
        val entityId = entity.getId()
        val oldPosition = entityLastKnownPositions[entityId] ?: return
        val newPosition = entity.getPosition()

        val oldChunkX = oldPosition.chunkX
        val oldChunkZ = oldPosition.chunkZ
        val newChunkX = newPosition.chunkX
        val newChunkZ = newPosition.chunkZ

        if (oldChunkX != newChunkX || oldChunkZ != newChunkZ) {
            getChunk(oldChunkX, oldChunkZ)?.removeEntity(entity)

            val newChunk = loadChunk(newChunkX, newChunkZ)
            if (newChunk === null) {
                // TODO: this is a non-ideal solution for a hard problem
                // when this happens the entity won't be tracked by any chunk, so we can't have it hanging around in memory
                // we also can't allow this to cause chunk generation, nor can we just create an empty ungenerated chunk
                // for it, because an empty chunk won't get saved, so the entity will vanish anyway. Therefore, this is
                // the cleanest way to make sure this doesn't result in leaks.
                logger.debug("Entity $entityId is in ungenerated terrain, flagging for despawn")
                entity.flagForDespawn()
                entity.despawnFromAll()
            } else {
                val newViewers = getChunkViewers(newPosition).toMutableSet()
                entity.getViewers().values.forEach { player ->
                    if (!newViewers.remove(player)) {
                        entity.despawnFrom(player)
                    }
                }
                newViewers.forEach { player ->
                    entity.spawnTo(player)
                }

                newChunk.addEntity(entity)
            }
        }
        entityLastKnownPositions[entityId] = newPosition.asVector3()
    }

    internal fun addTile(tile: Tile) {
        if (tile.closed) {
            throw IllegalArgumentException("Attempted to add a garbage closed Tile to world")
        }
        val pos = tile.pos
        if (!pos.isValid() || pos.world !== this) {
            throw IllegalArgumentException("Invalid Tile world")
        }

        val chunkX = pos.chunkX
        val chunkZ = pos.chunkZ
        val chunkHash = chunkHash(chunkX, chunkZ)

        val chunk = chunks[chunkHash]
        if (chunk !== null) {
            chunk.addTile(tile)
        } else {
            throw IllegalStateException("Attempted to create tile ${tile::class} in unloaded chunk chunkX chunkZ")
        }

        // delegate tile ticking to the corresponding block
        scheduleDelayedBlockUpdate(pos.asVector3(), 1)
    }

    internal fun removeTile(tile: Tile) {
        val pos = tile.pos
        if (!pos.isValid() || pos.world !== this) {
            throw IllegalArgumentException("Invalid Tile world")
        }

        val chunkX = pos.chunkX
        val chunkZ = pos.chunkZ
        val chunkHash = chunkHash(chunkX, chunkZ)
        chunks[chunkHash]?.removeTile(tile)
        getChunkListeners(chunkX, chunkZ).forEach { listener ->
            listener.onBlockChanged(pos.asVector3())
        }
    }

    fun isChunkInUse(chunkX: Int, chunkZ: Int): Boolean = chunkLoaders[chunkHash(chunkX, chunkZ)]?.takeIf { it.size > 0 } !== null

    /**
     * Attempts to load a chunk from the world provider (if not already loaded). If the chunk is already loaded, it is
     * returned directly.
     *
     * @return Chunk|null the requested chunk, or null on failure.
     *
     * @throws \InvalidStateException
     */
    fun loadChunk(chunkX: Int, chunkZ: Int): Chunk? {
        val chunkHash = chunkHash(chunkX, chunkZ)
        chunks[chunkHash]?.let {
            return it
        }

        timings.syncChunkLoad.startTiming()

        cancelUnloadChunkRequest(chunkX, chunkZ)

        timings.syncChunkLoadData.startTiming()

        val chunk = try {
            provider.loadChunk(chunkX, chunkZ)
        } catch (e: CorruptedChunkException) {
            logger.error("Failed to load chunk x=$chunkX z=$chunkZ: " + e.message)
            null
        }

        timings.syncChunkLoadData.stopTiming()

        if (chunk === null) {
            timings.syncChunkLoad.stopTiming()
            return null
        }

        chunks[chunkHash] = chunk
        blockCache.remove(chunkHash)
        chunk.initNbt(this, chunkX, chunkZ)

        ChunkLoadEvent(this, chunkX, chunkZ, chunk, false).call()

        if (!isChunkInUse(chunkX, chunkZ)) {
            logger.debug(
                "Newly loaded chunk $chunkX $chunkZ has no loaders registered, " +
                    "will be unloaded at next available opportunity"
            )
            unloadChunkRequest(chunkX, chunkZ)
        }
        getChunkListeners(chunkX, chunkZ).forEach { listener ->
            listener.onChunkLoaded(chunkX, chunkZ, chunk)
        }

        timings.syncChunkLoad.stopTiming()

        return chunk
    }

    private fun queueUnloadChunk(chunkX: Int, chunkZ: Int) {
        unloadQueue[chunkHash(chunkX, chunkZ)] = System.currentTimeMillis()
    }

    fun unloadChunkRequest(chunkX: Int, chunkZ: Int, safe: Boolean = true): Boolean =
        if (safe && isChunkInUse(chunkX, chunkZ) || isSpawnChunk(chunkX, chunkZ)) {
            false
        } else {
            val chunkHash = chunkHash(chunkX, chunkZ)
            unloadQueue[chunkHash] = System.currentTimeMillis()
            true
        }

    fun cancelUnloadChunkRequest(chunkX: Int, chunkZ: Int) {
        unloadQueue.remove(chunkHash(chunkX, chunkZ))
    }

    @JvmOverloads
    fun unloadChunk(chunkX: Int, chunkZ: Int, safe: Boolean = true, trySave: Boolean = true): Boolean {
        if (safe && isChunkInUse(chunkX, chunkZ)) {
            return false
        }

        if (isChunkLoaded(chunkX, chunkZ)) {
            return true
        }

        timings.doChunkUnload.startTiming()

        val chunkHash = chunkHash(chunkX, chunkZ)
        val chunk = chunks[chunkHash]

        chunk?.let {
            val ev = ChunkUnloadEvent(this, chunkX, chunkZ, it)
            ev.call()
            if (ev.isCancelled) {
                timings.doChunkUnload.stopTiming()

                return false
            }

            if (trySave && autoSave && chunk.isDirty) {
                timings.syncChunkSave.startTiming()
                try {
                    provider.saveChunk(chunkX, chunkZ, it)
                } finally {
                    timings.syncChunkSave.stopTiming()
                }
            }

            getChunkListeners(chunkX, chunkZ).forEach { listener -> listener.onChunkUnloaded(chunkX, chunkZ, it) }

            it.onUnload()
        }

        chunks.remove(chunkHash)
        blockCache.remove(chunkHash)
        changedBlocks.remove(chunkHash)

        chunkPopulationRequestMap.remove(chunkHash)?.let(Promise<Chunk>::reject)

        timings.doChunkUnload.stopTiming()

        return true
    }

    /** Returns whether the chunk at the specified coordinates is a spawn chunk */
    fun isSpawnChunk(chunkX: Int, chunkZ: Int): Boolean {
        val spawn = spawnLocation
        return abs(chunkX - spawn.chunkX) <= 1 && abs(chunkZ - spawn.chunkZ) <= 1
    }

    @Throws(WorldException::class)
    @JvmOverloads
    fun getSafeSpawn(pos: Vector3? = null): Position {
        var spawn = pos?.takeIf { it.y >= 1 } ?: spawnLocation

        val max = maxY
        val v = spawn.floor()
        val chunk = getOrLoadChunkAtPosition(v) ?: throw WorldException("Cannot find a safe spawn point in non-generated terrain")

        val x = v.floorX
        val z = v.floorZ
        var y = min(max - 2, v.floorY)

        var wasAir = getBlockAt(x, y - 1, z).getId() == VanillaBlocks.AIR.id
        while (y > minY) {
            if (getBlockAt(x, y, z).isFullCube()) {
                if (wasAir) {
                    y++
                    break
                }
            } else {
                wasAir = true
            }
            --y
        }

        while (y in minY until max) {
            if (!getBlockAt(x, y + 1, z).isFullCube()) {
                if (!getBlockAt(x, y, z).isFullCube()) {
                    return Position(spawn.floorX, if (y == spawn.floorY) spawn.floorY else y, spawn.floorZ, this)
                }
            } else {
                ++y
            }
            ++y
        }

        return Position(spawn.x, y.toDouble(), spawn.z, this)
    }

    fun stopTime() {
        stopTime = true
        sendTime()
    }

    fun startTime() {
        stopTime = false
        sendTime()
    }

    /**
     * @phpstan-return Promise<Chunk>
     */
    private fun enqueuePopulationRequest(chunkX: Int, chunkZ: Int, associatedChunkLoader: ChunkLoader?): Promise<Chunk> {
        val chunkHash = chunkHash(chunkX, chunkZ)
        chunkPopulationRequestQueue.remove(chunkHash)
        val promise = chunkPopulationRequestMap.getOrPut(chunkHash, ::Promise)
        if (associatedChunkLoader === null) {
            val temporaryLoader = object : ChunkLoader {}
            registerChunkLoader(temporaryLoader, chunkX, chunkZ)
            promise.onCompletion({ unregisterChunkLoader(temporaryLoader, chunkX, chunkZ) }, { })
        }
        return promise
    }

    /**
     * Attempts to initiate asynchronous generation/population of the target chunk, if it's currently reasonable to do
     * so (and if it isn't already generated/populated).
     * If the generator is busy, the request will be put into a queue and delayed until a better time.
     *
     * A ChunkLoader can be associated with the generation request to ensure that the generation request is cancelled if
     * no loaders are attached to the target chunk. If no loader is provided, one will be assigned (and automatically
     * removed when the generation request completes).
     */
    fun requestChunkPopulation(chunkX: Int, chunkZ: Int, associatedChunkLoader: ChunkLoader?): Promise<Chunk> {
        val chunkHash = chunkHash(chunkX, chunkZ)
        val promise = chunkPopulationRequestMap[chunkHash]
        if (promise !== null && activeChunkPopulationTasks.contains(chunkHash)) {
            // generation is already running
            return promise
        }
        if (activeChunkPopulationTasks.size >= maxConcurrentChunkPopulationTasks) {
            // too many chunks are already generating; delay resolution of the request until later
            return promise ?: enqueuePopulationRequest(chunkX, chunkZ, associatedChunkLoader)
        }
        return orderChunkPopulation(chunkX, chunkZ, associatedChunkLoader)
    }

    /**
     * Initiates asynchronous generation/population of the target chunk, if it's not already generated/populated.
     * If generation has already been requested for the target chunk, the promise for the already active request will be returned directly.
     *
     * If the chunk is currently locked (for example due to another chunk using it for async generation),
     * the request will be queued and executed at the earliest opportunity.
     *
     * @phpstan-return Promise<Chunk>
     */
    fun orderChunkPopulation(chunkX: Int, chunkZ: Int, associatedChunkLoader: ChunkLoader?): Promise<Chunk> {
        val chunkHash = chunkHash(chunkX, chunkZ)
        var promise = chunkPopulationRequestMap[chunkHash]
        if (promise !== null && activeChunkPopulationTasks.contains(chunkHash)) {
            return promise // generation is already running
        }
        for (xx in -1..1) {
            for (zz in -1..1) {
                if (isChunkLocked(chunkX + xx, chunkZ + zz)) {
                    // chunk is already in use by another generation request; queue the request for later
                    return promise ?: enqueuePopulationRequest(chunkX, chunkZ, associatedChunkLoader)
                }
            }
        }

        var chunk = loadChunk(chunkX, chunkZ)
        if (chunk != null && chunk.terrainPopulated) {
            // chunk is already populated; return a pre-resolved promise that will directly fire callbacks assigned
            promise = Promise()
            promise.resolve(chunk)
            return promise
        } else {
            Timings.population.startTiming()

            activeChunkPopulationTasks.add(chunkHash)
            if (promise === null) {
                promise = Promise()
                chunkPopulationRequestMap[chunkHash] = promise
            }

            for (xx in -1..1) {
                for (zz in -1..1) {
                    lockChunk(chunkX + xx, chunkZ + zz)
                }
            }

            val chunks = getAdjacentChunks(chunkX, chunkZ).apply { this[4] = chunk }
            workerPool.submit(
                AsyncTask(
                    {
                        val manager = SimpleChunkManager(minY, maxY)
                        val generator = this.generator.java
                            .getConstructor(Long::class.java, String::class.java)
                            .newInstance(provider.worldData.seed, provider.worldData.generatorOptions)

                        repeat2(3) { xx, zz ->
                            val i = zz * 3 + xx
                            val cX = chunkX + xx - 1
                            val cZ = chunkZ + zz - 1

                            val c = chunks[i] ?: Chunk().apply {
                                manager.setChunk(cX, cZ, this)
                                generator.generateChunk(manager, cX, cZ)

                                setDirtyFlag(Chunk.DIRTY_FLAG_TERRAIN, true)
                                setDirtyFlag(Chunk.DIRTY_FLAG_BIOMES, true)
                            }
                            manager.setChunk(cX, cZ, c)
                            chunks[i] = c
                        }

                        generator.populateChunk(manager, chunkX, chunkZ)
                        chunk = manager.getChunk(chunkX, chunkZ)?.apply {
                            terrainPopulated = true
                            setDirtyFlag(Chunk.DIRTY_FLAG_TERRAIN, true)
                        }

                        // onCompletion
                        if (!unloaded) {
                            repeat2(3) { xx, zz ->
                                val i = zz * 3 + xx
                                val cX = chunkX + xx - 1
                                val cZ = chunkZ + zz - 1
                                chunks[i]?.takeIf { i != 4 && it.isDirty }?.let { c ->
                                    Timings.generation.startTiming()
                                    if (chunkPopulationRequestMap.containsKey(chunkHash) && activeChunkPopulationTasks.contains(
                                            chunkHash
                                        )
                                    ) {
                                        for (cx in -1..1) {
                                            for (cz in -1..1) {
                                                unlockChunk(cX + xx, cZ + zz)
                                            }
                                        }

                                        val oldChunk = loadChunk(cX, cZ)
                                        setChunk(cX, cZ, c)
                                        if (oldChunk == null || !oldChunk.terrainPopulated && c.terrainPopulated) {
                                            (ChunkPopulateEvent(this, cX, cZ, c)).call()

                                            getChunkListeners(cX, cZ).forEach { listener ->
                                                listener.onChunkPopulated(cX, cZ, c)
                                            }
                                        }
                                        activeChunkPopulationTasks.remove(chunkHash)
                                        chunkPopulationRequestMap.remove(chunkHash)?.resolve(c)

                                        drainPopulationRequestQueue()
                                    } else if (isChunkLocked(cX, cZ)) {
                                        unlockChunk(cX, cZ)
                                        setChunk(cX, cZ, c)
                                        drainPopulationRequestQueue()
                                    } else {
                                        setChunk(cX, cZ, c)
                                    }
                                    Timings.generation.stopTiming()
                                }
                            }
                        }
                    },
                    Unit
                )
            )

            Timings.population.stopTiming()
            return promise
        }
    }

    fun doChunkGarbageCollection() {
        timings.doChunkGC.startTiming()

        chunks.forEach { (chunkHash, chunk) ->
            if (!unloadQueue.contains(chunkHash)) {
                val (chunkX, chunkZ) = parseChunkHash(chunkHash)
                if (!isSpawnChunk(chunkX, chunkZ)) {
                    unloadChunkRequest(chunkX, chunkZ, true)
                }
            }
            chunk.collectGarbage()
        }

        provider.doGarbageCollection()

        timings.doChunkGC.stopTiming()
    }

    @JvmOverloads
    fun unloadChunks(force: Boolean = true) {
        if (unloadQueue.isNotEmpty()) {
            var maxUnload = 96
            val now = System.currentTimeMillis()
            unloadQueue.forEach { (chunkHash, time) ->
                val (chunkX, chunkZ) = parseChunkHash(chunkHash)
                if (!force) {
                    if (maxUnload <= 0) {
                        return
                    } else if (time > (now - 30000)) {
                        return@forEach
                    }
                }

                // If the chunk can't be unloaded, it stays on the queue
                if (unloadChunk(chunkX, chunkZ, true)) {
                    unloadQueue.remove(chunkHash)
                    --maxUnload
                }
            }
        }
    }

    companion object {
        const val DIFFICULTY_PEACEFUL = 0
        const val DIFFICULTY_EASY = 1
        const val DIFFICULTY_NORMAL = 2
        const val DIFFICULTY_HARD = 3

        const val Y_MAX = 256
        const val Y_MIN = 0

        const val TIME_DAY = 1000
        const val TIME_NOON = 6000
        const val TIME_SUNSET = 12000
        const val TIME_NIGHT = 13000
        const val TIME_MIDNIGHT = 18000
        const val TIME_SUNRISE = 23000

        const val TIME_FULL = 24000

        private var worldIdCounter = 0

        private const val MORTON3D_BIT_SIZE = 21
        private const val BLOCKHASH_Y_BITS = 9
        private const val BLOCKHASH_Y_PADDING = 128 // size (in blocks) of padding after both boundaries of the Y axis
        private const val BLOCKHASH_Y_OFFSET = BLOCKHASH_Y_PADDING - Y_MIN
        private const val BLOCKHASH_Y_MASK = (1 shl BLOCKHASH_Y_BITS) - 1
        private const val BLOCKHASH_XZ_MASK = (1 shl MORTON3D_BIT_SIZE) - 1
        private const val BLOCKHASH_XZ_EXTRA_BITS = 6
        private const val BLOCKHASH_XZ_EXTRA_MASK = (1 shl BLOCKHASH_XZ_EXTRA_BITS) - 1
        private const val BLOCKHASH_XZ_SIGN_SHIFT = 64 - MORTON3D_BIT_SIZE - BLOCKHASH_XZ_EXTRA_BITS
        private const val BLOCKHASH_X_SHIFT = BLOCKHASH_Y_BITS
        private const val BLOCKHASH_Z_SHIFT = BLOCKHASH_X_SHIFT + BLOCKHASH_XZ_EXTRA_BITS

        private const val DEFAULT_TICKED_BLOCKS_PER_SUBCHUNK_PER_TICK = 3

        fun blockHash(pos: Vector3): BlockHash = blockHash(pos.floorX, pos.floorY, pos.floorZ)
        fun blockHash(x: Int, y: Int, z: Int): BlockHash {
            val shiftedY = y + BLOCKHASH_Y_OFFSET
            if (shiftedY and (0.inv() shl BLOCKHASH_Y_BITS) != 0) {
                throw IllegalArgumentException("Y coordinate $y is out of range!")
            }

            // morton3d gives us 21 bits on each axis, but the Y axis only requires 9
            // so we use the extra space on Y (12 bits) and add 6 extra bits from X and Z instead.
            // if we ever need more space for Y (e.g. due to expansion), take bits from X/Z to compensate.
            return Morton3D.encode(
                x = x and BLOCKHASH_XZ_MASK,
                y = shiftedY or
                    x shr MORTON3D_BIT_SIZE and BLOCKHASH_XZ_EXTRA_MASK shl BLOCKHASH_X_SHIFT or
                    z shr MORTON3D_BIT_SIZE and BLOCKHASH_XZ_EXTRA_MASK shl BLOCKHASH_Z_SHIFT,
                z = z and BLOCKHASH_XZ_MASK
            )
        }

        fun chunkHash(chunkX: Int, chunkZ: Int): ChunkHash = Morton2D.encode(chunkX, chunkZ)
        fun chunkHash(pos: Vector3): ChunkHash = Morton2D.encode(pos.chunkX, pos.chunkZ)

        /** Computes a small index relative to chunk base from the given coordinates. */
        fun chunkBlockHash(x: Int, y: Int, z: Int): ChunkBlockHash = Morton3D.encode(x, y, z)
        fun chunkBlockHash(pos: Vector3): ChunkBlockHash =
            Morton3D.encode(pos.chunkX, pos.chunkY, pos.chunkZ)

        fun parseBlockHash(hash: BlockHash): Triple<Int, Int, Int> {
            val (baseX, baseY, baseZ) = Morton3D.decode(hash)

            val extraX = baseY shr BLOCKHASH_X_SHIFT and BLOCKHASH_XZ_EXTRA_MASK shl MORTON3D_BIT_SIZE
            val extraZ = baseY shr BLOCKHASH_Z_SHIFT and BLOCKHASH_XZ_EXTRA_MASK shl MORTON3D_BIT_SIZE

            return Triple(
                baseX and BLOCKHASH_XZ_MASK or extraX shl BLOCKHASH_XZ_SIGN_SHIFT shr BLOCKHASH_XZ_SIGN_SHIFT,
                baseY and BLOCKHASH_Y_MASK - BLOCKHASH_Y_OFFSET,
                baseZ and BLOCKHASH_XZ_MASK or extraZ shl BLOCKHASH_XZ_SIGN_SHIFT shr BLOCKHASH_XZ_SIGN_SHIFT
            )
        }

        fun parseChunkHash(hash: ChunkHash): Pair<Int, Int> = Morton2D.decode(hash)

        fun chunkRange(x: Double, expansion: Float): IntRange = chunkRange(x, x, expansion)
        fun chunkRange(minX: Double, maxX: Double, expansion: Float): IntRange =
            ((minX - expansion).toInt() shr 4)..((maxX + expansion).toInt() shr 4)

        inline fun chunkRepeat(pos: Vector3, expansion: Float, action: (Int, Int) -> Unit) {
            for (chunkX in chunkRange(pos.x, expansion)) {
                for (chunkZ in chunkRange(pos.z, expansion)) {
                    action(chunkX, chunkZ)
                }
            }
        }

        inline fun chunkRepeat(bb: AxisAlignedBB, expansion: Float, action: (Int, Int) -> Unit) {
            for (chunkX in chunkRange(bb.minX, bb.maxX, expansion)) {
                for (chunkZ in chunkRange(bb.minZ, bb.maxZ, expansion)) {
                    action(chunkX, chunkZ)
                }
            }
        }

        fun getDifficultyFromString(str: String): Int = when (str.trim().lowercase()) {
            "0", "peaceful", "p" -> DIFFICULTY_PEACEFUL
            "1", "easy", "e" -> DIFFICULTY_EASY
            "2", "normal", "n" -> DIFFICULTY_NORMAL
            "3", "hard", "h" -> DIFFICULTY_HARD
            else -> -1
        }
    }
}
