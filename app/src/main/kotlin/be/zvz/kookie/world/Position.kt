package be.zvz.kookie.world

import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3

open class Position(x: Float = 0F, y: Float = 0F, z: Float = 0F, var world: World? = null) : Vector3(x, y, z) {
    fun asPosition(): Position = Position(x, y, z, world)
    fun isValid(): Boolean = when {
        world !== null && world!!.closed -> {
            world = null
            false
        }
        else -> world !== null
    }
    override fun getSide(side: Facing, step: Int) = fromObject(super.getSide(side, step), world)
    override fun toString(): String = "Position (world=${world?.folderName ?: "null"}, x=$x, y=$y, z=$z)"

    companion object {
        fun fromObject(pos: Vector3, world: World?): Position = Position(pos.x, pos.y, pos.z, world)
    }
}
