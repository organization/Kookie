package be.zvz.kookie.block.util

import be.zvz.kookie.world.Position

interface FallableHelper {
    fun getPos(): Position
    fun getId(): Int
    fun getMeta(): Int

    fun onNearbyBlockChange(): Unit {
        var pos = getPos()
        //not yet implemented
    }
}
