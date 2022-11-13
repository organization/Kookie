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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.item

import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.player.Player

abstract class Food(identifier: ItemIdentifier, name: String) : Item(identifier, name), FoodSourceItem {

    override val requiresHunger: Boolean = true

    override fun getResidue(): Item = ItemFactory.air()

    override fun getAdditionalEffects(): List<EffectInstance> = listOf()

    override fun onConsume(consumer: Living) {
    }

    override fun canStartUsingItem(player: Player): Boolean {
        return !requiresHunger || player.hungerManager.isHungry()
    }
}
