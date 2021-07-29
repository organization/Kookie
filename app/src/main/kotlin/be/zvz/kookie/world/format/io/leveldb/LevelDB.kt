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
package be.zvz.kookie.world.format.io.leveldb

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.data.bedrock.LegacyIdToStringIdMap
import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.NbtDataException
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.utils.Binary
import be.zvz.kookie.utils.BinaryDataException
import be.zvz.kookie.utils.BinaryStream
import be.zvz.kookie.world.WorldCreationOptions
import be.zvz.kookie.world.biome.BiomeIds
import be.zvz.kookie.world.format.BiomeArray
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.PalettedBlockArray
import be.zvz.kookie.world.format.SubChunk
import be.zvz.kookie.world.format.io.BaseWorldProvider
import be.zvz.kookie.world.format.io.SubChunkConverter
import be.zvz.kookie.world.format.io.WorldData
import be.zvz.kookie.world.format.io.WritableWorldProvider
import be.zvz.kookie.world.format.io.data.BedrockWorldData
import be.zvz.kookie.world.format.io.exception.CorruptedChunkException
import org.iq80.leveldb.DB
import org.iq80.leveldb.DBException
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import org.iq80.leveldb.impl.WriteBatchImpl
import org.slf4j.Logger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.createDirectory
import kotlin.io.path.isDirectory

class LevelDB(path: Path) : BaseWorldProvider(path), WritableWorldProvider {

    private val db: DB = createDB(path)

    override fun loadLevelData(): WorldData {
        return BedrockWorldData(path.resolve("level.dat"))
    }

    override val worldMinY: Int = 0 // TODO: -60 support
    override val worldMaxY: Int = 256 // TODO: 265+ support

    private fun deserializePaletted(stream: BinaryStream): PalettedBlockArray {
        val bitsPerBlock = stream.getByte() shr 1

        try {
            val words = stream.get(PalettedBlockArray.getExpectedWordArraySize(bitsPerBlock.toLong()))
            val nbt = LittleEndianNbtSerializer()

            val palette: MutableList<Long> = mutableListOf()

            val idMap = LegacyIdToStringIdMap.BLOCK
            val paletteSize = stream.getLInt()

            for (i in 0 until paletteSize) {
                val offset = stream.offset
                val tag = nbt.read(stream.buffer.toString(), offset).mustGetCompoundTag()
                stream.offset.set(offset.get())

                val id = idMap.stringToLegacy[tag.getString("name")] ?: VanillaBlocks.INFO_UPDATE.id
                var data = tag.getShort("val")
                if (id == VanillaBlocks.AIR.id) {
                    // TODO: quick and dirty hack for artifacts left behind by broken world editors
                    // we really need a proper state fixer, but this is a pressing issue.
                    data = 0
                }
                // FIXME: Block doesn't have INTERNAL_METADATA_BITS, have to add
                palette.add(((id shl INTERNAL_METADATA_BITS) or data).toLong())
            }

            return PalettedBlockArray(PalettedBlockArray.fromData(bitsPerBlock, words, palette))
        } catch (e: IllegalArgumentException) {
            throw CorruptedChunkException("Failed to deserialize paletted storage: " + e.message)
        }
    }

    fun deserializeLegacyExtraData(index: String, chunkVersion: Int): List<PalettedBlockArray> {
        try {
            val extraRawData = db.get((index + TAG_BLOCK_EXTRA_DATA).toByteArray())

            val extraDataLayers = mutableListOf<PalettedBlockArray>()
            val binaryStream = BinaryStream(extraRawData.toString())
            val count = binaryStream.getLInt()
            for (i in 0 until count) {
                val key = binaryStream.getLInt()
                val value = binaryStream.getLShort()

                val x = AtomicInteger()
                val fullY = AtomicInteger()
                val z = AtomicInteger()

                deserializeExtraDataKey(chunkVersion, key, x, fullY, z)

                val ySub = (fullY.get() shr 4) and 0xf
                val y = key and 0xf
                val blockId = value and 0xff
                val blockData = (value shr 8) and 0xf
                if (extraDataLayers.getOrNull(ySub) == null) {
                    extraDataLayers.add(
                        ySub,
                        PalettedBlockArray((VanillaBlocks.AIR.id shl INTERNAL_METADATA_BITS).toLong())
                    )
                }
                extraDataLayers[ySub].set(
                    x.get(),
                    y,
                    z.get(),
                    ((blockId shl INTERNAL_METADATA_BITS) or blockData).toLong()
                )
            }
            return extraDataLayers
        } catch (e: DBException) {
            return mutableListOf()
        }
    }

