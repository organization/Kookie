package be.zvz.kookie.item

import be.zvz.kookie.item.enchantment.VanillaEnchantments
import be.zvz.kookie.nbt.tag.CompoundTag
import java.util.*
import kotlin.math.min

abstract class Durable(identifier: ItemIdentifier, name: String) : Item(identifier, name) {

    abstract val maxDurability: Int

    var damage: Int = 0
        set(value) {
            if (value < 0 || value > maxDurability) {
                throw IllegalArgumentException("Damage must be in range 0 - $maxDurability")
            }
            field = value
        }
    var unbreakable: Boolean = false

    override fun getMeta(): Int = damage

    fun applyDamage(amountArg: Int): Boolean {
        if (unbreakable || isBroken()) {
            return false
        }
        val amount = amountArg - getUnbreakingDamageReduction(amountArg)
        damage = min(damage + amount, maxDurability)
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

    fun onBroken() {
        pop()
    }

    fun isBroken(): Boolean {
        return damage >= maxDurability
    }

    override fun deserializeCompoundTag(tag: CompoundTag) {
        super.deserializeCompoundTag(tag)
        unbreakable = tag.getByte("Unbreakable", 0) != 0
    }

    override fun serializeCompoundTag(tag: CompoundTag) {
        super.serializeCompoundTag(tag)
        if (unbreakable) {
            tag.setByte("Unbreakable", 1)
        } else {
            tag.removeTag("Unbreakable")
        }
    }
}
