package be.zvz.kookie.block.util

import be.zvz.kookie.world.Position

abstract class FallableTrait {
    protected abstract fun getPos(): Position
    protected abstract fun getId(): Int
    protected abstract fun getMeta(): Int

    fun onNearbyBlockChange(): Unit {
        var pos = getPos()
        //not yet implemented
    }
}
