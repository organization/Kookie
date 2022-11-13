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
package be.zvz.kookie.world

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.world.generator.Generator
import be.zvz.kookie.world.generator.NormalGenerator
import kotlin.reflect.KClass

data class WorldCreationOptions(
    var generatorClass: KClass<out Generator> = NormalGenerator::class,
    var seed: Long = -1L,
    var difficulty: Int = World.DIFFICULTY_NORMAL,
    var generatorOptions: String = "",
    var spawnPosition: Vector3
)
