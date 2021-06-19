package be.zvz.kookie.event.world

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk

class ChunkUnloadEvent(world: World, chunkX: Int, chunkZ: Int, chunk: Chunk) :
    ChunkEvent(world, chunkX, chunkZ, chunk),
    Cancellable {
    override var isCancelled: Boolean = false
}
