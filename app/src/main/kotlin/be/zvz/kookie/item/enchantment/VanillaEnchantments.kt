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

enum class VanillaEnchantments(val enchantment: Enchantment) {
    PROTECTION(
        ProtectionEnchantment(
            0,
            "%enchantment.protect.all",
            Rarity.COMMON,
            ItemFlags.ARMOR.flags,
            ItemFlags.NONE.flags,
            4,
            0.75F,
            null
        )
    ),
    FIRE_PROTECTION(
        ProtectionEnchantment(
            1,
            "%enchantment.protect.fire",
            Rarity.UNCOMMON,
            ItemFlags.ARMOR.flags,
            ItemFlags.NONE.flags,
            4,
            1.25F,
            null // TODO: EntityDamageEvent enum
        )
    ),
    FEATHER_FALLING(
        ProtectionEnchantment(
            2,
            "%enchantment.protect.fall",
            Rarity.UNCOMMON,
            ItemFlags.FEET.flags,
            ItemFlags.NONE.flags,
            4,
            2.5F,
            null // TODO: EntityDamageEvent enum
        )
    ),
    BLAST_PROTECTION(
        ProtectionEnchantment(
            3,
            "%enchantment.protect.explosion",
            Rarity.RARE,
            ItemFlags.ARMOR.flags,
            ItemFlags.NONE.flags,
            4,
            1.5F,
            null // TODO: EntityDamageEvent enum
        )
    ),
    PROJECTILE_PROTECTION(
        ProtectionEnchantment(
            4,
            "%enchantment.protect.projectile",
            Rarity.UNCOMMON,
            ItemFlags.ARMOR.flags,
            ItemFlags.NONE.flags,
            4,
            1.5F,
            null // TODO: EntityDamageEvent enum
        )
    ),
    THORNS(
        Enchantment(
            5,
            "%enchantment.thorns",
            Rarity.MYTHIC,
            ItemFlags.TORSO.flags,
            ItemFlags.HEAD.flags or ItemFlags.LEGS.flags or ItemFlags.FEET.flags,
            3
        )
    ),
    RESPIRATION(
        Enchantment(
            6,
            "%enchantment.oxygen",
            Rarity.RARE,
            ItemFlags.HEAD.flags,
            ItemFlags.NONE.flags,
            3
        )
    ),
    UNBREAKING(
        Enchantment(
            12,
            "%enchantment.durability",
            Rarity.UNCOMMON,
            ItemFlags.DIG.flags or ItemFlags.ARMOR.flags or ItemFlags.FISHING_ROD.flags or ItemFlags.BOW.flags,
            ItemFlags.TOOL.flags or ItemFlags.CARROT_STICK.flags or ItemFlags.ELYTRA.flags,
            3
        )
    );

    companion object {
        private val VALUES = values()
        fun from(value: String) = VALUES.firstOrNull { it.enchantment.toString().equals(value, true) }
    }
}
