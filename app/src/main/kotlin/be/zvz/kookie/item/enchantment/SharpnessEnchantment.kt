package be.zvz.kookie.item.enchantment

import be.zvz.kookie.entity.Entity

class SharpnessEnchantment(
    internalRuntimeId: Int,
    name: String,
    rarity: Rarity,
    primaryItemFlags: Array<ItemFlags>,
    secondaryItemFlags: Array<ItemFlags>,
    maxLevel: Int
) : MeleeWeaponEnchantment(
    internalRuntimeId, name,
    rarity,
    primaryItemFlags, secondaryItemFlags, maxLevel
) {
    override fun isApplicableTo(victim: Entity): Boolean = true

    override fun getDamageBonus(enchantmentLevel: Int): Float = 0.5F * (enchantmentLevel + 1)
}
