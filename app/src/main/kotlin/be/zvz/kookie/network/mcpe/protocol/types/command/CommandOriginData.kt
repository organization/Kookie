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
package be.zvz.kookie.network.mcpe.protocol.types.command

import java.util.UUID

class CommandOriginData {
    var type: Origin = Origin.UNKNOWN
    lateinit var uuid: UUID
    lateinit var requestId: String
    var playerEntityUniqueId: Long = 0

    enum class Origin(val id: Int) {
        PLAYER(0),
        BLOCK(1),
        MINECART_BLOCK(2),
        DEV_CONSOLE(3),
        TEST(4),
        AUTOMATION_PLAYER(5),
        CLIENT_AUTOMATION(6),
        DEDICATED_SERVER(7),
        ENTITY(8),
        VIRTUAL(9),
        GAME_ARGUMENT(10),
        ENTITY_SERVER(11), // ???
        UNKNOWN(-1);

        companion object {
            private val VALUES = values()

            @JvmStatic
            fun from(findValue: Int): Origin = VALUES.firstOrNull { it.id == findValue } ?: UNKNOWN
        }
    }
}