    override fun loadChunk(chunkX: Int, chunkZ: Int): Chunk? {
        try {
            val index = chunkIndex(chunkX, chunkZ)

            val chunkVersionRaw = db.get((index + TAG_VERSION).toByteArray()) ?: return null

            val subChunks: MutableList<SubChunk> = mutableListOf()

            var biomeArray: BiomeArray? = null

            val chunkVersion = ord(chunkVersionRaw.toString())
            var hasBeenUpgraded = chunkVersion < CURRENT_LEVEL_CHUNK_VERSION

            when (chunkVersion) {
                15, 14, 13, 12, 11, 10, 9, 7, 6, 4 -> {
                    val convertedLegacyExtraData = deserializeLegacyExtraData(index, chunkVersion)
                    for (y in 0 until Chunk.MAX_SUBCHUNKS) {
                        val data = db.get((index + TAG_SUBCHUNK_PREFIX).toByteArray()) ?: continue

                        val binaryStream = BinaryStream(data.toString())
                        if (binaryStream.feof()) {
                            throw CorruptedChunkException("Unexpected empty data for subchunk $y")
                        }
                        val subChunkVersion = binaryStream.getByte()
                        if (subChunkVersion < CURRENT_LEVEL_SUBCHUNK_VERSION) {
                            hasBeenUpgraded = true
                        }
                        when (subChunkVersion) {
                            1 -> { // paletted v1, has a single blockstorage
                                try {
                                    val blocks = binaryStream.get(4096)
                                    val blockData = binaryStream.get(2048)
                                    if (chunkVersion < 4) {
                                        binaryStream.get(4096) // legacy light info, discard it
                                    }
                                    hasBeenUpgraded = true
                                    val storages =
                                        mutableListOf(SubChunkConverter.convertSubChunkXZY(blocks, blockData))
                                    if (convertedLegacyExtraData.getOrNull(y) != null) {
                                        storages.add(convertedLegacyExtraData[y])
                                    }
                                    subChunks[y] =
                                        SubChunk((VanillaBlocks.AIR.id shl INTERNAL_METADATA_BITS).toLong(), storages)
                                } catch (e: BinaryDataException) {
                                    throw CorruptedChunkException(e.message ?: "No error was provided", e)
                                }
                            }
                            8 -> {
                                // legacy extradata layers intentionally ignored because they aren't supposed to exist in v8

                                val storageCount = binaryStream.getByte()
                                if (storageCount > 0) {
                                    val storages = mutableListOf<PalettedBlockArray>()
                                    for (i in 0 until storageCount) {
                                        storages.add(deserializePaletted(binaryStream))
                                    }
                                    subChunks[y] =
                                        SubChunk((VanillaBlocks.AIR.id shl INTERNAL_METADATA_BITS).toLong(), storages)
                                }
                            }
                            else -> throw CorruptedChunkException(
                                "don't know how to decode LevelDB subchunk format version $subChunkVersion"
                            )
                        }
                    }
                    val maps2d = db.get((index + TAG_DATA_2D).toByteArray())
                    if (maps2d != null) {
                        val binaryStream = BinaryStream(maps2d.toString())
                        try {
                            binaryStream.get(512) // heightmap, discard it
                            biomeArray = BiomeArray(binaryStream.get(256))
                        } catch (e: BinaryDataException) {
                            throw CorruptedChunkException(e.message ?: "No error was provided", e)
                        }
                    }
                }
                2, 1, 0 -> { // MCPE 1.0, 0.9.1 beta (first version)
                    val convertedLegacyExtraData = deserializeLegacyExtraData(index, chunkVersion)

                    val legacyTerrain = db.get((index + TAG_LEGACY_TERRAIN).toByteArray())
                        ?: throw CorruptedChunkException("Missing expected LEGACY_TERRAIN tag for format version $chunkVersion")

                    val binaryStream = BinaryStream(legacyTerrain.toString())
                    try {
                        val fullIds = binaryStream.get(32768)
                        val fullData = binaryStream.get(16384)

                        for (yy in 0 until 8) {
                            val storages =
                                mutableListOf(SubChunkConverter.convertSubChunkFromLegacyColumn(fullIds, fullData, yy))
                            if (convertedLegacyExtraData.getOrNull(yy) != null) {
                                storages.add(convertedLegacyExtraData[yy])
                            }
                            subChunks[yy] =
                                SubChunk((VanillaBlocks.AIR.id shl INTERNAL_METADATA_BITS).toLong(), storages)
                        }
                    } catch (e: BinaryDataException) {
                        throw CorruptedChunkException(e.message ?: "No error was provided", e)
                    }
                    try {
                        binaryStream.get(256) // heightmap, discard it
                        val unpackedBiomeArray = unpackNStar(binaryStream.get(1024).toByteArray())
                    } catch (e: BinaryDataException) {
                        throw CorruptedChunkException(e.message ?: "No error was provided", e)
                    }
                }
                else -> throw CorruptedChunkException("don't know how to decode chunk format version $chunkVersion")
            }
            val nbt = LittleEndianNbtSerializer()

            var entities: List<CompoundTag> = mutableListOf()
            val entityData = db.get((index + TAG_ENTITY).toByteArray())

            if (entityData != null && entityData.toString() != "") {
                try {
                    entities = mutableListOf<CompoundTag>().apply {
                        nbt.readMultiple(entityData.toString()).forEach {
                            add(it.mustGetCompoundTag())
                        }
                    }
                } catch (e: NbtDataException) {
                    throw CorruptedChunkException(e.message ?: "No error was provided", e)
                }
            }
            var tiles: MutableList<CompoundTag> = mutableListOf()
            val tileData = db.get((index + TAG_BLOCK_ENTITY).toByteArray())
            if (tileData != null && tileData.toString() != "") {
                try {
                    tiles = mutableListOf<CompoundTag>().apply {
                        nbt.readMultiple(tileData.toString()).forEach {
                            add(it.mustGetCompoundTag())
                        }
                    }
                } catch (e: NbtDataException) {
                    throw CorruptedChunkException(e.message ?: "No error was provided", e)
                }
            }
            val chunk = Chunk(
                subChunks,
                entities,
                tiles,
                biomeArray ?: BiomeArray.fill(BiomeIds.PLAINS.id)
            )
            chunk.terrainPopulated = true
            chunk.dirtyFlags = chunk.dirtyFlags or Chunk.DIRTY_FLAG_TERRAIN
            if (hasBeenUpgraded) {
                chunk.setDirty()
            }
            return chunk
        } catch (e: DBException) {
            return null
        }
    }

