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
package be.zvz.kookie.item

import be.zvz.kookie.item.enchantment.Enchantment
import be.zvz.kookie.item.enchantment.EnchantmentInstance

interface ItemEnchantmentHandling {
    val enchantments: MutableMap<Int, EnchantmentInstance>
    fun hasEnchantments(): Boolean = enchantments.isNotEmpty()
    fun hasEnchantment(enchantment: Enchantment, level: Int = -1): Boolean {
        val id = enchantment.internalRuntimeId
        return enchantments.contains(id) && (level == -1 || enchantments[id]?.getRuntimeId() == level)
    }

    fun getEnchantment(enchantment: Enchantment): EnchantmentInstance? = enchantments[enchantment.internalRuntimeId]
    fun removeEnchantment(enchantment: Enchantment, level: Int = -1): ItemEnchantmentHandling = apply {
        val instance = getEnchantment(enchantment)
        if (instance != null && (level == -1 || instance.getRuntimeId() == level)) {
            enchantments.remove(instance.getRuntimeId())
        }
    }

    fun removeEnchantments(): ItemEnchantmentHandling = apply {
        enchantments.clear()
    }

    fun hasEnchantment(enchantment: EnchantmentInstance): ItemEnchantmentHandling = apply {
        enchantments[enchantment.getRuntimeId()] = enchantment
    }

    fun getEnchantmentLevel(enchantment: Enchantment): Int = getEnchantment(enchantment)?.level ?: 0
}
