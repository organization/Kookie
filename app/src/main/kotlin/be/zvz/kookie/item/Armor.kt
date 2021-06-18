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

import be.zvz.kookie.color.Color
import be.zvz.kookie.event.EntityDamageEvent
import be.zvz.kookie.item.enchantment.ProtectionEnchantment
import be.zvz.kookie.item.enchantment.VanillaEnchantments
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.player.Player
import be.zvz.kookie.utils.Binary
import be.zvz.kookie.utils.inline.forEachValue
import java.util.Random

class Armor(identifier: ItemIdentifier, name: String, val armorInfo: ArmorTypeInfo) : Durable(identifier, name) {
    var customColor: Color? = null
    override val maxDurability: Int = armorInfo.maxDurability
    override val defensePoints: Int = armorInfo.defensePoints
    val armorSlot: Int = armorInfo.armorSlot
    override val maxStackSize: Int = 1

    fun getEnchantmentProtectionFactor(event: EntityDamageEvent): Int {
        var epf = 0

        enchantments.forEachValue {
            val type = it.enchantment
            if (type is ProtectionEnchantment && type.isApplicable(event)) {
                epf += type.getProtectionFactor(it.level)
            }
        }

        return epf
    }

    override fun getUnbreakingDamageReduction(amount: Int): Int {
        val unbreakingLevel = getEnchantmentLevel(VanillaEnchantments.UNBREAKING.enchantment)
        if (unbreakingLevel > 0) {
            var negated = 0
            val chance = 1f / (unbreakingLevel + 1)
            repeat(amount) {
                if (Random().nextFloat() >= 0.6f && Random().nextFloat() > chance) {
                    ++negated
                }
            }
            return negated
        }
        return 0
    }

    override fun onClickAir(player: Player, directionVector: Vector3): ItemUseResult {
        val existing = player.armorInventory.getItem(armorSlot)
        val thisCopy = clone()
        val new = thisCopy.pop()
        player.armorInventory.setItem(armorSlot, new)
        if (thisCopy.count == 0) {
            player.inventory.setItemInHand(existing)
        } else { // if the stack size was bigger than 1 (usually won't happen, but might be caused by plugins
            player.inventory.setItemInHand(thisCopy)
            player.inventory.addItem(existing)
        }
        return ItemUseResult.SUCCESS
    }

    override fun deserializeCompoundTag(tag: CompoundTag) {
        super.deserializeCompoundTag(tag)
        val colorTag = tag.getTag(TAG_CUSTOM_COLOR)
        customColor = colorTag?.let {
            Color.fromARGB(Binary.unsignInt(colorTag.value as Int))
        }
    }

    override fun serializeCompoundTag(tag: CompoundTag) {
        super.serializeCompoundTag(tag)
        customColor?.let {
            tag.setInt(TAG_CUSTOM_COLOR, Binary.signInt(it.toARGB()))
        } ?: run {
            tag.removeTag(TAG_CUSTOM_COLOR)
        }
    }

    companion object {
        const val TAG_CUSTOM_COLOR: String = "customColor"
    }
}
