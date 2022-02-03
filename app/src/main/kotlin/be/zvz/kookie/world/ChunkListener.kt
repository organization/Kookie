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
import be.zvz.kookie.world.format.Chunk

/**
 * This interface allows you to listen for events occurring on or in specific chunks.
 * This will receive events for any chunks which it is registered to listen to.
 *
 * @see World.registerChunkListener()
 * @see World.unregisterChunkListener()
 *
 * WARNING: When you're done with the listener, make sure you unregister it from all chunks it's listening to,
 * otherwise the object will not be destroyed.
 * The listener WILL NOT be unregistered when chunks are unloaded.
 * You need to do this yourself when you're done with a chunk.
 */
interface ChunkListener {
    fun onChunkChanged(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        // NOOP
    }

    fun onChunkLoaded(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        // NOOP
    }

    fun onChunkUnloaded(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        // NOOP
    }

    fun onChunkPopulated(chunkX: Int, chunkZ: Int, chunk: Chunk) {
        // NOOP
    }

    fun onBlockChanged(block: Vector3) {
        // NOOP
    }
}
