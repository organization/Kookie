package be.zvz.kookie.math

import kotlin.math.abs

class AxisAlignedBB constructor(var minX: Double, var minY: Double, var minZ: Double, var maxX: Double, var maxY: Double, var maxZ: Double) {
    constructor(minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int) :
            this(minX.toDouble(), minY.toDouble(), minZ.toDouble(), maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

    constructor(minX: Long, minY: Long, minZ: Long, maxX: Long, maxY: Long, maxZ: Long) :
            this(minX.toDouble(), minY.toDouble(), minZ.toDouble(), maxX.toDouble(), maxY.toDouble(), maxZ.toDouble())

    constructor(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float) :
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
    fun addCoord(x: Long, y: Long, z: Long): AxisAlignedBB = addCoord(x.toDouble(), y.toDouble(), z.toDouble())
    fun addCoord(x: Float, y: Float, z: Float): AxisAlignedBB = addCoord(x.toDouble(), y.toDouble(), z.toDouble())
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
    fun expand(x: Long, y: Long, z: Long): AxisAlignedBB = expand(x.toDouble(), y.toDouble(), z.toDouble())
    fun expand(x: Float, y: Float, z: Float): AxisAlignedBB = expand(x.toDouble(), y.toDouble(), z.toDouble())
    fun expand(x: Double, y: Double, z: Double): AxisAlignedBB = this.apply {
        minX -= x
        minY -= y
        minZ -= z
        maxX += x
        maxY += y
        maxZ += z
    }

    fun expandedCopy(x: Int, y: Int, z: Int): AxisAlignedBB = expandedCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun expandedCopy(x: Long, y: Long, z: Long): AxisAlignedBB = expandedCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun expandedCopy(x: Float, y: Float, z: Float): AxisAlignedBB = expandedCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun expandedCopy(x: Double, y: Double, z: Double): AxisAlignedBB = clone().expand(x, y, z)


    fun offset(x: Int, y: Int, z: Int): AxisAlignedBB = offset(x.toDouble(), y.toDouble(), z.toDouble())
    fun offset(x: Long, y: Long, z: Long): AxisAlignedBB = offset(x.toDouble(), y.toDouble(), z.toDouble())
    fun offset(x: Float, y: Float, z: Float): AxisAlignedBB = offset(x.toDouble(), y.toDouble(), z.toDouble())
    fun offset(x: Double, y: Double, z: Double): AxisAlignedBB = this.apply {
        minX += x
        minY += y
        minZ += z
        maxX += x
        maxY += y
        maxZ += z
    }

    fun offestCopy(x: Int, y: Int, z: Int): AxisAlignedBB = offsetCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun offestCopy(x: Long, y: Long, z: Long): AxisAlignedBB = offsetCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun offestCopy(x: Float, y: Float, z: Float): AxisAlignedBB = offsetCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun offsetCopy(x: Double, y: Double, z: Double): AxisAlignedBB = clone().offset(x, y, z)


    fun contract(x: Int, y: Int, z: Int): AxisAlignedBB = contract(x.toDouble(), y.toDouble(), z.toDouble())
    fun contract(x: Long, y: Long, z: Long): AxisAlignedBB = contract(x.toDouble(), y.toDouble(), z.toDouble())
    fun contract(x: Float, y: Float, z: Float): AxisAlignedBB = contract(x.toDouble(), y.toDouble(), z.toDouble())
    fun contract(x: Double, y: Double, z: Double): AxisAlignedBB = this.apply {
        minX -= x
        minY -= y
        minZ -= z
        maxX -= x
        maxY -= y
        maxZ -= z
    }

    fun contractCopy(x: Int, y: Int, z: Int): AxisAlignedBB = contractCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun contractCopy(x: Long, y: Long, z: Long): AxisAlignedBB = contractCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun contractCopy(x: Float, y: Float, z: Float): AxisAlignedBB = contractCopy(x.toDouble(), y.toDouble(), z.toDouble())
    fun contractCopy(x: Double, y: Double, z: Double): AxisAlignedBB = clone().contract(x, y, z)


    fun extend(face: Facing, distance: Int): AxisAlignedBB = extend(face, distance.toDouble())
    fun extend(face: Facing, distance: Long): AxisAlignedBB = extend(face, distance.toDouble())
    fun extend(face: Facing, distance: Float): AxisAlignedBB = extend(face, distance.toDouble())
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

    fun extendedCopy(face: Facing, distance: Int): AxisAlignedBB = extendedCopy(face, distance.toDouble())
    fun extendedCopy(face: Facing, distance: Long): AxisAlignedBB = extendedCopy(face, distance.toDouble())
    fun extendedCopy(face: Facing, distance: Float): AxisAlignedBB = extendedCopy(face, distance.toDouble())
    fun extendedCopy(face: Facing, distance: Double): AxisAlignedBB = clone().extend(face, distance)


    fun trim(face: Facing, distance: Int): AxisAlignedBB = trim(face, distance.toDouble())
    fun trim(face: Facing, distance: Long): AxisAlignedBB = trim(face, distance.toDouble())
    fun trim(face: Facing, distance: Float): AxisAlignedBB = trim(face, distance.toDouble())
    fun trim(face: Facing, distance: Double): AxisAlignedBB = extend(face, -distance)

    fun trimmedCopy(face: Facing, distance: Int): AxisAlignedBB = trimmedCopy(face, distance.toDouble())
    fun trimmedCopy(face: Facing, distance: Long): AxisAlignedBB = trimmedCopy(face, distance.toDouble())
    fun trimmedCopy(face: Facing, distance: Float): AxisAlignedBB = trimmedCopy(face, distance.toDouble())
    fun trimmedCopy(face: Facing, distance: Double): AxisAlignedBB = extendedCopy(face, distance)


    fun stretch(axis: Axis, distance: Int): AxisAlignedBB = stretch(axis, distance.toDouble())
    fun stretch(axis: Axis, distance: Long): AxisAlignedBB = stretch(axis, distance.toDouble())
    fun stretch(axis: Axis, distance: Float): AxisAlignedBB = stretch(axis, distance.toDouble())
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

    fun stretchedCopy(axis: Axis, distance: Int): AxisAlignedBB = stretchedCopy(axis, distance.toDouble())
    fun stretchedCopy(axis: Axis, distance: Long): AxisAlignedBB = stretchedCopy(axis, distance.toDouble())
    fun stretchedCopy(axis: Axis, distance: Float): AxisAlignedBB = stretchedCopy(axis, distance.toDouble())
    fun stretchedCopy(axis: Axis, distance: Double): AxisAlignedBB = clone().stretch(axis, distance)


    fun sqaush(axis: Axis, distance: Int): AxisAlignedBB = squash(axis, distance.toDouble())
    fun sqaush(axis: Axis, distance: Long): AxisAlignedBB = squash(axis, distance.toDouble())
    fun sqaush(axis: Axis, distance: Float): AxisAlignedBB = squash(axis, distance.toDouble())
    fun squash(axis: Axis, distance: Double): AxisAlignedBB = stretch(axis, -distance)

    fun sqaushedCopy(axis: Axis, distance: Int): AxisAlignedBB = squashedCopy(axis, distance.toDouble())
    fun sqaushedCopy(axis: Axis, distance: Long): AxisAlignedBB = squashedCopy(axis, distance.toDouble())
    fun sqaushedCopy(axis: Axis, distance: Float): AxisAlignedBB = squashedCopy(axis, distance.toDouble())
    fun squashedCopy(axis: Axis, distance: Double): AxisAlignedBB = stretchedCopy(axis, -distance)


    fun calculateXOffset(bb: AxisAlignedBB, x: Int): Double = calculateXOffset(bb, x.toDouble())
    fun calculateXOffset(bb: AxisAlignedBB, x: Long): Double = calculateXOffset(bb, x.toDouble())
    fun calculateXOffset(bb: AxisAlignedBB, x: Float): Double = calculateXOffset(bb, x.toDouble())
    fun calculateXOffset(bb: AxisAlignedBB, x: Double): Double = when {
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

    fun calculateYOffset(bb: AxisAlignedBB, y: Int): Double = calculateYOffset(bb, y.toDouble())
    fun calculateYOffset(bb: AxisAlignedBB, y: Long): Double = calculateYOffset(bb, y.toDouble())
    fun calculateYOffset(bb: AxisAlignedBB, y: Float): Double = calculateYOffset(bb, y.toDouble())
    fun calculateYOffset(bb: AxisAlignedBB, y: Double): Double = when {
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

    fun calculateZOffset(bb: AxisAlignedBB, z: Int): Double = calculateZOffset(bb, z.toDouble())
    fun calculateZOffset(bb: AxisAlignedBB, z: Long): Double = calculateZOffset(bb, z.toDouble())
    fun calculateZOffset(bb: AxisAlignedBB, z: Float): Double = calculateZOffset(bb, z.toDouble())
    fun calculateZOffset(bb: AxisAlignedBB, z: Double): Double = when {
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

    fun intersectsWith(bb: AxisAlignedBB): Boolean = intersectsWith(bb, 0.00001)
    fun intersectsWith(bb: AxisAlignedBB, epsilon: Int): Boolean = intersectsWith(bb, epsilon.toDouble())
    fun intersectsWith(bb: AxisAlignedBB, epsilon: Long): Boolean = intersectsWith(bb, epsilon.toDouble())
    fun intersectsWith(bb: AxisAlignedBB, epsilon: Float = 0.00001f): Boolean = intersectsWith(bb, epsilon.toDouble())
    fun intersectsWith(bb: AxisAlignedBB, epsilon: Double = 0.00001): Boolean {
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

    fun getAverageEdgeLength(): Double {
        return (maxX - minX + maxY - minY + maxZ - minZ) / 3
    }

    fun getXLength(): Double = maxX - minX

    fun getYLength(): Double = maxY - minY

    fun getZLength(): Double = maxZ - minZ

    fun isCube(): Boolean = isCube(0.000001)
    fun isCube(epsilon: Int): Boolean = isCube(epsilon.toDouble())
    fun isCube(epsilon: Long): Boolean = isCube(epsilon.toDouble())
    fun isCube(epsilon: Float = 0.000001f): Boolean = isCube(epsilon.toDouble())
    fun isCube(epsilon: Double = 0.000001): Boolean {
        val yLen = getYLength()
        return abs(getXLength() - yLen) < epsilon && abs(yLen - getZLength()) < epsilon
    }

    fun getVolume(): Double = (maxX - minX) * (maxY - minY) * (maxZ - minZ)

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

    override fun toString(): String {
        return "AxisAlignedBB({$minX}, {$minY}, {$minZ}, {$maxX}, {$maxY}, {$maxZ})"
    }

    companion object {
        fun one(): AxisAlignedBB = AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f)
    }
}
