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
package be.zvz.kookie.inventory

import be.zvz.kookie.entity.Living
import be.zvz.kookie.item.Item

class ArmorInventory(val holder: Living) : SimpleInventory(4) {
    fun getHelmet(): Item = getItem(SLOT_HEAD)
    fun getChestplate(): Item = getItem(SLOT_CHEST)
    fun getLeggings(): Item = getItem(SLOT_LEGS)
    fun getBoots(): Item = getItem(SLOT_FEET)
    fun setHelmet(helmet: Item) {
        setItem(SLOT_HEAD, helmet)
    }
    fun setChestplate(chestplate: Item) {
        setItem(SLOT_CHEST, chestplate)
    }
    fun setLeggings(leggings: Item) {
        setItem(SLOT_LEGS, leggings)
    }
    fun setBoots(boots: Item) {
        setItem(SLOT_FEET, boots)
    }

    companion object {
        const val SLOT_HEAD = 0
        const val SLOT_CHEST = 1
        const val SLOT_LEGS = 2
        const val SLOT_FEET = 3
    }
}
