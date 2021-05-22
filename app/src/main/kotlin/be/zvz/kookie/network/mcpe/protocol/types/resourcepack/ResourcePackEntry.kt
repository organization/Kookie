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
package be.zvz.kookie.network.mcpe.protocol.types.resourcepack

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class ResourcePackEntry(
    val packId: String,
    val version: String,
    val sizeBytes: Long,
    val encryptionKey: String = "",
    val subPackName: String = "",
    val contentId: String = "",
    val hasScript: Boolean = false,
    val isRtxCapable: Boolean = false,
) {
    fun write(output: PacketSerializer) {
        output.putString(packId)
        output.putString(version)
        output.putLLong(sizeBytes)
        output.putString(encryptionKey)
        output.putString(subPackName)
        output.putString(contentId)
        output.putBoolean(hasScript)
        output.putBoolean(isRtxCapable)
    }

    companion object {
        fun read(input: PacketSerializer): ResourcePackEntry {
            return ResourcePackEntry(
                input.getString(),
                input.getString(),
                input.getLLong(),
                input.getString(),
                input.getString(),
                input.getString(),
                input.getBoolean(),
                input.getBoolean()
            )
        }
    }
}
