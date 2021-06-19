package be.zvz.kookie.event.world

import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk

class ChunkLoadEvent(
    world: World,
    chunkX: Int,
    chunkZ: Int,
    chunk: Chunk,
    val isNewLoaded: Boolean
) : ChunkEvent(world, chunkX, chunkZ, chunk)
