package be.zvz.kookie.item.enchantment

class ProtectionEnchantment(
    internalRuntimeId: Int,
    name: String,
    rarity: Int,
    primaryItemFlags: Int,
    secondaryItemFlags: Int,
    maxLevel: Int,
    typeModifier: Float,
    applicableDamageTypes: MutableList<Int>?,
) : Enchantment(internalRuntimeId, name, rarity, primaryItemFlags, secondaryItemFlags, maxLevel)
