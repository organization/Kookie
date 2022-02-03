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
package be.zvz.kookie.event.world

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.Chunk

class ChunkUnloadEvent(world: World, chunkX: Int, chunkZ: Int, chunk: Chunk) :
    ChunkEvent(world, chunkX, chunkZ, chunk),
    Cancellable {
    override var isCancelled: Boolean = false
}
