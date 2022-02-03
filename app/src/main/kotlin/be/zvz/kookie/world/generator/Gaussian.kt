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
package be.zvz.kookie.world.generator

import com.koloboke.collect.map.hash.HashIntObjMaps
import kotlin.math.exp

class Gaussian(val smoothSize: Int) {
    val kernel: Map<Int, Map<Int, Float>>

    init {
        val bellSize = 1F / smoothSize
        val bellHeight = 2F * smoothSize

        kernel = HashIntObjMaps.newMutableMap()
        for (sx in -smoothSize..smoothSize) {
            val szMap = HashIntObjMaps.newMutableMap<Float>()
            for (sz in -smoothSize..smoothSize) {
                val bx = bellSize * sx
                val bz = bellSize * sz
                szMap[sz + smoothSize] = bellHeight * exp((bx * bx + bz * bz) / 2)
            }
            kernel[sx + smoothSize] = szMap
        }
    }
}
