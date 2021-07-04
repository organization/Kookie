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
package be.zvz.kookie.item

import be.zvz.kookie.entity.Living
import be.zvz.kookie.math.Vector3
import kotlin.math.floor
import kotlin.random.Random

class ChorusFruit(identifier: ItemIdentifier, name: String) : Food(identifier, name) {
    override val foodRestore: Int = 4
    override val saturationRestore: Float = 2.4F
    override val requiresHunger: Boolean = false

    override fun onConsume(consumer: Living) {
        val world = consumer.world

        val origin = consumer.getPosition()
        val minX = floor(origin.x).toInt() - 8
        val minY = floor(minOf(origin.y)).toInt() - 8 // TODO: world.maxY
        val minZ = floor(origin.z).toInt() - 8

        val maxX = minX + 16
        val maxY = minY + 16
        val maxZ = minZ + 16

        for (i in 0 until 16) {
            val x = Random.nextInt(minX, maxX)
            var y = Random.nextInt(minY, maxY)
            val z = Random.nextInt(minZ, maxZ)

            while (y >= 0 && !world.getBlock(Vector3(x, y, z)).isSolid()) --y
            if (y < 0) continue

            val blockUp = world.getBlock(Vector3(x, y + 1, z))
            val blockUp2 = world.getBlock(Vector3(x, y + 2, z))
            if (blockUp.isSolid() || blockUp2.isSolid()) continue // TODO: check blockUp(2) is Liquid

            // TODO: addSound at both source and destination

            consumer.teleport(Vector3(x + 0.5, y + 1.0, z + 0.5))

            break
        }
    }
}
