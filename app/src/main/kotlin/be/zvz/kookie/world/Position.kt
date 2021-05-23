package be.zvz.kookie.world

import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3

class Position(x: Float = 0F, y: Float = 0F, z: Float = 0F, val world: World? = null) : Vector3(x, y, z) {
    fun asPosition(): Position = Position(x, y, z, world)
    override fun getSide(side: Facing, step: Int) = fromObject(super.getSide(side, step), world)
    override fun toString(): String = "Position(level=${world?.folderName ?: "null"},x=$x,y=$y,z=$z)"

    companion object {
        fun fromObject(pos: Vector3, world: World?): Position = Position(pos.x, pos.y, pos.z, world)
    }
}
