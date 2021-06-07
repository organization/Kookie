/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.entity

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World

open class Location @JvmOverloads constructor(
    x: Double,
    y: Double = 0.0,
    z: Double = 0.0,
    var yaw: Double = 0.0,
    var pitch: Double = 0.0,
    world: World? = null
) : Position(x, y, z, world) {
    constructor() : this(0.0)
    constructor(
        x: Int = 0,
        y: Int = 0,
        z: Int = 0,
        yaw: Int = 0,
        pitch: Int = 0,
        world: World? = null
    ) : this(x.toDouble(), y.toDouble(), z.toDouble(), yaw.toDouble(), pitch.toDouble(), world)

    constructor(
        x: Long = 0L,
        y: Long = 0L,
        z: Long = 0L,
        yaw: Long = 0L,
        pitch: Long = 0L,
        world: World? = null
    ) : this(x.toDouble(), y.toDouble(), z.toDouble(), yaw.toDouble(), pitch.toDouble(), world)

    constructor(
        x: Float = 0F,
        y: Float = 0F,
        z: Float = 0F,
        yaw: Float = 0F,
        pitch: Float = 0F,
        world: World? = null
    ) : this(x.toDouble(), y.toDouble(), z.toDouble(), yaw.toDouble(), pitch.toDouble(), world)

    override fun toString(): String =
        "Location (world=${world?.folderName ?: "null"}, x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch)"

    fun asLocation(): Location = Location(x, y, z, yaw, pitch, world)

    companion object {
        fun fromObject(pos: Vector3, world: World?, yaw: Int = 0, pitch: Int = 0): Location =
            fromObject(pos, world, yaw.toDouble(), pitch.toDouble())

        fun fromObject(pos: Vector3, world: World?, yaw: Long = 0L, pitch: Long = 0L): Location =
            fromObject(pos, world, yaw.toDouble(), pitch.toDouble())

        fun fromObject(pos: Vector3, world: World?, yaw: Float = 0F, pitch: Float = 0F): Location =
            fromObject(pos, world, yaw.toDouble(), pitch.toDouble())

        fun fromObject(pos: Vector3, world: World?, yaw: Double = 0.0, pitch: Double = 0.0): Location =
            Location(pos.x, pos.y, pos.z, yaw, pitch, world ?: if (pos is Position) pos.world else null)
    }
}
