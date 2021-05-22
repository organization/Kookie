package be.zvz.kookie.item.enchantment

class ProtectionEnchantment(
    internalRuntimeId: Int,
    name: String,
    rarity: Int,
    primaryItemFlags: Int,
    secondaryItemFlags: Int,
    maxLevel: Int,
    val typeModifier: Float,
    val applicableDamageTypes: MutableList<Int>?,
) : Enchantment(internalRuntimeId, name, rarity, primaryItemFlags, secondaryItemFlags, maxLevel) {
    fun getProtectionFactor(level: Int): Int = ((6 + level * level) * typeModifier / 3).toInt()
}
