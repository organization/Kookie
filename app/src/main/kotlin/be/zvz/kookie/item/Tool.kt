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
