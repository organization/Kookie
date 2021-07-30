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

import be.zvz.kookie.world.format.Chunk
import be.zvz.kookie.world.format.io.exception.CorruptedChunkException
import org.slf4j.Logger
import java.nio.file.Path

interface WorldProvider {
    val worldMinY: Int
    val worldMaxY: Int
    val path: Path
    val worldData: WorldData

    /** If the chunk does not exist, null is returned. */
    @Throws(CorruptedChunkException::class)
    fun loadChunk(chunkX: Int, chunkZ: Int): Chunk?

    /** Performs garbage collection in the world provider, such as cleaning up regions in Region-based worlds. */
    fun doGarbageCollection()

    /** Performs cleanups necessary when the world provider is closed and no longer needed. */
    fun close()

    /**
     * Returns a generator which yields all the chunks in this world.
     *
     * @return \Generator|Chunk[]
     * @phpstan-return \Generator<array{int, int}, Chunk, void, void>
     * @throws CorruptedChunkException
     */
    @Throws(CorruptedChunkException::class)
    fun getAllChunks(skipCorrupted: Boolean = false, logger: Logger? = null): Sequence<Triple<Int, Int, Chunk>>

    /** Returns the number of chunks in the provider. Used for world conversion time estimations */
    fun calculateChunkCount(): Int
}
