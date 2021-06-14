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
package be.zvz.kookie.item.enchantment

open class Enchantment(
    val internalRuntimeId: Int,
    val name: String,
    val rarity: Rarity,
    primaryItemFlags: Array<ItemFlags>,
    secondaryItemFlags: Array<ItemFlags>,
    val maxLevel: Int,
) {
    val primaryItemFlags = let { enchantment ->
        var result: Int = primaryItemFlags.first().flags
        primaryItemFlags.drop(1).forEach {
            result = result or it.flags
        }
        return@let result
    }
    val secondaryItemFlags = let { enchantment ->
        var result: Int = secondaryItemFlags.first().flags
        secondaryItemFlags.drop(1).forEach {
            result = result or it.flags
        }
        return@let result
    }

    fun hasPrimaryItemType(flag: Int): Boolean = (primaryItemFlags and flag) != 0
    fun hasSecondaryItemType(flag: Int): Boolean = (secondaryItemFlags and flag) != 0
}
