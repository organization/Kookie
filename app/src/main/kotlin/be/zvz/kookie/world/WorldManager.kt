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
import com.koloboke.collect.map.hash.HashIntObjMaps

class WorldManager(private val server: Server) {
    val worlds: MutableMap<Int, World> = HashIntObjMaps.newMutableMap()

    fun loadWorld(world: String) {
    }

    fun unloadWorld(world: String) {
    }

    fun getWorld(id: Int): World? = worlds[id]

    fun getWorldByName(name: String): World? {
        worlds.forEach { (_, world) ->
            if (world.folderName == name) {
                return world
            }
        }
        return null
    }
}
