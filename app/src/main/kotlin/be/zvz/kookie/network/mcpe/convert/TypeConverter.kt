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

import be.zvz.kookie.item.Item
import be.zvz.kookie.network.mcpe.protocol.types.recipe.RecipeIngredient
import be.zvz.kookie.player.GameMode
import be.zvz.kookie.network.mcpe.protocol.types.GameMode as ProtocolGameMode

object TypeConverter {

    private const val DAMAGE_TAG = "Damage" // TAG_Int
    private const val DAMAGE_TAG_CONFLICT_RESOLUTION = "___Damage_ProtocolCollisionResolution___"
    private const val PM_META_TAG = "___Meta___"

    private val shieldRuntimeId: Int = GlobalItemTypeDictionary.getInstance().dictionary.fromStringId("minecraft:shield")

    fun coreGameModeToProtocol(gameMode: GameMode): Int = when (gameMode) {
        GameMode.SURVIVAL -> ProtocolGameMode.SURVIVAL
        GameMode.CREATIVE, GameMode.SPECTATOR -> ProtocolGameMode.CREATIVE
        GameMode.ADVENTURE -> ProtocolGameMode.ADVENTURE
    }

    fun protocolGameModeName(gameMode: GameMode): String = when (gameMode) {
        GameMode.SURVIVAL -> "Survival"
        GameMode.ADVENTURE -> "Adventure"
        else -> "Creative"
    }

    fun protocolGameModeToCore(gameMode: Int): GameMode? = when (gameMode) {
        ProtocolGameMode.SURVIVAL -> GameMode.SURVIVAL
        ProtocolGameMode.CREATIVE -> GameMode.CREATIVE
        ProtocolGameMode.ADVENTURE -> GameMode.ADVENTURE
        ProtocolGameMode.SURVIVAL_VIEWER, ProtocolGameMode.CREATIVE_VIEWER -> GameMode.SPECTATOR
        else -> null
    }

    fun coreItemStackToRecipeIngredient(itemStack: Item): RecipeIngredient {
        if (itemStack.isNull())
            return RecipeIngredient(0, 0, 0)
        val id: Int
        val meta: Int
        if (itemStack.hasAnyDamageValue()) {
            TODO()
        }
        return RecipeIngredient(0, 0, 0)
    }
}
