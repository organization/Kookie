package be.zvz.kookie.item.enchantment

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class KnockbackEnchantment(
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
    override fun isApplicableTo(victim: Entity): Boolean = victim is Living

    override fun getDamageBonus(enchantmentLevel: Int): Float = 0F

    override fun onPostAttack(attacker: Entity, victim: Entity, enchantmentLevel: Int) {
        if (victim is Living) {
            TODO("diff, victim")
        }
    }
}
