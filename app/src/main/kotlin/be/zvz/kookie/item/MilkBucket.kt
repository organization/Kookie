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

import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.effect.EffectInstance

class MilkBucket(identifier: ItemIdentifier, name: String) : Item(identifier, name), ConsumableItem {
    override val maxStackSize: Int = 1

    override fun getResidue(): Item = TODO("return VanillaItems.BUCKET")
    override fun getAdditionalEffects(): List<EffectInstance> = emptyList()

    override fun onConsume(consumer: Living) {
        consumer.getEffects().clear()
    }
}
