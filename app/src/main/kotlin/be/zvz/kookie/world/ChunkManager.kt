package be.zvz.kookie.world

import be.zvz.kookie.block.Block
import be.zvz.kookie.world.format.Chunk

interface ChunkManager {
    fun getBlockAt(x: Int, y: Int, z: Int): Block
    fun setBlockAt(x: Int, y: Int, z: Int, block: Block)

    fun getChunk(chunkX: Int, chunkZ: Int): Chunk?
    fun setChunk(chunkX: Int, chunkZ: Int, chunk: Chunk)

    fun getMinY(): Int
    fun getMaxY(): Int

    fun isInWorld(x: Int, y: Int, z: Int): Boolean
}
