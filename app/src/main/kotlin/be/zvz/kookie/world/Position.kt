package be.zvz.kookie.world

import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3

open class Position @JvmOverloads constructor(
    x: Float,
    y: Float = 0F,
    z: Float = 0F,
    var world: World? = null
) : Vector3(x, y, z) {
    constructor() : this(0.0)
    constructor(x: Int = 0, y: Int = 0, z: Int = 0, world: World? = null) : this(
        x.toFloat(),
        y.toFloat(),
        z.toFloat(),
        world
    )

    constructor(x: Long = 0L, y: Long = 0L, z: Long = 0L, world: World? = null) : this(
        x.toFloat(),
        y.toFloat(),
        z.toFloat(),
        world
    )

    constructor(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, world: World? = null) : this(
        x.toFloat(),
        y.toFloat(),
        z.toFloat(),
        world
    )

    override fun toString(): String = "Position (world=${world?.folderName ?: "null"}, x=$x, y=$y, z=$z)"

    fun asPosition(): Position = Position(x, y, z, world)

    fun isValid(): Boolean = when {
        world !== null && world!!.closed -> {
            world = null
            false
        }
        else -> world !== null
    }

    override fun getSide(side: Facing, step: Int) = fromObject(super.getSide(side, step), world)

    companion object {
        fun fromObject(pos: Vector3, world: World?): Position = Position(pos.x, pos.y, pos.z, world)
    }
}
