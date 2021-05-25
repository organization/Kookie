package be.zvz.kookie.math

import java.lang.IllegalArgumentException
import kotlin.math.abs

class AxisAlignedBB(var minX: Float, var minY: Float, var minZ: Float, var maxX: Float, var maxY: Float, var maxZ: Float) {

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

    fun clone(): AxisAlignedBB {
        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    fun addCoord(x: Float, y: Float, z: Float): AxisAlignedBB {
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

    fun expand(x: Float, y: Float, z: Float): AxisAlignedBB {
        minX -= x
        minY -= y
        minZ -= z
        maxX += x
        maxY += y
        maxZ += z

        return this
    }

    fun expandedCopy(x: Float, y: Float, z: Float): AxisAlignedBB {
        return clone().expand(x, y, z)
    }

    fun offset(x: Float, y: Float, z: Float): AxisAlignedBB {
        minX += x
        minY += y
        minZ += z
        maxX += x
        maxY += y
        maxZ += z

        return this
    }

    fun offsetCopy(x: Float, y: Float, z: Float): AxisAlignedBB {
        return clone().offset(x, y, z)
    }

    fun contract(x: Float, y: Float, z: Float): AxisAlignedBB {
        minX -= x
        minY -= y
        minZ -= z
        maxX -= x
        maxY -= y
        maxZ -= z

        return this
    }

    fun contractCopy(x: Float, y: Float, z: Float): AxisAlignedBB {
        return clone().contract(x, y, z)
    }

    fun extend(face: Int, distance: Float): AxisAlignedBB {
        if (face == Facing.DOWN.value) {
            minY -= distance
        } else if (face == Facing.UP.value) {
            maxY += distance
        } else if (face == Facing.NORTH.value) {
            minZ -= distance
        } else if (face == Facing.SOUTH.value) {
            maxZ += distance
        } else if (face == Facing.WEST.value) {
            minX -= distance
        } else if (face == Facing.EAST.value) {
            maxX += distance
        } else {
            throw IllegalArgumentException("Invalid face face")
        }

        return this
    }

    fun extendedCopy(face: Int, distance: Float): AxisAlignedBB {
        return clone().extend(face, distance)
    }

    fun trim(face: Int, distance: Float): AxisAlignedBB {
        return extend(face, -distance)
    }

    fun trimmedCopy(face: Int, distance: Float): AxisAlignedBB {
        return extendedCopy(face, distance)
    }

    fun stretch(axis: Int, distance: Float): AxisAlignedBB {
        if (axis == Axis.Y.value) {
            minY -= distance
            maxY += distance
        } else if (axis == Axis.Z.value) {
            minZ -= distance
            maxZ += distance
        } else if (axis == Axis.X.value) {
            minX -= distance
            maxX += distance
        } else {
            throw IllegalArgumentException("Invalid axis axis")
        }
        return this
    }

    fun stretchedCopy(axis: Int, distance: Float): AxisAlignedBB {
        return clone().stretch(axis, distance)
    }

    fun squash(axis: Int, distance: Float): AxisAlignedBB {
        return stretch(axis, -distance)
    }

    fun squashedCopy(axis: Int, distance: Float): AxisAlignedBB {
        return stretchedCopy(axis, -distance)
    }

    fun calculateXOffset(bb: AxisAlignedBB, x: Float): Float = when {
        bb.maxY <= minY || bb.minY >= maxY -> x
        bb.maxZ <= minZ || bb.minZ >= maxZ -> x
        x > 0 && bb.maxX <= minX -> {
            val x1 = minX - bb.maxX
            if (x1 < x) x1
            else x
        }
        x < 0 && bb.minX >= maxX -> {
            val x2 = maxX - bb.minX
            if (x2 > x) x2
            else x
        }
        else -> x
    }

    fun calculateYOffset(bb: AxisAlignedBB, y: Float): Float = when {
        bb.maxX <= minX || bb.minX >= maxX -> y
        bb.maxZ <= minZ || bb.minZ >= maxZ -> y
        y > 0 && bb.maxY <= minY -> {
            val y1 = minY - bb.maxY
            if (y1 < y) y1
            else y
        }
        y < 0 && bb.minY >= maxY -> {
            val y2 = maxY - bb.minY
            if (y2 > y) y2
            else y
        }
        else -> y
    }

    fun calculateZOffset(bb: AxisAlignedBB, z: Float): Float = when {
        bb.maxX <= minX || bb.minX >= maxX -> z
        bb.maxY <= minY || bb.minY >= maxY -> z
        z > 0 && bb.maxZ <= minZ -> {
            val z1 = minZ - bb.maxZ
            if (z1 < z) z1
            else z
        }
        z < 0 && bb.minZ >= maxZ -> {
            val z2 = maxZ - bb.minZ
            if (z2 > z) z2
            else z
        }
        else -> z
    }

    fun intersectsWith(bb: AxisAlignedBB, epsilon: Float = 0.00001f): Boolean {
        if (bb.maxX - minX > epsilon && maxX - bb.minX > epsilon) {
            if (bb.maxY - minY > epsilon && maxY - bb.minY > epsilon) {
                return bb.maxZ - minZ > epsilon && maxZ - bb.minZ > epsilon
            }
        }

        return false
    }

    fun isVectorInside(vector: Vector3): Boolean = when {
        vector.x <= minX || vector.x >= maxX -> false
        vector.y <= minY || vector.y >= maxY -> false
        else -> vector.z > minZ && vector.z < maxZ
    }

    fun getAverageEdgeLength(): Float {
        return (maxX - minX + maxY - minY + maxZ - minZ) / 3
    }

    fun getXLength(): Float = maxX - minX

    fun getYLength(): Float = maxY - minY

    fun getZLength(): Float = maxZ - minZ

    fun isCube(epsilon: Float = 0.000001f): Boolean {
        val yLen = getYLength()
        return abs(getXLength() - yLen) < epsilon && abs(yLen - getZLength()) < epsilon
    }

    fun getVolume(): Float = (maxX - minX) * (maxY - minY) * (maxZ - minZ)

    fun isVectorInYZ(vector: Vector3): Boolean = vector.y in minY..maxY && vector.z >= minZ && vector.z <= maxZ

    fun isVectorInXZ(vector: Vector3): Boolean = vector.x in minX..maxX && vector.z >= minZ && vector.z <= maxZ

    fun isVectorInXY(vector: Vector3): Boolean = vector.x in minX..maxX && vector.y >= minY && vector.y <= maxY

    fun calculateIntercept(pos1: Vector3, pos2: Vector3): RayTraceResult? {
        var v1 = pos1.getIntermediateWithXValue(pos2, minX)
        var v2 = pos1.getIntermediateWithXValue(pos2, maxX)
        var v3 = pos1.getIntermediateWithYValue(pos2, minY)
        var v4 = pos1.getIntermediateWithYValue(pos2, maxY)
        var v5 = pos1.getIntermediateWithZValue(pos2, minZ)
        var v6 = pos1.getIntermediateWithZValue(pos2, maxZ)

        if (v1 !== null && !isVectorInYZ(v1)) {
            v1 = null
        }

        if (v2 !== null && !isVectorInYZ(v2)) {
            v2 = null
        }

        if (v3 !== null && !isVectorInXZ(v3)) {
            v3 = null
        }

        if (v4 !== null && !isVectorInXZ(v4)) {
            v4 = null
        }

        if (v5 !== null && !isVectorInXY(v5)) {
            v5 = null
        }

        if (v6 !== null && !isVectorInXY(v6)) {
            v6 = null
        }

        var vector: Vector3? = null
        var distance = Float.MAX_VALUE

        arrayOf(v1, v2, v3, v4, v5, v6).forEach { v ->
            v?.let {
                val d: Float = pos1.distanceSquared(v)
                if (d < distance) {
                    vector = v
                    distance = d
                }
            }
        }

        return vector?.let {
            val f = when (it) {
                v1 -> {
                    Facing.WEST
                }
                v2 -> {
                    Facing.EAST
                }
                v3 -> {
                    Facing.DOWN
                }
                v4 -> {
                    Facing.UP
                }
                v5 -> {
                    Facing.NORTH
                }
                v6 -> {
                    Facing.SOUTH
                }
                else -> {
                    Facing.CENTER
                }
            }

            RayTraceResult(this, f, it)
        }
    }

    override fun toString(): String {
        return "AxisAlignedBB({$minX}, {$minY}, {$minZ}, {$maxX}, {$maxY}, {$maxZ})"
    }

    companion object {
        fun one(): AxisAlignedBB = AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f)
    }
}
