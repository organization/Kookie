package be.zvz.kookie.world.biome

import be.zvz.kookie.block.Block
import be.zvz.kookie.world.ChunkManager
import be.zvz.kookie.world.generator.populator.Populator
import kotlin.random.Random

abstract class Biome(
    val id: Int
) {
    var populators: MutableList<Populator> = mutableListOf()
        private set

    var minElevation: Int = 0
        private set
    var maxElevation: Int = 0
        private set

    var groundCover: MutableList<Block> = mutableListOf()

    var rainfall: Float = 0.5f
        protected set
    var temperature: Float = 0.5f
        protected set

    fun clearPopulators() = populators.clear()

    fun addPopulator(populator: Populator) {
        populators.add(populator)
    }

    fun populateChunk(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random) {
        populators.forEach {
            it.populate(world, chunkX, chunkZ, random)
        }
    }

    abstract fun getName(): String

    fun setElevation(min: Int, max: Int) {
        minElevation = min
        maxElevation = max
    }

    companion object {
        const val MAX_BIOMES = 256
    }
}
