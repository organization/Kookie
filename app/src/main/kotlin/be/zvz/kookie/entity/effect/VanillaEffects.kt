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
package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.data.bedrock.EffectIds

enum class VanillaEffects(val effect: Effect) {
    ABSORPTION(
        AbsorptionEffect(
            internalRuntimeId = EffectIds.ABSORPTION,
            name = "%potion.absorption",
            color = Color(0x25, 0x52, 0xa5)
        )
    ), // AbsorptionEffect
    BLINDNESS(
        Effect(
            internalRuntimeId = EffectIds.BLINDNESS,
            name = "%potion.blindness",
            color = Color(0x1f, 0x1f, 0x23),
            bad = true
        )
    ),
    CONDUIT_POWER(
        Effect(
            internalRuntimeId = EffectIds.CONDUIT_POWER,
            name = "%potion.conduitPower",
            color = Color(0x1d, 0xc2, 0xd1)
        )
    ),
    FATAL_POISON(
        PoisonEffect(
            internalRuntimeId = EffectIds.FATAL_POISON,
            name = "%potion.poison",
            color = Color(0x4e, 0x93, 0x31),
            bad = true,
            hasBubbles = true,
            fatal = true
        )
    ), // PoisonEffect

    /*
    FIRE_RESISTANCE(),
    HASTE(),
    HEALTH_BOOST(), // HealthBoostEffect
    HUNGER(), // HungerEffect
    INSTANT_DAMAGE(), // InstantDamageEffect
    INSTANT_HEALTH(), // InstantHealthEffect
    INVISIBILITY(), // InvisibilityEffect
    */
    JUMP_BOOST(
        Effect(
            internalRuntimeId = EffectIds.JUMP_BOOST,
            name = "%potion.jump",
            color = Color(0xce, 0xff, 0xff)
        )
    ),
    /*
    LEVITATION(), // LevitationEffect
    MINING_FATIGUE(),
    NAUSEA(),
    NIGHT_VISION(),
    POISON(), // PoisonEffect
    REGENERATION(), // RegenerationEffect
    RESISTANCE(),
    SATURATION(), // SaturationEffect
    SLOWNESS(), // SlownessEffect
    SPEED(), // SpeedEffect
    STRENGTH(),
    WEAKNESS(),
    WITHER() // WitherEffect

     */
}
