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
            arrayOf(ItemFlags.ARMOR),
            arrayOf(ItemFlags.NONE),
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
            arrayOf(ItemFlags.ARMOR),
            arrayOf(ItemFlags.NONE),
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
            arrayOf(ItemFlags.FEET),
            arrayOf(ItemFlags.NONE),
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
            arrayOf(ItemFlags.ARMOR),
            arrayOf(ItemFlags.NONE),
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
            arrayOf(ItemFlags.ARMOR),
            arrayOf(ItemFlags.NONE),
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
            arrayOf(ItemFlags.TORSO),
            arrayOf(ItemFlags.HEAD, ItemFlags.LEGS, ItemFlags.FEET),
            3
        )
    ),
    RESPIRATION(
        Enchantment(
            6,
            "%enchantment.oxygen",
            Rarity.RARE,
            arrayOf(ItemFlags.HEAD),
            arrayOf(ItemFlags.NONE),
            3
        )
    ),
    SHARPNESS(
        SharpnessEnchantment(
            7,
            "%enchantment.damage.all",
            Rarity.COMMON,
            arrayOf(ItemFlags.SWORD),
            arrayOf(ItemFlags.AXE),
            5
        )
    ),
    // TODO: smite, bane of arthropods (these don't make sense now because their applicable mobs don't exist yet)
    KNOCKBACK(
        KnockbackEnchantment(
            8,
            "%enchantment.knockback",
            Rarity.UNCOMMON,
            arrayOf(ItemFlags.SWORD),
            arrayOf(ItemFlags.NONE),
            2
        )
    ),
    FIRE_ASPECT(
        FireAspectEnchantment(
            9,
            "%enchantment.fire",
            Rarity.RARE,
            arrayOf(ItemFlags.SWORD),
            arrayOf(ItemFlags.NONE),
            2
        )
    ),
    EFFICIENCY(
        Enchantment(
            10,
            "%enchantment.digging",
            Rarity.COMMON,
            arrayOf(ItemFlags.DIG),
            arrayOf(ItemFlags.SHEARS),
            5
        )
    ),
    SILK_TOUCH(
        Enchantment(
            11,
            "%enchantment.untouching",
            Rarity.MYTHIC,
            arrayOf(ItemFlags.DIG),
            arrayOf(ItemFlags.SHEARS),
            1
        )
    ),
    UNBREAKING(
        Enchantment(
            12,
            "%enchantment.durability",
            Rarity.UNCOMMON,
            arrayOf(ItemFlags.DIG, ItemFlags.ARMOR, ItemFlags.FISHING_ROD, ItemFlags.BOW),
            arrayOf(ItemFlags.TOOL, ItemFlags.CARROT_STICK, ItemFlags.ELYTRA),
            3
        )
    ),
    POWER(
        Enchantment(
            13,
            "%enchantment.arrowDamage",
            Rarity.COMMON,
            arrayOf(ItemFlags.BOW),
            arrayOf(ItemFlags.NONE),
            5
        )
    ),
    PUNCH(
        Enchantment(
            14,
            "%enchantment.arrowKnockback",
            Rarity.RARE,
            arrayOf(ItemFlags.BOW),
            arrayOf(ItemFlags.NONE),
            5
        )
    ),
    FLAME(
        Enchantment(
            15,
            "%enchantment.arrowFire",
            Rarity.RARE,
            arrayOf(ItemFlags.BOW),
            arrayOf(ItemFlags.NONE),
            1
        )
    ),
    INFINITY(
        Enchantment(
            16,
            "%enchantment.arrowInfinite",
            Rarity.MYTHIC,
            arrayOf(ItemFlags.BOW),
            arrayOf(ItemFlags.NONE),
            1
        )
    ),
    MENDING(
        Enchantment(
            17,
            "%enchantment.mending",
            Rarity.RARE,
            arrayOf(ItemFlags.NONE),
            arrayOf(ItemFlags.ALL),
            1
        )
    ),
    VANISHING(
        Enchantment(
            18,
            "%enchantment.curse.vanishing",
            Rarity.MYTHIC,
            arrayOf(ItemFlags.NONE),
            arrayOf(ItemFlags.ALL),
            1
        )
    );

    companion object {
        private val VALUES = values()
        fun from(value: String) = VALUES.firstOrNull { it.enchantment.toString().equals(value, true) }
        fun getAll(): Array<VanillaEnchantments> = VALUES
    }
}
