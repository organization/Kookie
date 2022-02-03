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

import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler

class WorldTimings(world: World) {
    private val name = world.folderName + " - "
    private val prefixedName = Timings.INCLUDED_BY_OTHER_TIMINGS_PREFIX + name

    val setBlock = TimingsHandler(prefixedName + "setBlock")
    val doBlockLightUpdates = TimingsHandler(prefixedName + "Block Light Updates")
    val doBlockSkyLightUpdates = TimingsHandler(prefixedName + "Sky Light Updates")

    val doChunkUnload = TimingsHandler(prefixedName + "Unload Chunks")
    val scheduledBlockUpdates = TimingsHandler(prefixedName + "Scheduled Block Updates")
    val randomChunkUpdates = TimingsHandler(prefixedName + "Random Chunk Updates")
    val randomChunkUpdatesChunkSelection = TimingsHandler(prefixedName + "Random Chunk Updates - Chunk Selection")
    val doChunkGC = TimingsHandler(prefixedName + "Garbage Collection")
    val entityTick = TimingsHandler(prefixedName + "Tick Entities")
    val doTick = TimingsHandler(name + "World Tick")

    val syncChunkSend = TimingsHandler(prefixedName + "Player Send Chunks", Timings.playerChunkSend)
    val syncChunkSendPrepare = TimingsHandler(prefixedName + "Player Send Chunk Prepare", Timings.playerChunkSend)

    val syncChunkLoad = TimingsHandler(prefixedName + "Chunk Load", Timings.worldLoad)
    val syncChunkLoadData = TimingsHandler(prefixedName + "Chunk Load - Data")
    val syncChunkLoadEntities = TimingsHandler(prefixedName + "Chunk Load - Entities")
    val syncChunkLoadTileEntities = TimingsHandler(prefixedName + "Chunk Load - TileEntities")
    val syncChunkSave = TimingsHandler(prefixedName + "Chunk Save", Timings.worldSave)
}
