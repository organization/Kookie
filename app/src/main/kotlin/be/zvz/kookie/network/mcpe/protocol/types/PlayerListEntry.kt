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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData
import java.util.UUID

class PlayerListEntry {
    lateinit var uuid: UUID
    var entityUniqueId: Long = 0
    lateinit var username: String
    lateinit var skinData: SkinData
    lateinit var xboxUserId: String
    var platformChatId: String = ""
    var buildPlatform: Int = DeviceOS.UNKNOWN.id
    var isTeacher: Boolean = false
    var isHost: Boolean = false

    companion object {
        @JvmStatic
        fun createRemovalEntry(uuid: UUID) = PlayerListEntry().apply {
            this.uuid = uuid
        }

        @JvmStatic
        @JvmOverloads
        fun createAdditionEntry(
            uuid: UUID,
            entityUniqueId: Long,
            username: String,
            skinData: SkinData,
            xboxUserId: String = "",
            platformChatId: String = "",
            buildPlatform: Int = -1,
            isTeacher: Boolean = false,
            isHost: Boolean = false
        ) = PlayerListEntry().apply {
            this.uuid = uuid
            this.entityUniqueId = entityUniqueId
            this.username = username
            this.skinData = skinData
            this.xboxUserId = xboxUserId
            this.platformChatId = platformChatId
            this.buildPlatform = buildPlatform
            this.isTeacher = isTeacher
            this.isHost = isHost
        }
    }
}
