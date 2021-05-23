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

import kotlinx.coroutines.runBlocking
import kotlin.math.*

open class Vector3 @JvmOverloads constructor(var x: Float = 0F, var y: Float = 0F, var z: Float = 0F) : Vector {

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

    fun ceil(): Vector3 = Vector3(ceil(x), ceil(y), ceil(z))

    fun floor(): Vector3 = Vector3(floor(x), floor(y), floor(z))

    fun round(): Vector3 = Vector3(round(x), round(y), round(z))

    fun abs(): Vector3 = Vector3(abs(x), abs(y), abs(z))

    @JvmOverloads
    open fun getSide(side: Facing, step: Int = 1): Vector3 = when (side) {
        Facing.DOWN -> Vector3(x, y - step, z)
        Facing.UP -> Vector3(x, y + step, z)
        Facing.NORTH -> Vector3(x, y, z - step)
        Facing.SOUTH -> Vector3(x, y, z + step)
        Facing.WEST -> Vector3(x - step, y, z)
        Facing.EAST -> Vector3(x + step, y, z)
        Facing.CENTER -> this
    }

    @JvmOverloads
    fun down(step: Int = 1): Vector3 = getSide(Facing.DOWN, step)

    @JvmOverloads
    fun up(step: Int = 1): Vector3 = getSide(Facing.UP, step)

    @JvmOverloads
    fun north(step: Int = 1): Vector3 = getSide(Facing.NORTH, step)

    @JvmOverloads
    fun south(step: Int = 1): Vector3 = getSide(Facing.SOUTH, step)

    @JvmOverloads
    fun west(step: Int = 1): Vector3 = getSide(Facing.WEST, step)

    @JvmOverloads
    fun east(step: Int = 1): Vector3 = getSide(Facing.EAST, step)

    @JvmOverloads
    suspend fun sides(step: Int = 1) = sequence {
        Facing.ALL.forEach {
            yield(getSide(it, step))
        }
    }

    @JvmOverloads
    fun sidesArray(step: Int = 1): List<Vector3> = runBlocking {
        return@runBlocking sides(step).toList()
    }

    fun sidesAroundAxis(axis: Int, step: Int = 1) = sequence {
        Facing.ALL.forEach {
            if (Facing.axis(it.value) != axis) {
                yield(Pair(it, getSide(it, step)))
            }
        }
    }

    fun asVector3(): Vector3 = Vector3(x, y, z)

    fun distance(pos: Vector3): Float = sqrt(distanceSquared(pos))

    fun distanceSquared(pos: Vector3): Float = (x - pos.x).pow(2) + (y - pos.y).pow(2) + (z - pos.z).pow(2)

    fun maxPlainDistance(x: Vector, z: Float): Float {
        return when (x) {
            is Vector3 -> {
                maxPlainDistance(x, z)
            }
            is Vector2 -> {
                maxPlainDistance(x, z)
            }
            else -> {
                throw IllegalArgumentException("${x::class.simpleName} is not Vector3 / Vector2")
            }
        }
    }

    fun maxPlainDistance(x: Float, z: Float): Float = max(abs(this.x - x), abs(this.z - z))

    fun length(): Float = sqrt(lengthSquared())

    fun lengthSquared(): Float = x * x + y * y + z * z

    fun normalize(): Vector3 {
        val len = lengthSquared()
        return if (len > 0) {
            this / sqrt(len)
        } else {
            Vector3(0F, 0F, 0F)
        }
    }

    fun dot(v: Vector3): Float = x * v.x + y * v.y + z * v.z

    fun cross(v: Vector3): Vector3 = Vector3(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x
    )

    fun getIntermediateWithXValue(v: Vector3, x: Float): Vector3? {
        val xDiff = v.x - this.x
        if (xDiff * xDiff < 0.0000001) {
            return null
        }
        val f = (x - this.x) / xDiff
        return if (f < 0 || f > 1) {
            null
        } else {
            Vector3(x, y + (v.y - y) * f, z + (v.z - z) * f)
        }
    }

    fun getIntermediateWithYValue(v: Vector3, y: Float): Vector3? {
        val yDiff = v.y - this.y
        if (yDiff * yDiff < 0.0000001) {
            return null
        }
        val f = (y - this.y) / yDiff
        return if (f < 0 || f > 1) {
            null
        } else {
            Vector3(x + (v.x - x), y, z + (v.z - z) * f)
        }
    }

    fun getIntermediateWithZValue(v: Vector3, z: Float): Vector3? {
        val zDiff = v.z - this.z
        if (zDiff * zDiff < 0.0000001) {
            return null
        }
        val f = (z - this.z) / zDiff
        return if (f < 0 || f > 1) {
            null
        } else {
            Vector3(x + (v.x - x), y + (v.y - y) * f, z)
        }
    }

    fun withComponents(x: Float?, y: Float?, z: Float?): Vector3 =
        if (x !== null || y !== null || z !== null) {
            Vector3(x ?: this.x, y ?: this.y, z ?: this.z)
        } else {
            this
        }

    fun maxComponents(vector: Vector3, vararg vectors: Vector3): Vector3 {
        var x = vector.x
        var y = vector.y
        var z = vector.z
        vectors.forEach {
            x = max(x, it.x)
            y = max(y, it.y)
            z = max(z, it.z)
        }
        return Vector3(x, y, z)
    }

    fun minComponents(vector: Vector3, vararg vectors: Vector3): Vector3 {
        var x = vector.x
        var y = vector.y
        var z = vector.z
        vectors.forEach {
            x = min(x, it.x)
            y = min(y, it.y)
            z = min(z, it.z)
        }
        return Vector3(x, y, z)
    }

    fun sum(vararg vector3s: Vector3): Vector3 {
        var x = 0F
        var y = 0F
        var z = 0F
        vector3s.forEach {
            x += it.x
            y += it.y
            z += it.z
        }
        return Vector3(x, y, z)
    }
}
