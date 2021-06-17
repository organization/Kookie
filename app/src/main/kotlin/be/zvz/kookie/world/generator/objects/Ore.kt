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
package be.zvz.kookie.world.generator.objects

import be.zvz.kookie.block.Block
import be.zvz.kookie.math.Vector2
import be.zvz.kookie.world.ChunkManager
import java.util.Random
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Ore(
    val random: Random,
    val type: Type
) {
    fun canPlaceObject(world: ChunkManager, x: Int, y: Int, z: Int): Boolean =
        world.getBlockAt(x, y, z).isSameType(this.type.replaces)

    fun placeObject(world: ChunkManager, x: Int, y: Int, z: Int) {
        val clusterSize = type.clusterSize
        val angle = random.nextFloat() * Math.PI
        val offset = Vector2(cos(angle), sin(angle)).multiply(clusterSize / 8)
        val x1 = x + 8 + offset.x
        val x2 = x + 8 - offset.x
        val z1 = z + 8 + offset.y
        val z2 = z + 8 - offset.y
        val y1 = y + random.nextInt() % 3 + 2
        val y2 = y + random.nextInt() % 3 + 2

        repeat(clusterSize + 1) { count ->
            val seedX = x1 + (x2 - x1) * count / clusterSize
            val seedY = y1 + (y2 - y1) * count / clusterSize
            val seedZ = z1 + (z2 - z1) * count / clusterSize
            val size = ((sin(count * (Math.PI / clusterSize)) + 1) * random.nextFloat() * clusterSize / 16 + 1) / 2

            val startX = (seedX - size).toInt()
            val startY = (seedY - size).toInt()
            val startZ = (seedZ - size).toInt()
            val endX = (seedX + size).toInt()
            val endY = (seedY + size).toInt()
            val endZ = (seedZ + size).toInt()

            for (xx in startX..endX) {
                val sizeX = (xx + 0.5 - seedX).pow(2.0)
                if (sizeX < 1) {
                    for (yy in startY..endY) {
                        val sizeY = ((yy + 0.5 - seedY) / size).pow(2.0)
                        if (yy > 0 && sizeX + sizeY < 1) {
                            for (zz in startZ..endZ) {
                                val sizeZ = ((zz + 0.5 - seedZ) / size).pow(2.0)
                                if (
                                    sizeX + sizeY + sizeZ < 1 &&
                                    world.getBlockAt(xx, yy, zz).isSameType(type.replaces)
                                ) {
                                    world.setBlockAt(xx, yy, zz, type.material)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    data class Type(
        val material: Block,
        val replaces: Block,
        val clusterCount: Int,
        val clusterSize: Int,
        val minHeight: Int,
        val maxHeight: Int
    )
}
