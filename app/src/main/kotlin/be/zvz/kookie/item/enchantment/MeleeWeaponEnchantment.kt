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
package be.zvz.kookie.item.enchantment

import be.zvz.kookie.entity.Entity

abstract class MeleeWeaponEnchantment(
    internalRuntimeId: Int,
    name: String,
    rarity: Rarity,
    primaryItemFlags: Array<ItemFlags>,
    secondaryItemFlags: Array<ItemFlags>,
    maxLevel: Int
) : Enchantment(internalRuntimeId, name, rarity, primaryItemFlags, secondaryItemFlags, maxLevel) {
    abstract fun isApplicableTo(victim: Entity): Boolean
    abstract fun getDamageBonus(enchantmentLevel: Int): Float
    open fun onPostAttack(attacker: Entity, victim: Entity, enchantmentLevel: Int) {
    }
}
