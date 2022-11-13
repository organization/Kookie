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
package be.zvz.kookie.math

import kotlin.math.floor

object VoxelRayTrace {

    @JvmStatic
    fun inDirection(start: Vector3, directionVector: Vector3, maxDistance: Double) =
        betweenPoints(start, start.add(directionVector.multiply(maxDistance)))

    @JvmStatic
    fun betweenPoints(start: Vector3, end: Vector3) = sequence {
        var currentBlock = start.floor()

        val directionVector = end.subtract(start).normalize()

        if (directionVector.lengthSquared() <= 0) {
            throw IllegalArgumentException("Start and end points are the same, giving a zero direction vector")
        }

        val radius = start.distance(end)

        val stepX = directionVector.x.compareTo(0).toDouble()
        val stepY = directionVector.y.compareTo(0).toDouble()
        val stepZ = directionVector.z.compareTo(0).toDouble()

        var tMaxX = rayTraceDistanceToBoundary(start.x, directionVector.x)
        var tMaxY = rayTraceDistanceToBoundary(start.y, directionVector.y)
        var tMaxZ = rayTraceDistanceToBoundary(start.z, directionVector.z)

        val tDeltaX = (if (directionVector.x == 0.0) 0 else stepX / directionVector.x).toDouble()
        val tDeltaY = (if (directionVector.y == 0.0) 0 else stepY / directionVector.y).toDouble()
        val tDeltaZ = (if (directionVector.z == 0.0) 0 else stepY / directionVector.z).toDouble()

        while (true) {
            yield(currentBlock)

            if (tMaxX < tMaxY && tMaxX < tMaxZ) {
                if (tMaxX > radius) {
                    break
                }
                currentBlock = currentBlock.add(stepX, 0.0, 0.0)
                tMaxX += tDeltaX
            } else if (tMaxY < tMaxZ) {
                if (tMaxY > radius) {
                    break
                }
                currentBlock = currentBlock.add(0.0, stepY, 0.0)
                tMaxY += tDeltaY
            } else {
                if (tMaxZ > radius) {
                    break
                }
                currentBlock = currentBlock.add(0.0, 0.0, stepZ)
                tMaxZ += tDeltaZ
            }
        }
    }

    @JvmStatic
    fun rayTraceDistanceToBoundary(s: Double, ds: Double): Double {
        if (ds == 0.0) {
            return Double.POSITIVE_INFINITY
        }
        var sT = s
        var dsT = ds
        if (dsT < 0) {
            sT = -sT
            dsT = -dsT
            if (floor(sT) == sT) {
                return 0.0
            }
        }
        return (1 - (sT - floor(sT))) / dsT
    }
}
