package be.zvz.kookie.event.world

import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk

class ChunkPopulateEvent(world: World, chunkX: Int, chunkZ: Int, chunk: Chunk) : ChunkEvent(world, chunkX, chunkZ, chunk)
