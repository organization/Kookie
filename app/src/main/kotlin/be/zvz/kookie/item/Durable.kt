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
import be.zvz.kookie.nbt.tag.CompoundTag
import java.util.Random
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

    protected open fun getUnbreakingDamageReduction(amount: Int): Int {
        val unbreakingLevel = getEnchantmentLevel(VanillaEnchantments.UNBREAKING.enchantment)
        if (unbreakingLevel > 0) {
            var negated = 0
            val chance = 1f / (unbreakingLevel + 1)
            for (i in 0 until amount) {
                if (Random().nextFloat() > chance) {
                    ++negated
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
