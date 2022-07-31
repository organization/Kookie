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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.world

import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3

open class Position @JvmOverloads constructor(
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 0.0,
    world: World? = null
) : Vector3(x, y, z) {
    var world: World? = null
        set(value) {
            if (value !== null && value.unloaded) {
                throw IllegalArgumentException("Position world is null or has been unloaded")
            }
            field = value
        }

    init {
        this.world = world
    }

    @JvmOverloads constructor(x: Int, y: Int = 0, z: Int = 0, world: World? = null) :
        this(x.toDouble(), y.toDouble(), z.toDouble(), world)

    @JvmOverloads constructor(x: Long, y: Long = 0L, z: Long = 0L, world: World? = null) :
        this(x.toDouble(), y.toDouble(), z.toDouble(), world)

    @JvmOverloads constructor(x: Float, y: Float = 0F, z: Float = 0F, world: World? = null) :
        this(x.toDouble(), y.toDouble(), z.toDouble(), world)

    override fun toString(): String = "Position (world=${world?.folderName ?: "null"}, x=$x, y=$y, z=$z)"

    fun asPosition(): Position = Position(x, y, z, world)

    fun isValid(): Boolean = world?.let {
        if (it.unloaded) {
            world = null
            false
        } else {
            true
        }
    } ?: false

    override fun clone(): Position {
        return Position(x, y, z, world)
    }

    override fun getSide(side: Facing, step: Int) = fromObject(super.getSide(side, step), world)

    companion object {
        @JvmStatic
        fun fromObject(pos: Vector3, world: World?): Position = Position(pos.x, pos.y, pos.z, world)
    }
}
