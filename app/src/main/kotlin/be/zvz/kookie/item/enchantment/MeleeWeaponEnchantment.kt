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
