package be.zvz.kookie.item

import be.zvz.kookie.item.enchantment.VanillaEnchantments
import java.util.*
import kotlin.math.min

abstract class Durable(identifier: ItemIdentifier) : Item(identifier) {
    override var damage = 0
    var unbreakable = false
    override fun getMeta(): Int = damage
    fun applyDamage(amountArg: Int): Boolean {
        if (unbreakable || isBroken()) {
            return false
        }
        val amount = amountArg - getUnbreakingDamageReduction(amountArg)
        damage = min(damage + amount, getMaxDurability())
        if (isBroken()) {
            onBroken()
        }

        return true
    }

    protected fun getUnbreakingDamageReduction(amount: Int): Int {
        val unbreakingLevel = getEnchantmentLevel(VanillaEnchantments.UNBREAKING.enchantment)
        if (unbreakingLevel > 0) {
            var negated = 0
            val chance = 1 / (unbreakingLevel + 1)
            for (i in 0..amount) {
                if (Random().nextFloat() > chance) {
                    negated++
                }
            }
            return negated
        }
        return 0
    }

    abstract fun getMaxDurability(): Int

    fun onBroken() {
        pop()
    }

    fun isBroken(): Boolean {
        return damage >= getMaxDurability()
    }
}
