package be.zvz.kookie.block.tile

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt
import be.zvz.kookie.world.World

abstract class Spawnable(world: World, pos: Vector3) : Tile(world, pos) {
    // TODO: CacheableNbt implementation for spawnCompoundCache
    var spawnCompoundCache: CacheableNbt? = null
    var dirty: Boolean = true
        set(value) {
            if (value) {
                spawnCompoundCache = null
                // TODO: implement spawnCompoundCache
            }
            field = value
        }
}
