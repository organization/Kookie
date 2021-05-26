package be.zvz.kookie.entity

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World

open class Location(x: Float = 0F, y: Float = 0F, z: Float = 0F, var yaw: Float = 0.0f, var pitch: Float = 0.0f, world: World? = null) : Position(x, y, z, world) {

    fun asLocation(): Location = Location(x, y, z, yaw, pitch, world)
    override fun toString(): String = "Location (world=${world?.folderName ?: "null"}, x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch)"

    companion object {
        fun fromObject(pos: Vector3, world: World?, yaw: Float = 0.0f, pitch: Float = 0.0f): Location = Location(pos.x, pos.y, pos.z, yaw, pitch, world ?: if( pos is Position ) pos.world else null)
    }
}