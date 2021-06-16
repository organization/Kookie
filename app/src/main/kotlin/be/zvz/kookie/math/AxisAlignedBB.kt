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

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class AxisAlignedBB constructor(
    var minX: Double,
    var minY: Double,
    var minZ: Double,
    var maxX: Double,
    var maxY: Double,
    var maxZ: Double
) {
    constructor(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int) :
        this(minX.toDouble(), minY.toDouble(), minZ.toDouble(), maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

    init {
        if (minX > maxX) {
            throw IllegalArgumentException("minX minX is larger than maxX maxX")
        }
        if (minY > maxY) {
            throw IllegalArgumentException("minY minY is larger than maxY maxY")
        }
        if (minZ > maxZ) {
            throw IllegalArgumentException("minZ minZ is larger than maxZ maxZ")
        }
    }

    fun clone(): AxisAlignedBB = AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)

    fun addCoord(x: Int, y: Int, z: Int): AxisAlignedBB = addCoord(x.toDouble(), y.toDouble(), z.toDouble())
    fun addCoord(x: Double, y: Double, z: Double): AxisAlignedBB {
        var minX = minX
        var minY = minY
        var minZ = minZ
        var maxX = maxX
        var maxY = maxY
        var maxZ = maxZ

        if (x < 0) {
            minX += x
        } else if (x > 0) {
            maxX += x
        }

        if (y < 0) {
            minY += y
        } else if (y > 0) {
            maxY += y
        }

        if (z < 0) {
            minZ += z
        } else if (z > 0) {
            maxZ += z
        }

        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    fun expand(x: Int, y: Int, z: Int): AxisAlignedBB = expand(x.toDouble(), y.toDouble(), z.toDouble())
    fun expand(x: Double, y: Double, z: Double): AxisAlignedBB = this.apply {
        minX -= x
        minY -= y
        minZ -= z
        maxX += x
        maxY += y
        maxZ += z
    }

    fun expandedCopy(x: Int, y: Int, z: Int): AxisAlignedBB = clone().expand(x, y, z)
    fun expandedCopy(x: Double, y: Double, z: Double): AxisAlignedBB = clone().expand(x, y, z)

    fun offset(x: Int, y: Int, z: Int): AxisAlignedBB = offset(x.toDouble(), y.toDouble(), z.toDouble())
    fun offset(x: Double, y: Double, z: Double): AxisAlignedBB = this.apply {
        minX += x
        minY += y
        minZ += z
        maxX += x
        maxY += y
        maxZ += z
    }

    fun offsetCopy(x: Int, y: Int, z: Int): AxisAlignedBB = clone().offset(x, y, z)
    fun offsetCopy(x: Double, y: Double, z: Double): AxisAlignedBB = clone().offset(x, y, z)

    fun contract(x: Int, y: Int, z: Int): AxisAlignedBB = contract(x.toDouble(), y.toDouble(), z.toDouble())
    fun contract(x: Double, y: Double, z: Double): AxisAlignedBB = this.apply {
        minX -= x
        minY -= y
        minZ -= z
        maxX -= x
        maxY -= y
        maxZ -= z
    }

    fun contractCopy(x: Int, y: Int, z: Int): AxisAlignedBB = clone().contract(x, y, z)
    fun contractCopy(x: Double, y: Double, z: Double): AxisAlignedBB = clone().contract(x, y, z)

    fun extend(face: Facing, distance: Int): AxisAlignedBB = extend(face, distance.toDouble())
    fun extend(face: Facing, distance: Double): AxisAlignedBB = this.apply {
        when (face) {
            Facing.DOWN -> minY -= distance
            Facing.UP -> minY += distance
            Facing.NORTH -> minZ -= distance
            Facing.SOUTH -> minZ += distance
            Facing.WEST -> minX -= distance
            Facing.EAST -> minX += distance
            else -> throw IllegalArgumentException("Invalid face $face")
        }
    }

    fun extendedCopy(face: Facing, distance: Int): AxisAlignedBB = clone().extend(face, distance)
    fun extendedCopy(face: Facing, distance: Double): AxisAlignedBB = clone().extend(face, distance)

    fun trim(face: Facing, distance: Int): AxisAlignedBB = extend(face, -distance)
    fun trim(face: Facing, distance: Double): AxisAlignedBB = extend(face, -distance)

    fun trimmedCopy(face: Facing, distance: Int): AxisAlignedBB = clone().extend(face, -distance)
    fun trimmedCopy(face: Facing, distance: Double): AxisAlignedBB = clone().extend(face, -distance)

    fun stretch(axis: Axis, distance: Int): AxisAlignedBB = stretch(axis, distance.toDouble())
    fun stretch(axis: Axis, distance: Double): AxisAlignedBB = this.apply {
        when (axis) {
            Axis.Y -> {
                minY -= distance
                maxY += distance
            }
            Axis.Z -> {
                minZ -= distance
                maxZ += distance
            }
            Axis.X -> {
                minX -= distance
                maxX += distance
            }
        }
    }

    fun stretchedCopy(axis: Axis, distance: Int): AxisAlignedBB = clone().stretch(axis, distance)
    fun stretchedCopy(axis: Axis, distance: Double): AxisAlignedBB = clone().stretch(axis, distance)

    fun squash(axis: Axis, distance: Int): AxisAlignedBB = stretch(axis, -distance)
    fun squash(axis: Axis, distance: Double): AxisAlignedBB = stretch(axis, -distance)

    fun squashedCopy(axis: Axis, distance: Int): AxisAlignedBB = clone().stretch(axis, -distance)
    fun squashedCopy(axis: Axis, distance: Double): AxisAlignedBB = clone().stretch(axis, -distance)

    fun calculateXOffset(bb: AxisAlignedBB, x: Int): Double = calculateXOffset(bb, x.toDouble())
    fun calculateXOffset(bb: AxisAlignedBB, x: Double): Double = when {
        bb.maxY <= minY || bb.minY >= maxY -> x
        bb.maxZ <= minZ || bb.minZ >= maxZ -> x
        x > 0 && bb.maxX <= minX -> min(minX - bb.maxX, x)
        x < 0 && bb.minX >= maxX -> max(maxX - bb.minX, x)
        else -> x
    }

    fun calculateYOffset(bb: AxisAlignedBB, y: Int): Double = calculateYOffset(bb, y.toDouble())
    fun calculateYOffset(bb: AxisAlignedBB, y: Double): Double = when {
        bb.maxX <= minX || bb.minX >= maxX -> y
        bb.maxZ <= minZ || bb.minZ >= maxZ -> y
        y > 0 && bb.maxY <= minY -> min(minY - bb.maxY, y)
        y < 0 && bb.minY >= maxY -> max(maxY - bb.minY, y)
        else -> y
    }

    fun calculateZOffset(bb: AxisAlignedBB, z: Int): Double = calculateZOffset(bb, z.toDouble())
    fun calculateZOffset(bb: AxisAlignedBB, z: Double): Double = when {
        bb.maxX <= minX || bb.minX >= maxX -> z
        bb.maxY <= minY || bb.minY >= maxY -> z
        z > 0 && bb.maxZ <= minZ -> min(minZ - bb.maxZ, z)
        z < 0 && bb.minZ >= maxZ -> max(maxZ - bb.minZ, z)
        else -> z
    }

    fun intersectsWith(bb: AxisAlignedBB): Boolean = intersectsWith(bb, 0.00001)
    fun intersectsWith(bb: AxisAlignedBB, epsilon: Int): Boolean = intersectsWith(bb, epsilon.toDouble())
    fun intersectsWith(bb: AxisAlignedBB, epsilon: Double): Boolean =
        bb.maxX - minX > epsilon && maxX - bb.minX > epsilon &&
            bb.maxY - minY > epsilon && maxY - bb.minY > epsilon &&
            bb.maxZ - minZ > epsilon && maxZ - bb.minZ > epsilon

    fun isVectorInside(vector: Vector3): Boolean = when {
        vector.x <= minX || vector.x >= maxX -> false
        vector.y <= minY || vector.y >= maxY -> false
        else -> vector.z > minZ && vector.z < maxZ
    }

    fun getAverageEdgeLength(): Double = (maxX - minX + maxY - minY + maxZ - minZ) / 3

    fun getXLength(): Double = (maxX - minX)

    fun getYLength(): Double = (maxY - minY)

    fun getZLength(): Double = (maxZ - minZ)

    fun isCube(): Boolean = isCube(0.000001)
    fun isCube(epsilon: Int): Boolean = isCube(epsilon.toDouble())
    fun isCube(epsilon: Double): Boolean {
        val yLen = getYLength()
        return abs(getXLength() - yLen) < epsilon && abs(yLen - getZLength()) < epsilon
    }

    fun getVolume(): Double = (maxX - minX) * (maxY - minY) * (maxZ - minZ)

    fun isVectorInYZ(vector: Vector3): Boolean = vector.y in minY..maxY && vector.z >= minZ && vector.z <= maxZ

    fun isVectorInXZ(vector: Vector3): Boolean = vector.x in minX..maxX && vector.z >= minZ && vector.z <= maxZ

    fun isVectorInXY(vector: Vector3): Boolean = vector.x in minX..maxX && vector.y >= minY && vector.y <= maxY

    fun calculateIntercept(pos1: Vector3, pos2: Vector3): RayTraceResult? {
        val v1 = pos1.getIntermediateWithXValue(pos2, minX)?.takeIf(this::isVectorInYZ)
        val v2 = pos1.getIntermediateWithXValue(pos2, maxX)?.takeIf(this::isVectorInYZ)
        val v3 = pos1.getIntermediateWithYValue(pos2, minY)?.takeIf(this::isVectorInYZ)
        val v4 = pos1.getIntermediateWithYValue(pos2, maxY)?.takeIf(this::isVectorInYZ)
        val v5 = pos1.getIntermediateWithZValue(pos2, minZ)?.takeIf(this::isVectorInYZ)
        val v6 = pos1.getIntermediateWithZValue(pos2, maxZ)?.takeIf(this::isVectorInYZ)

        var vector: Vector3? = null
        var distance = Double.MAX_VALUE

        arrayOf(v1, v2, v3, v4, v5, v6).forEach { v ->
            v?.let {
                val d: Double = pos1.distanceSquared(v)
                if (d < distance) {
                    vector = v
                    distance = d
                }
            }
        }

        return vector?.let {
            val f = when (it) {
                v1 -> Facing.WEST
                v2 -> Facing.EAST
                v3 -> Facing.DOWN
                v4 -> Facing.UP
                v5 -> Facing.NORTH
                v6 -> Facing.SOUTH
                else -> Facing.CENTER
            }

            RayTraceResult(this, f, it)
        }
    }

    override fun toString(): String = "AxisAlignedBB({$minX}, {$minY}, {$minZ}, {$maxX}, {$maxY}, {$maxZ})"

    companion object {
        @JvmStatic
        fun one(): AxisAlignedBB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }
}
