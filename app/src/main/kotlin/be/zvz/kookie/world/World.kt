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
import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.block.tile.Tile
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.math.Morton2D
import be.zvz.kookie.math.Morton3D
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket
import be.zvz.kookie.player.Player
import be.zvz.kookie.scheduler.AsyncPool
import be.zvz.kookie.utils.Promise
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.io.WritableWorldProvider
import be.zvz.kookie.world.generator.Generator
import be.zvz.kookie.world.generator.GeneratorManager
import be.zvz.kookie.world.light.BlockLightUpdate
import be.zvz.kookie.world.light.SkyLightUpdate
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashLongFloatMaps
import com.koloboke.collect.map.hash.HashLongIntMaps
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
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KClass

class World(
    val server: Server,
    val folderName: String,
    private val provider: WritableWorldProvider,
    private val workerPool: AsyncPool
) :
    ChunkManager {
    val players: MutableList<Player> = mutableListOf()
    val entities: MutableList<Entity> = mutableListOf()

    private val entityLastKnownPositions: MutableMap<Int, Vector3> = HashIntObjMaps.newMutableMap()

    private val updateEntities: MutableList<Entity> = mutableListOf()
    private var blockCache: MutableMap<Int, MutableMap<Int, Block>> = HashIntObjMaps.newMutableMap()

    private var sendTimeTicker: Int = 0

    val worldId: Int = worldIdCounter++

    private var providerGarbageCollectionTicker: Int = 0

    override val minY: Int = provider.worldMinY
    override val maxY: Int = provider.worldMaxY

    private val tickingLoaders: MutableMap<TickingChunkLoader, Int> = HashObjIntMaps.newMutableMap()
    private val chunkLoaders: MutableMap<Long, MutableSet<ChunkLoader>> = HashLongObjMaps.newMutableMap()

    private val chunkListeners: MutableMap<Long, MutableSet<ChunkListener>> = HashLongObjMaps.newMutableMap()
    private val playerChunkListeners: MutableMap<Long, MutableSet<ClientboundPacket>> = HashLongObjMaps.newMutableMap()

    private var packetBuffersByChunk: MutableMap<Long, MutableList<Chunk>> = HashLongObjMaps.newMutableMap()
    private var unloadQueue: MutableMap<Long, Float> = HashLongFloatMaps.newMutableMap()

    var time: Long = provider.worldData.time
        set(value) {
            field = value
            TODO("Implements sendTime()")
        }
    var stopTime: Boolean = false
        set(value) {
            field = value
            TODO("Implements sendTime()")
        }

    var sunAnglePercentage: Float = 0F
        private set
    var skyLightReduction: Int = 0
        private set

    val displayName: String = provider.worldData.name

    val chunks: MutableMap<Long, Chunk> = HashLongObjMaps.newMutableMap()
    private var changedBlocks: MutableMap<Long, MutableMap<Long, Chunk>> = HashLongObjMaps.newMutableMap()

    private val scheduledBlockUpdateQueue: PriorityQueue<Pair<Long, Vector3>> = PriorityQueue { first, second ->
        -(first.first - second.first).toInt()
    }
    private var scheduledBlockUpdateQueueIndex: MutableMap<Long, Int> = HashLongIntMaps.newMutableMap()

    private val neighbourBlockUpdateQueue: Queue<Long> = LinkedList()
    private val neighbourBlockUpdateQueueIndex: MutableSet<Long> = HashLongSets.newMutableSet()

    private val activeChunkPopulationTasks: MutableSet<Long> = HashLongSets.newMutableSet()
    private val chunkLock: MutableSet<Long> = HashLongSets.newMutableSet()
    private val maxConcurrentChunkPopulationTasks: Int =
        server.configGroup.getProperty("chunk-generation.population-queue-size").asLong(2).toInt()
    private var chunkPopulationRequestMap: MutableMap<Long, Promise<Chunk>> = HashLongObjMaps.newMutableMap()
    private var chunkPopulationRequestQueue: Queue<Long> = LinkedList()
    private val generatorRegisteredWorkers: MutableSet<Int> = HashIntSets.newMutableSet()

    var autoSave: Boolean = true

    var sleepTicks: Int = 0

    val chunkTickRadius: Int =
        min(
            max(2, server.configGroup.getConfigLong("view-distance", 8)).toInt(),
            max(1, server.configGroup.getProperty("chunk-ticking.tick-radius").asLong(4).toInt())
        )
    val chunksPerTick: Int = server.configGroup.getProperty("chunk-ticking.per-tick").asLong(40).toInt()
    val tickedBlocksPerSubchunkPerTick: Int = server.configGroup.getProperty("chunk-ticking.blocks-per-subchunk-per-tick")
        .asLong(DEFAULT_TICKED_BLOCKS_PER_SUBCHUNK_PER_TICK.toLong()).toInt()
    private val randomTickBlocks: MutableSet<Long> = HashLongSets.newMutableSet()

    val timings = WorldTimings(this)
    var worldRateTime: Float = 0f
    var doingTick: Boolean = false
        private set

    private val generator: KClass<out Generator> = GeneratorManager.getGenerator(provider.worldData.generatorName, true)

    var closed: Boolean = false
        private set
    val unloadCallbacks: MutableSet<() -> Unit> = HashObjSets.newMutableSet()

    private var blockLightUpdate: BlockLightUpdate? = null
    private var skyLightUpdate: SkyLightUpdate? = null

    val logger: Logger = LoggerFactory.getLogger("World: $displayName")

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
        /**
         * TODO: Implements after implemented WorldPool::addWorkerStartHook() and WorldPool::removeWorkerStartHook()
         *
         * val workerStartHook = { workerId: Int ->
         *     if (generatorRegisteredWorkers.remove(workerId)) {
         *         logger.debug("Worker $workerId with previously registered generator restarted, flagging as unregistered")
         *     }
         * }
         * workerPool.addWorkerStartHook(workerStartHook)
         * addOnUnloadCallback { workerPool.removeWorkerStartHook(workerStartHook) }
         */
    }

    fun getOrLoadChunkAtPosition(pos: Vector3): Chunk? {
        TODO("Chunk not yet implemented")
    }

    fun getTile(pos: Vector3): Tile? {
        TODO("World not yet implemented")
    }

    fun addTile(tile: Tile) {
        TODO("World not yet implemented")
    }

    fun getBlock(pos: Vector3): Block {
        TODO("Chunk not yet implemented")
    }

    fun setBlock(pos: Vector3, block: Block): Any {
        TODO("Chunk not yet implemented")
    }

    override fun getBlockAt(x: Int, y: Int, z: Int): Block {
        TODO("Not yet implemented")
    }

    override fun setBlockAt(x: Int, y: Int, z: Int, block: Block) {
        TODO("Not yet implemented")
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): Chunk? {
        TODO("Not yet implemented")
    }

    override fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        TODO("Not yet implemented")
    }

    override fun isInWorld(x: Int, y: Int, z: Int): Boolean {
        TODO("Not yet implemented")
    }

    fun addOnUnloadCallback(callback: () -> Unit) = unloadCallbacks.add(callback)
    fun removeOnUnloadCallback(callback: () -> Unit) = unloadCallbacks.remove(callback)

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

        fun blockHash(x: Int, y: Int, z: Int): Long {
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

        fun chunkHash(x: Int, z: Int): Long = Morton2D.encode(x, z)

        /** Computes a small index relative to chunk base from the given coordinates. */
        fun chunkBlockHash(x: Int, y: Int, z: Int): Long = Morton3D.encode(x, y, z)

        fun getBlockXYZ(hash: Long): Triple<Int, Int, Int> {
            val (baseX, baseY, baseZ) = Morton3D.decode(hash)

            val extraX = baseY shr BLOCKHASH_X_SHIFT and BLOCKHASH_XZ_EXTRA_MASK shl MORTON3D_BIT_SIZE
            val extraZ = baseY shr BLOCKHASH_Z_SHIFT and BLOCKHASH_XZ_EXTRA_MASK shl MORTON3D_BIT_SIZE

            return Triple(
                baseX and BLOCKHASH_XZ_MASK or extraX shl BLOCKHASH_XZ_SIGN_SHIFT shr BLOCKHASH_XZ_SIGN_SHIFT,
                baseY and BLOCKHASH_Y_MASK - BLOCKHASH_Y_OFFSET,
                baseZ and BLOCKHASH_XZ_MASK or extraZ shl BLOCKHASH_XZ_SIGN_SHIFT shr BLOCKHASH_XZ_SIGN_SHIFT
            )
        }

        fun getXZ(hash: Long): Pair<Int, Int> = Morton2D.decode(hash)

        fun getDifficultyFromString(str: String): Int = when (str.trim().lowercase()) {
            "0", "peaceful", "p" -> DIFFICULTY_PEACEFUL
            "1", "easy", "e" -> DIFFICULTY_EASY
            "2", "normal", "n" -> DIFFICULTY_NORMAL
            "3", "hard", "h" -> DIFFICULTY_HARD
            else -> -1
        }
    }
}
