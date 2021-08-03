package be.zvz.kookie.block.utils

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.math.Facing
import be.zvz.kookie.world.Position

interface FallableHelper {
    var pos: Position
    fun getId(): Int
    fun getMeta(): Int

    fun onNearbyBlockChange() {
        pos.world?.let { world ->
            val down = world.getBlock(pos.getSide(Facing.DOWN))
            if (down.getId() == VanillaBlocks.AIR.id /* TODO: || down is Liquid || down is Fire */) {
                world.setBlock(pos, VanillaBlocks.AIR.block)

                if (this !is Block)
                    throw IllegalStateException("FallableHelper should only be used by Blocks")

                /** TODO: Implement after implementing FallingBlock
                 * val fall = FallingBlock(Location.fromObject(pos.add(0.5, 0.0, 0.5), world, 0), this)
                 * fall.spawnToAll()
                 */
            }
        }
    }
}
