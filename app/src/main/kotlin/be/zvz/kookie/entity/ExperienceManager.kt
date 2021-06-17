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
package be.zvz.kookie.entity

import be.zvz.kookie.entity.utils.ExperienceUtils
import be.zvz.kookie.item.Durable
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.enchantment.VanillaEnchantments
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class ExperienceManager(private val entity: Human) {

    private var levelAttr: Attribute = fetchAttribute(entity, Attribute.Identifier.EXPERIENCE_LEVEL)

    private var progressAttr: Attribute = fetchAttribute(entity, Attribute.Identifier.EXPERIENCE)

    var totalXp: Int = 0

    var xpCooldown: Long = 0
        private set

    fun getXpLevel(): Int = levelAttr.currentValue.toInt()

    fun setXpLevel(level: Int): Boolean = setXpAndProgress(level, null)

    fun addXpLevels(amount: Int, playSound: Boolean = true): Boolean {
        val oldLevel = getXpLevel()
        if (setXpLevel(oldLevel + amount)) {
            if (playSound) {
                val newLevel = getXpLevel()
                if ((newLevel / 5) > (oldLevel / 5)) {
                    // TODO: entity.broadcastSound(XpLevelUpSound(newLevel))
                }
            }
            return true
        }
        return false
    }

    fun subtractLevels(amount: Int): Boolean = addXpLevels(-amount)

    fun getXpProgress(): Float = progressAttr.currentValue

    fun setXpProgress(progress: Float): Boolean = setXpAndProgress(null, progress)

    fun getRemainderXp(): Int = ExperienceUtils.getXpToCompleteLevel((getXpLevel() * getXpProgress()).toInt())

    fun getCurrentTotalXp(): Int = ExperienceUtils.getXpToReachLevel(getXpLevel()) + getRemainderXp()

    fun setCurrentTotalXp(xp: Int): Boolean {
        val newLevel = ExperienceUtils.getLevelFromXp(xp)
        return setXpAndProgress(newLevel.toInt(), newLevel - newLevel.toInt())
    }

    fun addXp(amount: Int, playSound: Boolean = true): Boolean {
        val oldLevel = getXpLevel()
        val oldTotal = getCurrentTotalXp()

        if (setCurrentTotalXp(oldTotal + amount)) {
            if (amount > 0) {
                totalXp += amount
            }
            if (playSound) {
                val newLevel = getXpLevel()
                if ((newLevel / 5) > (oldLevel / 5)) {
                    // TODO: entity.broadcastSound(XpLevelUpSound(newLevel))
                } else if (getCurrentTotalXp() > oldTotal) {
                    // TODO: entity.broadcastSound(XpCollectSound())
                }
            }
            return true
        }
        return false
    }

    fun subtractXp(amount: Int): Boolean = addXp(-amount)

    fun setXpAndProgress(level: Int?, progress: Float?): Boolean {
        /*
        TODO:
        val ev = PlayerExperienceChangeEvent(entity, getXpLevel(), getXpProgress(), level, progress)
        ev.call()
        if (ev.isCancelled()) {
        return false
        } 
         */

        // FIXME: level = ev.getNewLevel()
        // FIXME: progress = ev.getNewProgress()

        level?.let {
            levelAttr.setValue(it.toFloat())
        }

        progress?.let {
            progressAttr.setValue(it)
        }
        return true
    }

    fun setXpAndProgressNoEvent(level: Int, progress: Float) {
        levelAttr.setValue(level.toFloat())
        progressAttr.setValue(progress)
    }

    fun getLifetimeTotalXp(): Int = totalXp

    fun canPickUpXp(): Boolean = xpCooldown == 0L

    fun onPickUpXp(xpValue: Int) {
        var mainHandIndex = -1
        var offHandIndex = -2

        var xpValue = xpValue

        val equipment: MutableMap<Int, Durable> = mutableMapOf()

        var item: Item = entity.inventory.getItemInHand()

        if (item is Durable && item.hasEnchantment(VanillaEnchantments.MENDING.enchantment)) {
            equipment[mainHandIndex] = item
        }

        item = entity.offHandInventory.getItem(0)

        if (item is Durable && item.hasEnchantment(VanillaEnchantments.MENDING.enchantment)) {
            equipment[offHandIndex] = item
        }

        entity.armorInventory.getContents().forEach { (k, item) ->
            if (item is Durable && item.hasEnchantment(VanillaEnchantments.MENDING.enchantment)) {
                equipment[k] = item
            }
        }

        if (equipment.isNotEmpty()) {
            val (index, repairItem) = equipment.toList().random()

            if (repairItem.damage > 0) {
                val repairAmount = min(repairItem.damage, xpValue * 2)
                repairItem.damage = repairAmount
                xpValue -= ceil((repairAmount / 2).toDouble()).toInt()
            }

            if (index == mainHandIndex) {
                entity.inventory.setItemInHand(repairItem)
            } else if (index == offHandIndex) {
                entity.offHandInventory.setItem(0, repairItem)
            } else {
                entity.armorInventory.setItem(index, repairItem)
            }
        }

        addXp(xpValue)
        resetXpCooldown()
    }

    fun resetXpCooldown(value: Long = 2) {
        xpCooldown = value
    }

    fun tick(tickDiff: Long) {
        if (xpCooldown > 0) {
            xpCooldown = max(0, xpCooldown - tickDiff)
        }
    }

    companion object {
        @JvmStatic
        private fun fetchAttribute(entity: Human, attribute: Attribute.Identifier): Attribute {
            val attr = AttributeFactory.mustGet(attribute)
            entity.attributeMap.add(attr)
            return attr
        }
    }
}
