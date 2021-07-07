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

import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.entity.effect.VanillaEffects
import kotlin.random.Random

class RawChicken(identifier: ItemIdentifier, name: String) : Food(identifier, name) {
    override val foodRestore: Int = 1
    override val saturationRestore: Float = 1.2F

    override fun getAdditionalEffects(): List<EffectInstance> {
        if (Random.nextInt(0, 9) < 3) return mutableListOf(
            EffectInstance(VanillaEffects.HUNGER.effect, 600)
        )
        return emptyList()
    }
}
