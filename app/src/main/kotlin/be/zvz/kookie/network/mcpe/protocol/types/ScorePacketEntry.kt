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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.PacketHandlingException

data class ScorePacketEntry @JvmOverloads constructor(
    val scoreboardId: Long,
    val objectiveName: String,
    val score: Int,
    var type: Type? = null,
    var entityUniqueId: Long = 0,
    var customName: String = ""
) {

    enum class Type(val id: Int) {
        PLAYER(1),
        ENTITY(2),
        FAKE_PLAYER(3);

        companion object {
            private val VALUES = values()

            @JvmStatic
            fun from(value: Int) = VALUES.firstOrNull { it.id == value }
                ?: throw PacketHandlingException("Unhandled set score entry type $value!")
        }
    }
}
