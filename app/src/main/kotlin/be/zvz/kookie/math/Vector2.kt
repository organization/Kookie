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

import kotlin.math.*

class Vector2 @JvmOverloads constructor(var x: Double, var y: Double = 0.0) : Vector {
    constructor() : this(0.0)
    constructor(x: Int = 0, y: Int = 0) : this(x.toDouble(), y.toDouble())
    constructor(x: Long = 0L, y: Long = 0L) : this(x.toDouble(), y.toDouble())
    constructor(x: Float = 0F, y: Float = 0F) : this(x.toDouble(), y.toDouble())

    override fun equals(other: Any?): Boolean = other is Vector2 && other.x == x && other.y == y

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    operator fun plus(pos: Vector2): Vector2 = add(pos)
    fun add(pos: Vector2): Vector2 = add(pos.x, pos.y)

    @JvmOverloads
    fun add(x: Int, y: Int = 0): Vector2 = add(x.toDouble(), y.toDouble())

    @JvmOverloads
    fun add(x: Long, y: Long = 0L): Vector2 = add(x.toDouble(), y.toDouble())

    @JvmOverloads
    fun add(x: Float, y: Float = 0F): Vector2 = add(x.toDouble(), y.toDouble())

    @JvmOverloads
    fun add(x: Double, y: Double = 0.0): Vector2 = Vector2(this.x + x, this.y + y)


    operator fun minus(pos: Vector2): Vector2 = subtract(pos)
    fun subtract(pos: Vector2): Vector2 = subtract(pos.x, pos.y)

    @JvmOverloads
    fun subtract(x: Int, y: Int = 0): Vector2 = subtract(x.toDouble(), y.toDouble())

    @JvmOverloads
    fun subtract(x: Long, y: Long = 0L): Vector2 = subtract(x.toDouble(), y.toDouble())

    @JvmOverloads
    fun subtract(x: Float, y: Float = 0F): Vector2 = subtract(x.toDouble(), y.toDouble())

    @JvmOverloads
    fun subtract(x: Double, y: Double = 0.0): Vector2 = Vector2(this.x + x, this.y + y)


    operator fun times(len: Int): Vector2 = multiply(len)
    operator fun times(len: Long): Vector2 = multiply(len)
    operator fun times(len: Float): Vector2 = multiply(len)
    operator fun times(len: Double): Vector2 = multiply(len)
    fun multiply(len: Int): Vector2 = multiply(len.toDouble())
    fun multiply(len: Long): Vector2 = multiply(len.toDouble())
    fun multiply(len: Float): Vector2 = multiply(len.toDouble())
    fun multiply(len: Double): Vector2 = Vector2(x * len, y * len)


    operator fun div(len: Int): Vector2 = divide(len)
    operator fun div(len: Long): Vector2 = divide(len)
    operator fun div(len: Float): Vector2 = divide(len)
    operator fun div(len: Double): Vector2 = divide(len)
    fun divide(len: Int): Vector2 = divide(len.toDouble())
    fun divide(len: Long): Vector2 = divide(len.toDouble())
    fun divide(len: Float): Vector2 = divide(len.toDouble())
    fun divide(len: Double): Vector2 = if (len == 0.0) throw RuntimeException("Division by zero") else Vector2(x / len.toDouble(), y / len.toDouble())

    fun ceil(): Vector2 = Vector2(ceil(x), ceil(y))
    fun floor(): Vector2 = Vector2(floor(x), floor(y))
    fun round(): Vector2 = Vector2(round(x), round(y))
    fun abs(): Vector2 = Vector2(abs(x), abs(y))

    fun distance(pos: Vector2): Double = distance(x, y)
    fun distance(x: Double, y: Double): Double = sqrt(distanceSquared(x, y))
    fun distance(x: Number, y: Number): Double = distance(x.toDouble(), y.toDouble())

    fun distanceSquared(pos: Vector2): Double = distanceSquared(pos.x, pos.y)
    fun distanceSquared(x: Double, y: Double): Double = (this.x - x).pow(2) + (this.y - y).pow(2)
    fun distanceSquared(x: Number, y: Number): Double = distanceSquared(x.toDouble(), y.toDouble())

    fun length(): Double = sqrt(lengthSquared())
    fun lengthSquared(): Double = x * x + y * y

    fun normalize(): Vector2 {
        val len = lengthSquared()
        return if (len > 0.0) (this / sqrt(len)) else Vector2(0.0, 0.0)
    }

    fun dot(v: Vector2): Double = x * v.x + y * v.y
}
