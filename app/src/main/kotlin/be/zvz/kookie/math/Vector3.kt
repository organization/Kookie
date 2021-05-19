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
package be.zvz.kookie.math

class Vector3 @JvmOverloads constructor(var x: Float = 0F, var y: Float = 0F, var z: Float = 0F) {

    override fun equals(other: Any?): Boolean = other is Vector3 && other.x == x && other.y == y && other.z == z

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    @JvmOverloads
    fun add(x: Float, y: Float = 0F, z: Float = 0F): Vector3 = Vector3(this.x + x, this.y + y, this.z + z)

    fun add(x: Vector3): Vector3 = this + x

    operator fun plus(x: Vector3): Vector3 = Vector3(this.x + x.x, this.y + x.y, this.z + x.z)

    @JvmOverloads
    fun subtract(x: Float, y: Float = 0F, z: Float = 0F): Vector3 = Vector3(this.x - x, this.y - y, this.z - z)

    fun subtract(x: Vector3): Vector3 = this - x

    operator fun minus(x: Vector3): Vector3 = Vector3(this.x - x.x, this.y - x.y, this.z - x.z)

    fun multiply(len: Float): Vector3 = this * len

    operator fun times(len: Float): Vector3 = Vector3(x * len, y * len, z * len)

    fun divide(len: Float): Vector3 = this / len

    operator fun div(len: Float): Vector3 {
        if (len == 0F) {
            throw RuntimeException("Division by zero")
        }
        return Vector3(x / len, y / len, z / len)
    }

    @JvmOverloads
    fun getSide(side: Facing, step: Int = 1): Vector3 = when (side) {
        Facing.DOWN -> Vector3(x, y - step, z)
        Facing.UP -> Vector3(x, y + step, z)
        Facing.NORTH -> Vector3(x, y, z - step)
        Facing.SOUTH -> Vector3(x, y, z + step)
        Facing.WEST -> Vector3(x - step, y, z)
        Facing.EAST -> Vector3(x + step, y, z)
        Facing.CENTER -> this
    }

    @JvmOverloads
    suspend fun sides(step: Int = 1) = sequence {
        Facing.ALL.forEach {
            yield(getSide(it, step))
        }
    }
}
