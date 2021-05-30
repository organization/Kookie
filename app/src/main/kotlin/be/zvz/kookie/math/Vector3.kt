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

open class Vector3 @JvmOverloads constructor(var x: Double, var y: Double = 0.0, var z: Double = 0.0) : Vector {
    constructor() : this(0.0)
    constructor(x: Int = 0, y: Int = 0, z: Int = 0) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Long = 0L, y: Long = 0L, z: Long = 0L) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float = 0F, y: Float = 0F, z: Float = 0F) : this(x.toDouble(), y.toDouble(), z.toDouble())

    override fun equals(other: Any?): Boolean = other is Vector3 && other.x == x && other.y == y && other.z == z

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    operator fun plus(pos: Vector3): Vector3 = add(pos)
    fun add(pos: Vector3): Vector3 = add(pos.x, pos.y, pos.z)

    @JvmOverloads
    fun add(x: Int, y: Int = 0, z: Int = 0): Vector3 = add(x.toDouble(), y.toDouble(), z.toDouble())

    @JvmOverloads
    fun add(x: Long, y: Long = 0L, z: Long = 0L): Vector3 = add(x.toDouble(), y.toDouble(), z.toDouble())

    @JvmOverloads
    fun add(x: Float, y: Float = 0F, z: Float = 0F): Vector3 = add(x.toDouble(), y.toDouble(), z.toDouble())

    @JvmOverloads
    fun add(x: Double, y: Double = 0.0, z: Double = 0.0): Vector3 = Vector3(this.x + x, this.y + y, this.z + z)


    operator fun minus(pos: Vector3): Vector3 = subtract(pos)
    fun subtract(pos: Vector3): Vector3 = subtract(pos.x, pos.y, pos.z)

    @JvmOverloads
    fun subtract(x: Int, y: Int = 0, z: Int = 0): Vector3 = subtract(x.toDouble(), y.toDouble(), z.toDouble())

    @JvmOverloads
    fun subtract(x: Long, y: Long = 0L, z: Long = 0L): Vector3 = subtract(x.toDouble(), y.toDouble(), z.toDouble())

    @JvmOverloads
    fun subtract(x: Float, y: Float = 0F, z: Float = 0F): Vector3 = subtract(x.toDouble(), y.toDouble(), z.toDouble())

    @JvmOverloads
    fun subtract(x: Double, y: Double = 0.0, z: Double = 0.0): Vector3 = Vector3(this.x - x, this.y - y, this.z - z)


    operator fun times(len: Int): Vector3 = multiply(len)
    operator fun times(len: Long): Vector3 = multiply(len)
    operator fun times(len: Float): Vector3 = multiply(len)
    operator fun times(len: Double): Vector3 = multiply(len)
    fun multiply(len: Int): Vector3 = multiply(len.toDouble())
    fun multiply(len: Long): Vector3 = multiply(len.toDouble())
    fun multiply(len: Float): Vector3 = multiply(len.toDouble())
    fun multiply(len: Double): Vector3 = Vector3(x * len, y * len, z * len)

    operator fun div(len: Int): Vector3 = divide(len.toDouble())
    operator fun div(len: Long): Vector3 = divide(len.toDouble())
    operator fun div(len: Float): Vector3 = divide(len.toDouble())
    operator fun div(len: Double): Vector3 = divide(len)
    fun divide(len: Int): Vector3 = divide(len.toDouble())
    fun divide(len: Long): Vector3 = divide(len.toDouble())
    fun divide(len: Float): Vector3 = divide(len.toDouble())
    fun divide(len: Double): Vector3 = if (len == 0.0) throw RuntimeException("Division by zero") else Vector3(x / len, y / len, z / len)

    fun ceil(): Vector3 = Vector3(ceil(x), ceil(y), ceil(z))
    fun floor(): Vector3 = Vector3(floor(x), floor(y), floor(z))
    fun round(): Vector3 = Vector3(round(x), round(y), round(z))
    fun abs(): Vector3 = Vector3(abs(x), abs(y), abs(z))

    fun distance(pos: Vector3): Double = distance(pos.x, pos.y, pos.z)
    fun distance(x: Int, y: Int, z: Int): Double = distance(x.toDouble(), y.toDouble(), z.toDouble())
    fun distance(x: Long, y: Long, z: Long): Double = distance(x.toDouble(), y.toDouble(), z.toDouble())
    fun distance(x: Float, y: Float, z: Float): Double = distance(x.toDouble(), y.toDouble(), z.toDouble())
    fun distance(x: Double, y: Double, z: Double): Double = sqrt(distanceSquared(x, y, z))

    fun distanceSquared(pos: Vector3): Double = distanceSquared(pos.x, pos.y, pos.z)
    fun distanceSquared(x: Int, y: Int, z: Int): Double = distance(x.toDouble(), y.toDouble(), z.toDouble())
    fun distanceSquared(x: Long, y: Long, z: Long): Double = distance(x.toDouble(), y.toDouble(), z.toDouble())
    fun distanceSquared(x: Float, y: Float, z: Float): Double = distance(x.toDouble(), y.toDouble(), z.toDouble())
    fun distanceSquared(x: Double, y: Double, z: Double): Double = (this.x - x).pow(2) + (this.y - y).pow(2) + (this.z - z).pow(2)

    fun length(): Double = sqrt(lengthSquared())
    fun lengthSquared(): Double = x * x + y * y + z * z

    fun normalize(): Vector3 {
        val len = lengthSquared()
        return if (len > 0) {
            this / sqrt(len)
        } else {
            Vector3()
        }
    }

    fun dot(v: Vector3): Double = x * v.x + y * v.y + z * v.z

    fun asVector3(): Vector3 = Vector3(x, y, z)


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
    fun sides(step: Int = 1) = sequence {
        Facing.ALL.forEach {
            yield(getSide(it, step))
        }
    }


    @JvmOverloads
    fun sidesArray(step: Int = 1): List<Vector3> = runBlocking {
        return@runBlocking sides(step).toList()
    }

    fun sidesAroundAxis(axis: Axis, step: Int = 1) = sequence {
        Facing.ALL.forEach {
            if (Facing.axis(it.value) != axis.value) {
                yield(Pair(it, getSide(it, step)))
            }
        }
    }


    fun maxPlainDistance(v: Vector, z: Int): Double = maxPlainDistance(v, z.toDouble())
    fun maxPlainDistance(v: Vector, z: Long): Double = maxPlainDistance(v, z.toDouble())
    fun maxPlainDistance(v: Vector, z: Float): Double = maxPlainDistance(v, z.toDouble())
    fun maxPlainDistance(v: Vector, z: Double): Double = when (v) {
        is Vector3 -> maxPlainDistance(v.x, z)
        is Vector2 -> maxPlainDistance(v.x, z)
        else -> throw IllegalArgumentException("${v::class.simpleName} is not Vector3 / Vector2")
    }

    fun maxPlainDistance(x: Double, z: Double): Double = max(abs(this.x - x), abs(this.z - z))


    fun cross(v: Vector3): Vector3 = Vector3(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x
    )

    fun getIntermediateWithXValue(v: Vector3, x: Int): Vector3? = getIntermediateWithXValue(v, x.toDouble())
    fun getIntermediateWithXValue(v: Vector3, x: Long): Vector3? = getIntermediateWithXValue(v, x.toDouble())
    fun getIntermediateWithXValue(v: Vector3, x: Float): Vector3? = getIntermediateWithXValue(v, x.toDouble())
    fun getIntermediateWithXValue(v: Vector3, x: Double): Vector3? {
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


    fun getIntermediateWithYValue(v: Vector3, y: Int): Vector3? = getIntermediateWithYValue(v, y.toDouble())
    fun getIntermediateWithYValue(v: Vector3, y: Long): Vector3? = getIntermediateWithYValue(v, y.toDouble())
    fun getIntermediateWithYValue(v: Vector3, y: Float): Vector3? = getIntermediateWithYValue(v, y.toDouble())
    fun getIntermediateWithYValue(v: Vector3, y: Double): Vector3? {
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


    fun getIntermediateWithZValue(v: Vector3, z: Int): Vector3? = getIntermediateWithZValue(v, z.toDouble())
    fun getIntermediateWithZValue(v: Vector3, z: Long): Vector3? = getIntermediateWithZValue(v, z.toDouble())
    fun getIntermediateWithZValue(v: Vector3, z: Float): Vector3? = getIntermediateWithZValue(v, z.toDouble())
    fun getIntermediateWithZValue(v: Vector3, z: Double): Vector3? {
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


    fun withComponents(x: Int?, y: Int?, z: Int?): Vector3 = withComponents(x?.toDouble(), y?.toDouble(), z?.toDouble())
    fun withComponents(x: Long?, y: Long?, z: Long?): Vector3 = withComponents(x?.toDouble(), y?.toDouble(), z?.toDouble())
    fun withComponents(x: Float?, y: Float?, z: Float?): Vector3 = withComponents(x?.toDouble(), y?.toDouble(), z?.toDouble())
    fun withComponents(x: Double?, y: Double?, z: Double?): Vector3 =
        if (x !== null || y !== null || z !== null)
            Vector3(x ?: this.x, y ?: this.y, z ?: this.z)
        else this.asVector3()

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
        var x = 0.0
        var y = 0.0
        var z = 0.0
        vector3s.forEach {
            x += it.x
            y += it.y
            z += it.z
        }
        return Vector3(x, y, z)
    }
}
