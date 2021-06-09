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
package be.zvz.kookie.world.format.io

import be.zvz.kookie.math.Vector3

interface WorldData {
    var time: Long
    var spawn: Vector3
    var difficulty: Int
    var rainTime: Int
    var rainLevel: Float
    var lightningTime: Int
    var lightningLevel: Float

    val name: String
    val generatorName: String
    val generatorOptions: Map<String, String>
    val seed: Long

    /** Saves information about the world state, such as weather, time, etc. */
    fun save()
}
