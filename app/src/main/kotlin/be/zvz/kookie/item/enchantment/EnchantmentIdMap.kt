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

import com.koloboke.collect.map.hash.HashIntIntMaps
import com.koloboke.collect.map.hash.HashIntObjMaps

object EnchantmentIdMap {
    private val idToEnch: MutableMap<Int, Enchantment> = HashIntObjMaps.newMutableMap()
    private val enchToId: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()

    init {
        register(EnchantmentIds.PROTECTION.id, VanillaEnchantments.PROTECTION.enchantment)
        register(EnchantmentIds.FIRE_PROTECTION.id, VanillaEnchantments.FIRE_PROTECTION.enchantment)
        register(EnchantmentIds.FEATHER_FALLING.id, VanillaEnchantments.FEATHER_FALLING.enchantment)
        register(EnchantmentIds.BLAST_PROTECTION.id, VanillaEnchantments.BLAST_PROTECTION.enchantment)
        register(EnchantmentIds.PROJECTILE_PROTECTION.id, VanillaEnchantments.PROJECTILE_PROTECTION.enchantment)
        register(EnchantmentIds.THORNS.id, VanillaEnchantments.THORNS.enchantment)
        register(EnchantmentIds.RESPIRATION.id, VanillaEnchantments.RESPIRATION.enchantment)

        register(EnchantmentIds.SHARPNESS.id, VanillaEnchantments.SHARPNESS.enchantment)
        // TODO: smite.id, bane of arthropods (these don't make sense now because their applicable mobs don't exist yet.enchantment)

        register(EnchantmentIds.KNOCKBACK.id, VanillaEnchantments.KNOCKBACK.enchantment)
        register(EnchantmentIds.FIRE_ASPECT.id, VanillaEnchantments.FIRE_ASPECT.enchantment)

        register(EnchantmentIds.EFFICIENCY.id, VanillaEnchantments.EFFICIENCY.enchantment)
        register(EnchantmentIds.SILK_TOUCH.id, VanillaEnchantments.SILK_TOUCH.enchantment)
        register(EnchantmentIds.UNBREAKING.id, VanillaEnchantments.UNBREAKING.enchantment)

        register(EnchantmentIds.POWER.id, VanillaEnchantments.POWER.enchantment)
        register(EnchantmentIds.PUNCH.id, VanillaEnchantments.PUNCH.enchantment)
        register(EnchantmentIds.FLAME.id, VanillaEnchantments.FLAME.enchantment)
        register(EnchantmentIds.INFINITY.id, VanillaEnchantments.INFINITY.enchantment)

        register(EnchantmentIds.MENDING.id, VanillaEnchantments.MENDING.enchantment)

        register(EnchantmentIds.VANISHING.id, VanillaEnchantments.VANISHING.enchantment)
    }

    fun register(mcpeId: Int, enchantment: Enchantment) {
        idToEnch[mcpeId] = enchantment
        enchToId[enchantment.internalRuntimeId] = mcpeId
    }

    fun fromId(id: Int): Enchantment? = idToEnch[id]
    fun toId(enchantment: Enchantment): Int {
        if (!enchToId.containsKey(enchantment.internalRuntimeId)) {
            throw Exception("Enchantment does not have a mapped ID")
        }
        return enchToId[enchantment.internalRuntimeId]!!
    }
}
