package be.zvz.kookie.world.format.io.leveldb

import be.zvz.kookie.block.BlockLegacyIds
import be.zvz.kookie.data.bedrock.LegacyIdToStringIdMap
import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.utils.Binary
import be.zvz.kookie.utils.BinaryStream
import be.zvz.kookie.world.WorldCreationOptions
import be.zvz.kookie.world.format.BiomeArray
import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.PalettedBlockArray
import be.zvz.kookie.world.format.SubChunk
import be.zvz.kookie.world.format.io.BaseWorldProvider
import be.zvz.kookie.world.format.io.WorldData
import be.zvz.kookie.world.format.io.data.BedrockWorldData
import be.zvz.kookie.world.format.io.exception.CorruptedChunkException
import com.koloboke.collect.map.hash.HashIntObjMaps
import org.iq80.leveldb.DB
import org.iq80.leveldb.DBException
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import org.slf4j.Logger
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.createDirectory
import kotlin.io.path.isDirectory

class LevelDB(path: Path) : BaseWorldProvider(path) {

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

                val id = idMap.stringToLegacy[tag.getString("name")] ?: BlockLegacyIds.INFO_UPDATE.id
                var data = tag.getShort("val")
                if (id == BlockLegacyIds.AIR.id) {
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
                        PalettedBlockArray((BlockLegacyIds.AIR.id shl INTERNAL_METADATA_BITS).toLong())
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

            val subChunks: MutableMap<Int, SubChunk> = HashIntObjMaps.newMutableMap()

            val biomeArray: BiomeArray? = null

            val chunkVersion =

            /*
            $index = LevelDB::chunkIndex($chunkX, $chunkZ);

            $chunkVersionRaw = $this->db->get($index . self::TAG_VERSION);
            if($chunkVersionRaw === false){
                return null;
            }

            /** @var SubChunk[] $subChunks */
            $subChunks = [];

            /** @var BiomeArray|null $biomeArray */
            $biomeArray = null;

            $chunkVersion = ord($chunkVersionRaw);
            $hasBeenUpgraded = $chunkVersion < self::CURRENT_LEVEL_CHUNK_VERSION;

            switch($chunkVersion){
                case 15: //MCPE 1.12.0.4 beta (???)
                case 14: //MCPE 1.11.1.2 (???)
                case 13: //MCPE 1.11.0.4 beta (???)
                case 12: //MCPE 1.11.0.3 beta (???)
                case 11: //MCPE 1.11.0.1 beta (???)
                case 10: //MCPE 1.9 (???)
                case 9: //MCPE 1.8 (???)
                case 7: //MCPE 1.2 (???)
                case 6: //MCPE 1.2.0.2 beta (???)
                case 4: //MCPE 1.1
                    //TODO: check beds
                case 3: //MCPE 1.0
                    $convertedLegacyExtraData = $this->deserializeLegacyExtraData($index, $chunkVersion);

                    for($y = 0; $y < Chunk::MAX_SUBCHUNKS; ++$y){
                        if(($data = $this->db->get($index . self::TAG_SUBCHUNK_PREFIX . chr($y))) === false){
                            continue;
                        }

                        $binaryStream = new BinaryStream($data);
                        if($binaryStream->feof()){
                            throw new CorruptedChunkException("Unexpected empty data for subchunk $y");
                        }
                        $subChunkVersion = $binaryStream->getByte();
                        if($subChunkVersion < self::CURRENT_LEVEL_SUBCHUNK_VERSION){
                            $hasBeenUpgraded = true;
                        }

                        switch($subChunkVersion){
                            case 0:
                            case 2: //these are all identical to version 0, but vanilla respects these so we should also
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                try{
                                    $blocks = $binaryStream->get(4096);
                                    $blockData = $binaryStream->get(2048);

                                    if($chunkVersion < 4){
                                        $binaryStream->get(4096); //legacy light info, discard it
                                        $hasBeenUpgraded = true;
                                    }
                                }catch(BinaryDataException $e){
                                    throw new CorruptedChunkException($e->getMessage(), 0, $e);
                                }

                                $storages = [SubChunkConverter::convertSubChunkXZY($blocks, $blockData)];
                                if(isset($convertedLegacyExtraData[$y])){
                                    $storages[] = $convertedLegacyExtraData[$y];
                                }

                                $subChunks[$y] = new SubChunk(BlockLegacyIds::AIR << Block::INTERNAL_METADATA_BITS, $storages);
                                break;
                            case 1: //paletted v1, has a single blockstorage
                                $storages = [$this->deserializePaletted($binaryStream)];
                                if(isset($convertedLegacyExtraData[$y])){
                                    $storages[] = $convertedLegacyExtraData[$y];
                                }
                                $subChunks[$y] = new SubChunk(BlockLegacyIds::AIR << Block::INTERNAL_METADATA_BITS, $storages);
                                break;
                            case 8:
                                //legacy extradata layers intentionally ignored because they aren't supposed to exist in v8
                                $storageCount = $binaryStream->getByte();
                                if($storageCount > 0){
                                    $storages = [];

                                    for($k = 0; $k < $storageCount; ++$k){
                                        $storages[] = $this->deserializePaletted($binaryStream);
                                    }
                                    $subChunks[$y] = new SubChunk(BlockLegacyIds::AIR << Block::INTERNAL_METADATA_BITS, $storages);
                                }
                                break;
                            default:
                                //TODO: set chunks read-only so the version on disk doesn't get overwritten
                                throw new CorruptedChunkException("don't know how to decode LevelDB subchunk format version $subChunkVersion");
                        }
                    }

                    if(($maps2d = $this->db->get($index . self::TAG_DATA_2D)) !== false){
                        $binaryStream = new BinaryStream($maps2d);

                        try{
                            $binaryStream->get(512); //heightmap, discard it
                            $biomeArray = new BiomeArray($binaryStream->get(256)); //never throws
                        }catch(BinaryDataException $e){
                            throw new CorruptedChunkException($e->getMessage(), 0, $e);
                        }
                    }
                    break;
                case 2: // < MCPE 1.0
                case 1:
                case 0: //MCPE 0.9.0.1 beta (first version)
                    $convertedLegacyExtraData = $this->deserializeLegacyExtraData($index, $chunkVersion);

                    $legacyTerrain = $this->db->get($index . self::TAG_LEGACY_TERRAIN);
                    if($legacyTerrain === false){
                        throw new CorruptedChunkException("Missing expected LEGACY_TERRAIN tag for format version $chunkVersion");
                    }
                    $binaryStream = new BinaryStream($legacyTerrain);
                    try{
                        $fullIds = $binaryStream->get(32768);
                        $fullData = $binaryStream->get(16384);
                        $binaryStream->get(32768); //legacy light info, discard it
                    }catch(BinaryDataException $e){
                        throw new CorruptedChunkException($e->getMessage(), 0, $e);
                    }

                    for($yy = 0; $yy < 8; ++$yy){
                        $storages = [SubChunkConverter::convertSubChunkFromLegacyColumn($fullIds, $fullData, $yy)];
                        if(isset($convertedLegacyExtraData[$yy])){
                            $storages[] = $convertedLegacyExtraData[$yy];
                        }
                        $subChunks[$yy] = new SubChunk(BlockLegacyIds::AIR << Block::INTERNAL_METADATA_BITS, $storages);
                    }

                    try{
                        $binaryStream->get(256); //heightmap, discard it
                        /** @var int[] $unpackedBiomeArray */
                        $unpackedBiomeArray = unpack("N*", $binaryStream->get(1024)); //unpack() will never fail here
                        $biomeArray = new BiomeArray(ChunkUtils::convertBiomeColors(array_values($unpackedBiomeArray))); //never throws
                    }catch(BinaryDataException $e){
                        throw new CorruptedChunkException($e->getMessage(), 0, $e);
                    }
                    break;
                default:
                    //TODO: set chunks read-only so the version on disk doesn't get overwritten
                    throw new CorruptedChunkException("don't know how to decode chunk format version $chunkVersion");
            }

            $nbt = new LittleEndianNbtSerializer();

            /** @var CompoundTag[] $entities */
            $entities = [];
            if(($entityData = $this->db->get($index . self::TAG_ENTITY)) !== false and $entityData !== ""){
                try{
                    $entities = array_map(function(TreeRoot $root) : CompoundTag{ return $root->mustGetCompoundTag(); }, $nbt->readMultiple($entityData));
                }catch(NbtDataException $e){
                    throw new CorruptedChunkException($e->getMessage(), 0, $e);
                }
            }

            /** @var CompoundTag[] $tiles */
            $tiles = [];
            if(($tileData = $this->db->get($index . self::TAG_BLOCK_ENTITY)) !== false and $tileData !== ""){
                try{
                    $tiles = array_map(function(TreeRoot $root) : CompoundTag{ return $root->mustGetCompoundTag(); }, $nbt->readMultiple($tileData));
                }catch(NbtDataException $e){
                    throw new CorruptedChunkException($e->getMessage(), 0, $e);
                }
            }

            $chunk = new Chunk(
                $subChunks,
                $entities,
                $tiles,
                $biomeArray
            );

            //TODO: tile ticks, biome states (?)

            $chunk->setPopulated();
            if($hasBeenUpgraded){
                $chunk->setDirty(); //trigger rewriting chunk to disk if it was converted from an older format
            }

            return $chunk;
             */
        } catch (e: DBException) {
            return null
        }
    }

    override fun doGarbageCollection() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun getAllChunks(skipCorrupted: Boolean, logger: Logger?): Sequence<Chunk> {
        TODO("Not yet implemented")
    }

    override fun calculateChunkCount(): Int {
        TODO("Not yet implemented")
    }

    companion object {
        //According to Tomasso, these aren't supposed to be readable anymore. Thankfully he didn't change the readable ones...
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
            return if (c < 0x80) {
                c.code
            } else {
                ord(Character.toString(c))
            }
        }
    }
}