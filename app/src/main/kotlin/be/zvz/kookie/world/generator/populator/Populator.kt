package be.zvz.kookie.world.generator.populator

import be.zvz.kookie.world.ChunkManager
import kotlin.random.Random

abstract class Populator {
    abstract fun populate(world: ChunkManager, chunkX: Int, chunkZ: Int, random: Random)
}
