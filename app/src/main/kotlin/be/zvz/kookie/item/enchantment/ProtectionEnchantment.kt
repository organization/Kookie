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

import be.zvz.kookie.event.entity.EntityDamageEvent
import com.koloboke.collect.map.hash.HashObjObjMaps

class ProtectionEnchantment(
    internalRuntimeId: Int,
    name: String,
    rarity: Rarity,
    primaryItemFlags: Array<ItemFlags>,
    secondaryItemFlags: Array<ItemFlags>,
    maxLevel: Int,
    val typeModifier: Float,
    applicableDamageTypes: MutableList<EntityDamageEvent.Type>?,
) : Enchantment(internalRuntimeId, name, rarity, primaryItemFlags, secondaryItemFlags, maxLevel) {
    var applicableDamageTypes: MutableMap<EntityDamageEvent.Type, Int>? = null

    init {
        var index = 0

        applicableDamageTypes?.let { list ->
            this.applicableDamageTypes = HashObjObjMaps.newMutableMap<EntityDamageEvent.Type, Int>().apply {
                list.forEach { type ->
                    this[type] = index++
                }
            }
        }
    }

    fun getProtectionFactor(level: Int): Int = ((6 + level * level) * typeModifier / 3).toInt()

    fun isApplicable(event: EntityDamageEvent): Boolean =
        applicableDamageTypes === null || applicableDamageTypes?.containsKey(event.cause) == true
}
