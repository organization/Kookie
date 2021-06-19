package be.zvz.kookie.event.world

import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk

abstract class ChunkEvent(world: World, val chunkX: Int, val chunkZ: Int, chunk: Chunk) : WorldEvent(world)