    override fun saveChunk(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        val idMap = LegacyIdToStringIdMap.BLOCK
        val index = chunkIndex(chunkX, chunkZ)

        val write = WriteBatchImpl()
        // FIXME: PHP sucks, I don't know how to encode Int to char, but when I know it another problem have appeared to me
        // This is hacky method and I don't know if it is correct
        write.put(
            (index + TAG_VERSION).toByteArray(),
            CURRENT_LEVEL_CHUNK_VERSION.toChar().code.toString().toByteArray()
        )
        if (chunk.getDirtyFlag(Chunk.DIRTY_FLAG_TERRAIN)) {
            val subChunks = chunk.subChunks
            subChunks.forEachIndexed { y, subChunk ->
                val key = index + TAG_SUBCHUNK_PREFIX + y.toChar().code.toString().toByteArray()
                if (subChunk.isEmptyAuthoritative()) {
                    write.delete(key.toByteArray())
                } else {
                    val subStream = BinaryStream()
                    subStream.putByte(CURRENT_LEVEL_SUBCHUNK_VERSION)

                    val layers = subChunk.blockLayers
                    subStream.putByte(layers.size)
                    layers.forEach {
                        subStream.putByte(it.bitsPerBlock shl 1)
                        subStream.put(it.wordArray.toString())

                        val palette = it.palette
                        subStream.putLInt(palette.size)
                        val tags = mutableListOf<TreeRoot>()
                        palette.forEach { p ->
                            tags.add(
                                TreeRoot(
                                    CompoundTag.create().apply {
                                        setString(
                                            "name",
                                            idMap.legacyToString[p.toInt() shr INTERNAL_METADATA_BITS]
                                                ?: "minecraft:info_update"
                                        )
                                        setInt("oldid", p.toInt() shr INTERNAL_METADATA_BITS)
                                        setShort("val", p.toInt() and INTERNAL_METADATA_MASK)
                                    }
                                )
                            )

                            subStream.put(LittleEndianNbtSerializer().writeMultiple(tags))
                        }
                        write.put(key.toByteArray(), subStream.buffer.toString().toByteArray())
                    }
                }
            }
        }
        if (chunk.getDirtyFlag(Chunk.DIRTY_FLAG_BIOMES)) {
            write.put(
                (index + TAG_DATA_2D).toByteArray(),
                (0x00.toString().repeat(512) + chunk.biomeIdArray).toByteArray()
            )
        }

        write.put(
            (index + TAG_STATE_FINALISATION).toByteArray(),
            FINALISATION_DONE.toChar().code.toString().toByteArray()
        )

        writeTags(chunk.NBTtiles, index + TAG_BLOCK_ENTITY, write)
        writeTags(chunk.NBTentities, index + TAG_ENTITY, write)

        write.delete((index + TAG_DATA_2D_LEGACY).toByteArray())
        write.delete((index + TAG_LEGACY_TERRAIN).toByteArray())
        db.write(write)
    }

