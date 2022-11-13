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
package be.zvz.kookie.network.mcpe.serializer

import be.zvz.kookie.block.tile.Spawnable
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.utils.Binary
import be.zvz.kookie.utils.BinaryStream
import be.zvz.kookie.world.format.Chunk

object ChunkSerializer {
    /**
     * Returns the number of subchunks that will be sent from the given chunk.
     * Chunks are sent in a stack, so every chunk below the top non-empty one must be sent.
     */
    fun getActualSubChunkCount(chunk: Chunk): Int {
        for (count in chunk.subChunks.size..0 step -1) {
            if (!chunk.getSubChunk(count - 1).isEmptyFast()) {
                return count
            }
        }

        return 0
    }

    fun serialize(chunk: Chunk): String {
        val stream = PacketSerializer()
        val subChunkCount = getActualSubChunkCount(chunk)
        repeat(subChunkCount) { y ->
            val layers = chunk.getSubChunk(y).blockLayers
            stream.putByte(8) // version

            stream.putByte(layers.size)
            layers.forEach { blocks ->
                stream.putByte((blocks.bitsPerBlock shl 1) or 1) // last 1-bit means "network format", but seems pointless
                stream.put(blocks.wordArray.toString())

                val palette = blocks.palette
                // these LSHIFT by 1 uvarints are optimizations: the client expects zigzag varints here
                // but since we know they are always unsigned, we can avoid the extra fcall overhead of
                // zigzag and just shift directly.
                stream.putUnsignedVarInt(palette.size shl 1) // yes, this is intentionally zigzag
                palette.forEach { fullId ->
                    stream.put(
                        Binary.writeUnsignedVarInt(
                            fullId.toInt() // TODO: replace to `RuntimeBlockMapping.toRuntimeId(fullId) shl 1`
                        )
                    )
                }
            }
        }
        stream.put(chunk.biomeIds.payload)
        stream.putByte(0); // border block array count
        // Border block entry format: 1 byte (4 bits X, 4 bits Z). These are however useless since they crash the regular client.

        stream.put(serializeTiles(chunk))
        return stream.buffer.toString()
    }

    fun serializeTiles(chunk: Chunk): String {
        val stream = BinaryStream()
        chunk.tiles.forEach { tile ->
            if (tile is Spawnable) {
                stream.put(tile.serializedSpawnCompound.encodedNbt)
            }
        }
        return stream.buffer.toString()
    }
}
