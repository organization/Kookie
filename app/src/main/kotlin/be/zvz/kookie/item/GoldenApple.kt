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

import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.entity.effect.VanillaEffects

class GoldenApple(identifier: ItemIdentifier, vanillaName: String = "Unknown") : Food(identifier, vanillaName) {
    override val foodRestore: Int = 4
    override val saturationRestore: Float = 9.6F
    override val requiresHunger: Boolean = true

    override fun getAdditionalEffects(): List<EffectInstance> = mutableListOf(
        EffectInstance(VanillaEffects.REGENERATION.effect, 100, 1),
        EffectInstance(VanillaEffects.ABSORPTION.effect, 2400),
    )
}