    private fun writeTags(targets: List<CompoundTag>, index: String, write: WriteBatchImpl) {
        if (targets.isNotEmpty()) {
            val nbt = LittleEndianNbtSerializer()
            val list = mutableListOf<TreeRoot>().apply {
                targets.forEach { add(TreeRoot(it)) }
            }
            write.put(index.toByteArray(), nbt.writeMultiple(list).toByteArray())
        } else {
            write.delete(index.toByteArray())
        }
    }

    override fun doGarbageCollection() {
        // NOOP
    }

    override fun close() {
        db.close()
    }

    override fun getAllChunks(skipCorrupted: Boolean, logger: Logger?) = sequence {
        db.forEach { (key, _) ->
            val chunkX = Binary.readLInt(key.toString().substring(0 until 4))
            val chunkZ = Binary.readLInt(key.toString().substring(4))

            try {
                loadChunk(chunkX, chunkZ)?.let {
                    yield(Triple(chunkX, chunkZ, it))
                }
            } catch (e: CorruptedChunkException) {
                if (!skipCorrupted) {
                    throw e
                }
                logger?.error("Skipped corrupted chunk $chunkX $chunkZ (${e.message})")
            }
        }
    }

    override fun calculateChunkCount(): Int {
        var count = 0
        db.forEach { (key, _) ->
            if (key.toString().length == 9 && key.toString().substring(-1) == TAG_VERSION) {
                count++
            }
        }
        return count
    }

    companion object {
        // According to Tomasso, these aren't supposed to be readable anymore. Thankfully he didn't change the readable ones...
        private const val TAG_DATA_2D = 0x2d.toChar().toString()
        private const val TAG_DATA_2D_LEGACY = 0x2e.toChar().toString()
        private const val TAG_SUBCHUNK_PREFIX = 0x2f.toChar().toString()
        private const val TAG_LEGACY_TERRAIN = "0"
        private const val TAG_BLOCK_ENTITY = "1"
        private const val TAG_ENTITY = "2"
        private const val TAG_PENDING_TICK = "3"
        private const val TAG_BLOCK_EXTRA_DATA = "4"
        private const val TAG_BIOME_STATE = "5"
        private const val TAG_STATE_FINALISATION = "6"

        private const val TAG_BORDER_BLOCKS = "8"
        private const val TAG_HARDCODED_SPAWNERS = "9"

        private const val FINALISATION_NEEDS_INSTATICKING = 0
        private const val FINALISATION_NEEDS_POPULATION = 1
        private const val FINALISATION_DONE = 2

        private const val TAG_VERSION = "v"

        private const val ENTRY_FLAT_WORLD_LAYERS = "game_flatworldlayers"

        private const val CURRENT_LEVEL_CHUNK_VERSION = 7
        private const val CURRENT_LEVEL_SUBCHUNK_VERSION = 8

        const val INTERNAL_METADATA_BITS = 4
        const val INTERNAL_METADATA_MASK = (0.inv() shl INTERNAL_METADATA_BITS).inv()

        private fun createDB(path: Path): DB {
            return Iq80DBFactory().open(path.toFile(), Options())
        }

        fun generate(path: Path, name: String, options: WorldCreationOptions) {
            if (!path.isDirectory()) {
                path.createDirectory()
            }
            BedrockWorldData.generate(path, name, options)
        }

        private fun deserializeExtraDataKey(
            chunkVersion: Int,
            key: Int,
            x: AtomicInteger = AtomicInteger(),
            y: AtomicInteger = AtomicInteger(),
            z: AtomicInteger = AtomicInteger()
        ) {
            if (chunkVersion >= 3) {
                x.set((key shr 12) and 0xf)
                z.set((key shr 8) and 0xf)
                y.set(key and 0xff)
            } else { // pre-1.0, 7 bits were used because the build height limit was lower
                x.set((key shr 11) and 0xf)
                z.set((key shr 7) and 0xf)
                y.set(key and 0x7f)
            }
        }

        fun chunkIndex(chunkX: Int, chunkZ: Int): String {
            return Binary.writeLInt(chunkX) + Binary.writeLInt(chunkZ)
        }

        fun ord(s: String): Int {
            return if (s.isNotEmpty()) {
                s.toByteArray(StandardCharsets.UTF_8)[0].toInt() and 0xff
            } else {
                0
            }
        }

        fun ord(c: Char): Int {
            return if (c.code < 0x80) {
                c.code
            } else {
                ord(c.toString())
            }
        }

        private fun unpackNStar(bytes: ByteArray): String {
            val byteBuf: ByteBuffer = ByteBuffer.wrap(bytes)

            byteBuf.order(ByteOrder.BIG_ENDIAN)
            val intBuf: IntBuffer = byteBuf.asIntBuffer()

            val integers = IntArray(intBuf.remaining())
            intBuf[integers].get()
            return integers.toString()
        }
    }
}
