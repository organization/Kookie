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
package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.player.GameMode
import be.zvz.kookie.network.mcpe.protocol.types.GameMode as ProtocolGameMode

object TypeConverter {

    private const val DAMAGE_TAG = "Damage" // TAG_Int
    private const val DAMAGE_TAG_CONFLICT_RESOLUTION = "___Damage_ProtocolCollisionResolution___"
    private const val PM_META_TAG = "___Meta___"

    private val shieldRuntimeId: Int = ItemTypeDictionary.getInstance().fromStringId("minecraft:shield")

    fun coreGameModeToProtocol(gameMode: GameMode): Int {
        return when (gameMode.id()) {
            0 -> ProtocolGameMode.SURVIVAL
            1 -> ProtocolGameMode.CREATIVE
            2 -> ProtocolGameMode.ADVENTURE
            3 -> ProtocolGameMode.SURVIVAL_VIEWER
            4 -> ProtocolGameMode.CREATIVE_VIEWER
            else -> ProtocolGameMode.DEFAULT
        }
    }
}
