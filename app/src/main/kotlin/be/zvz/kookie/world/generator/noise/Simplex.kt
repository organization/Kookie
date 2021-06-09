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
package be.zvz.kookie.world.generator.noise

import java.util.Random
import kotlin.math.sqrt

class Simplex(
    random: Random,
    octaves: Int,
    persistence: Float,
    expansion: Float
) : Noise(octaves, persistence, expansion) {

    protected val offsetX: Float
    protected val offsetZ: Float
    protected val offsetY: Float
    protected val perm = IntArray(512) { 0 }

    init {
        offsetX = random.nextFloat() * 256
        offsetY = random.nextFloat() * 256
        offsetZ = random.nextFloat() * 256

        repeat(256) { i ->
            perm[i] = random.nextInt() % 256
        }
        repeat(256) { i ->
            val old = random.nextInt() % 256
            val pos = random.nextInt() % (256 - i) + i
            perm[i] = perm[pos]
            perm[pos] = old
            perm[i + 256] = perm[i]
        }

        // this dummy call is necessary to produce the same RNG state as before latest refactors to this file
        // previously this value would be used for offsetW
        // this really needs to reset the RNG seed to avoid future RNG contamination
        random.nextInt()
    }

    override fun getNoise2D(x: Float, z: Float): Float {
        val xx = x + offsetX
        val yy = z + offsetY

        // Skew the input space to determine which simplex cell we're in
        val s = (xx + yy) * F2 // Hairy factor for 2D
        val i = (xx + s).toInt()
        val j = (yy + s).toInt()
        val t = (i + j) * G2
        // Unskew the cell origin back to (x,y) space
        val x0 = xx - (i - t) // The x,y distances from the cell origin
        val y0 = yy - (j - t)

        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        val (i1, j1) = when {
            x0 > y0 -> listOf(1, 0) // lower triangle, XY order: (0,0)->(1,0)->(1,1)
            else -> listOf(0, 1) // upper triangle, YX order: (0,0)->(0,1)->(1,1)
        }
        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6

        val x1 = x0 - i1 + G2 // Offsets for middle corner in (x,y) unskewed coords
        val y1 = y0 - j1 + G2
        val x2 = x0 + G22 // Offsets for last corner in (x,y) unskewed coords
        val y2 = y0 + G22

        // Work out the hashed gradient indices of the three simplex corners
        val ii = i and 255
        val jj = j and 255

        var n = 0F

        // Calculate the contribution from the three corners
        val t0 = 0.5F - x0 * x0 - y0 * y0
        if (t0 > 0) {
            val gi = GRAD3[perm[ii + perm[jj]] % 12]
            n += t0 * t0 * t0 * t0 * (gi[0] * x0 + gi[1] * y0) // (x,y) of GRAD3 used for 2D gradient
        }

        val t1 = 0.5F - x1 * x1 - y1 * y1
        if (t1 > 0) {
            val gi = GRAD3[perm[ii + i1 + perm[jj + j1]] % 12]
            n += t1 * t1 * t1 * t1 * (gi[0] * x1 + gi[1] * y1)
        }

        val t2 = 0.5F - x2 * x2 - y2 * y2
        if (t2 > 0) {
            val gi = GRAD3[perm[ii + 1 + perm[jj + 1]] % 12]
            n += t2 * t2 * t2 * t2 * (gi[0] * x2 + gi[1] * y2)
        }

        // Add contributions from each corner to get the noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0F * n
    }

    override fun getNoise3D(x: Float, y: Float, z: Float): Float {
        val xx = x + offsetX
        val yy = y + offsetY
        val zz = z + offsetZ

        // Skew the input space to determine which simplex cell we're in
        val s = (xx + yy + zz) * F3 // Very nice and simple skew factor for 3D
        val i = (xx + s).toInt()
        val j = (yy + s).toInt()
        val k = (zz + s).toInt()
        val t = (i + j + k) * G3
        // Unskew the cell origin back to (x,y,z) space
        val x0 = xx - (i - t) // The x,y,z distances from the cell origin
        val y0 = yy - (j - t)
        val z0 = zz - (k - t)

        // For the 3D case, the simplex shape is a slightly irregular tetrahedron.

        // Determine which simplex we are in.
        val (i1, j1, k1, i2, j2, k2) = when {
            x0 >= y0 -> when {
                y0 >= z0 -> listOf(1, 0, 0, 1, 1, 0) // X Y Z order
                x0 >= z0 -> listOf(1, 0, 0, 1, 0, 1) // X Z Y order
                else -> listOf(0, 0, 1, 1, 0, 1) // Z X Y order
            }
            else -> when {
                y0 < z0 -> listOf(0, 0, 1, 0, 1, 1) // Z Y X order
                x0 < z0 -> listOf(0, 1, 0, 0, 1, 1) // Y Z X order
                else -> listOf(0, 1, 0, 1, 1, 0) // Y X Z order
            }
        }

        // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
        // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
        // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
        // c = 1/6.
        val x1 = x0 - i1 + G3 // Offsets for second corner in (x,y,z) coords
        val y1 = y0 - j1 + G3
        val z1 = z0 - k1 + G3
        val x2 = x0 - i2 + 2.0F * G3 // Offsets for third corner in (x,y,z) coords
        val y2 = y0 - j2 + 2.0F * G3
        val z2 = z0 - k2 + 2.0F * G3
        val x3 = x0 - 1.0F + 3.0F * G3 // Offsets for last corner in (x,y,z) coords
        val y3 = y0 - 1.0F + 3.0F * G3
        val z3 = z0 - 1.0F + 3.0F * G3

        // Work out the hashed gradient indices of the four simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255

        var n = 0F

        // Calculate the contribution from the four corners
        val t0 = 0.6F - x0 * x0 - y0 * y0 - z0 * z0
        if (t0 > 0) {
            val gi = GRAD3[perm[ii + perm[jj + perm[kk]]] % 12]
            n += t0 * t0 * t0 * t0 * (gi[0] * x0 + gi[1] * y0 + gi[2] * z0)
        }

        val t1 = 0.6F - x1 * x1 - y1 * y1 - z1 * z1
        if (t1 > 0) {
            val gi = GRAD3[perm[ii + i1 + perm[jj + j1 + perm[kk + k1]]] % 12]
            n += t1 * t1 * t1 * t1 * (gi[0] * x1 + gi[1] * y1 + gi[2] * z1)
        }

        val t2 = 0.6F - x2 * x2 - y2 * y2 - z2 * z2
        if (t2 > 0) {
            val gi = GRAD3[perm[ii + i2 + perm[jj + j2 + perm[kk + k2]]] % 12]
            n += t2 * t2 * t2 * t2 * (gi[0] * x2 + gi[1] * y2 + gi[2] * z2)
        }

        val t3 = 0.6F - x3 * x3 - y3 * y3 - z3 * z3
        if (t3 > 0) {
            val gi = GRAD3[perm[ii + 1 + perm[jj + 1 + perm[kk + 1]]] % 12]
            n += t3 * t3 * t3 * t3 * (gi[0] * x3 + gi[1] * y3 + gi[2] * z3)
        }

        // Add contributions from each corner to get the noise value.
        // The result is scaled to stay just inside [-1,1]
        return 32.0F * n
    }

    companion object {
        val GRAD3 = arrayOf(
            intArrayOf(1, 1, 0), intArrayOf(-1, 1, 0), intArrayOf(1, -1, 0), intArrayOf(-1, -1, 0),
            intArrayOf(1, 0, 1), intArrayOf(-1, 0, 1), intArrayOf(1, 0, -1), intArrayOf(-1, 0, -1),
            intArrayOf(0, 1, 1), intArrayOf(0, -1, 1), intArrayOf(0, 1, -1), intArrayOf(0, -1, -1)
        )

        val F2 = 0.5F * (sqrt(3.0F) - 1)
        val G2 = (3 - sqrt(3.0F)) / 6
        val G22 = G2 * 2.0F - 1
        val F3 = 1.0F / 3.0F
        val G3 = 1.0F / 6.0F
    }
}

private operator fun <E> List<E>.component6() = this[5]
