package be.zvz.kookie.block.utils

import be.zvz.kookie.world.Position

interface FallableHelper {
    var pos: Position
    fun getId(): Int
    fun getMeta(): Int

    fun onNearbyBlockChange(): Unit {
        var pos = getPos()
        //TODO: not yet implemented
    }
}
