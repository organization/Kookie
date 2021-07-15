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
package be.zvz.kookie.world

import be.zvz.kookie.Server
import be.zvz.kookie.world.format.io.WorldProviderManager
import com.koloboke.collect.map.hash.HashIntObjMaps
import java.nio.file.Path

class WorldManager(
    private val server: Server,
    val dataPath: String,
    val providerManager: WorldProviderManager
) {
    private val worldPath: Path get() = server.getDataPath().resolve("worlds")

    val worlds: MutableMap<Int, World> = HashIntObjMaps.newMutableMap()
    lateinit var defaultWorld: World

    var autoSave: Boolean = true
        set(value) {
            field = value
            worlds.values.forEach { world ->
                world.autoSave = true
            }
        }
    var autoSaveTicks = 6000
        set(value) {
            if (value <= 0) {
                throw IllegalArgumentException("Auto save ticks must be positive")
            }
            field = value
        }

    fun isWorldLoaded(name: String): Boolean = getWorldByName(name) is World
    fun getWorldByName(name: String): World? = worlds.values.find { it.folderName == name }
    fun getWorld(id: Int): World? = worlds[id]

    fun loadWorld(world: String) {
    }

    fun unloadWorld(world: String) {
    }
}
