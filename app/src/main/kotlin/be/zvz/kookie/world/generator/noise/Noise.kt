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

abstract class Noise(
    protected var octaves: Int,
    protected var persistence: Float,
    protected var expansion: Float
) {

    abstract fun getNoise2D(x: Float, z: Float): Float
    abstract fun getNoise3D(x: Float, y: Float, z: Float): Float

    @JvmOverloads
    fun noise2D(x: Float, z: Float, normalized: Boolean = false): Float {
        var (result, freq, amp, max) = listOf(0F, 1F, 1F, 0F)

        val expansionX = x * expansion
        val expansionZ = z * expansion

        for (i in 0 until octaves) {
            result += getNoise2D(expansionX * freq, expansionZ * freq) * amp

            max += amp
            freq *= 2
            amp *= persistence
        }

        return if (normalized) {
            result / max
        } else result
    }

    @JvmOverloads
    fun noise3D(x: Int, y: Int, z: Int, normalized: Boolean = false): Float = noise3D(x, y, z, normalized)

    @JvmOverloads
    fun noise3D(x: Float, y: Float, z: Float, normalized: Boolean = false): Float {
        var (result, freq, amp, max) = listOf(0F, 1F, 1F, 0F)

        val expansionX = x * expansion
        val expansionY = y * expansion
        val expansionZ = z * expansion

        for (i in 0 until octaves) {
            result += getNoise3D(expansionX * freq, expansionY * freq, expansionZ * freq) * amp

            max += amp
            freq *= 2
            amp *= persistence
        }

        return if (normalized) {
            result / max
        } else result
    }

    @JvmOverloads
    fun getFastNoise3D(
        x: Int,
        y: Int,
        z: Int,
        xSize: Int = 16,
        ySize: Int = 128,
        zSize: Int = 16,
        xSamplingRate: Int = 4,
        ySamplingRate: Int = 8,
        zSamplingRate: Int = 4
    ): Array<Array<FloatArray>> {
        require(xSamplingRate != 0) { "xSamplingRate cannot be 0" }
        require(zSamplingRate != 0) { "zSamplingRate cannot be 0" }
        require(ySamplingRate != 0) { "ySamplingRate cannot be 0" }

        require(xSize % xSamplingRate == 0) { "xSize % xSamplingRate must return 0" }
        require(zSize % zSamplingRate == 0) { "zSize % zSamplingRate must return 0" }
        require(ySize % ySamplingRate == 0) { "ySize % ySamplingRate must return 0" }

        val noises: Array<Array<FloatArray>> = Array(xSize) { xx ->
            Array(zSize) { zz ->
                FloatArray(ySize) { yy ->
                    if (xx % xSamplingRate == 0 && yy % ySamplingRate == 0 && zz % zSamplingRate == 0) {
                        noise3D(x + xx, y + yy, z + zz, true)
                    } else {
                        0F
                    }
                }
            }
        }
        repeat(noises.size) { xx ->
            val (nx, nnx, dx1, dx2) = noiseValues(xx, xSamplingRate)
            repeat(noises[xx].size) { zz ->
                val (nz, nnz, dz1, dz2) = noiseValues(zz, xSamplingRate)
                repeat(noises[xx][zz].size) { yy ->
                    val (ny, nny, dy1, dy2) = noiseValues(yy, xSamplingRate)
                    if (xx % xSamplingRate != 0 || yy % ySamplingRate != 0 || zz % zSamplingRate != 0) {
                        noises[xx][zz][yy] =
                            dz1 * (
                            dy1 * (dx1 * noises[nx][nz][ny] + dx2 * noises[nnx][nz][ny]) +
                                dy2 * (dx1 * noises[nx][nz][nny] + dx2 * noises[nnx][nz][nny])
                            ) +
                            dz2 * (
                            dy1 * (dx1 * noises[nx][nnz][ny] + noises[nnx][nnz][ny]) +
                                dy2 * (dx1 * noises[nx][nnz][nny] + noises[nnx][nnz][nny])
                            )
                    }
                }
            }
        }
        return noises
    }

    companion object {
        data class Values(val noise: Int, val next: Int, val d1: Float, val d2: Float)

        fun noiseValues(current: Int, samplingRate: Int): Values {
            val noise = current - current % samplingRate
            val next = noise + samplingRate
            return Values(
                noise,
                next,
                (next.toFloat() - current) / (next - noise),
                (current.toFloat() - noise) / (next - noise)
            )
        }
    }
}
