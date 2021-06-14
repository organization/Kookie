package be.zvz.kookie.item

import be.zvz.kookie.item.enchantment.VanillaEnchantments
import kotlin.math.pow

abstract class Tool(identifier: ItemIdentifier, name: String) : Durable(identifier, name) {

    override val maxStackSize: Int = 1
    open val baseMiningEfficiency: Float = 1f

    override fun getMiningEfficiency(isCorrectTool: Boolean): Float {
        var efficiency = 1f
        if (isCorrectTool) {
            efficiency = baseMiningEfficiency
            val level = getEnchantmentLevel(VanillaEnchantments.EFFICIENCY.enchantment) + 0f
            if (level > 0) {
                efficiency += level.pow(2) + 1
            }
        }

        return efficiency
    }
}
