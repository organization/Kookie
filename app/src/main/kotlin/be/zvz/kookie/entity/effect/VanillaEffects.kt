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
    ),
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
            fatal = true
        )
    ),
    FIRE_RESISTANCE(
        Effect(
            internalRuntimeId = EffectIds.FIRE_RESISTANCE,
            name = "%potion.fireResistance",
            color = Color(0xe4, 0x9a, 0x3a)
        )
    ),
    HASTE(
        Effect(
            internalRuntimeId = EffectIds.HASTE,
            name = "%potion.digSpeed",
            color = Color(0xd9, 0xc0, 0x43)
        )
    ),
    HEALTH_BOOST(
        HealthBoostEffect(
            internalRuntimeId = EffectIds.HEALTH_BOOST,
            name = "%potion.healthBoost",
            color = Color(0xf8, 0x7d, 0x23)
        )
    ),
    HUNGER(
        HungerEffect(
            internalRuntimeId = EffectIds.HUNGER,
            name = "%potion.hunger",
            color = Color(0x58, 0x76, 0x53),
            bad = true
        )
    ),
    INSTANT_DAMAGE(
        InstantDamageEffect(
            internalRuntimeId = EffectIds.INSTANT_DAMAGE,
            name = "%potion.harm",
            color = Color(0x43, 0x0a, 0x09)
        )
    ),
    INSTANT_HEALTH(
        InstantHealthEffect(
            internalRuntimeId = EffectIds.INSTANT_HEALTH,
            name = "%potion.heal",
            color = Color(0xf8, 0x24, 0x23)
        )
    ),
    INVISIBILITY(
        InvisibilityEffect(
            internalRuntimeId = EffectIds.INVISIBILITY,
            name = "%potion.invisibility",
            color = Color(0x7f, 0x83, 0x23)
        )
    ),
    JUMP_BOOST(
        Effect(
            internalRuntimeId = EffectIds.JUMP_BOOST,
            name = "%potion.jump",
            color = Color(0x22, 0xff, 0x4c)
        )
    ),
    LEVITATION(
        LevitationEffect(
            internalRuntimeId = EffectIds.LEVITATION,
            name = "%potion.levitation",
            color = Color(0xce, 0xff, 0xff)
        )
    ),
    MINING_FATIGUE(
        Effect(
            internalRuntimeId = EffectIds.MINING_FATIGUE,
            name = "%potion.digSlowDown",
            color = Color(0x4a, 0x42, 0x17),
            bad = true
        )
    ),
    NAUSEA(
        Effect(
            internalRuntimeId = EffectIds.NAUSEA,
            name = "%potion.confusion",
            color = Color(0x55, 0x1d, 0x4a),
            bad = true
        )
    ),
    NIGHT_VISION(
        Effect(
            internalRuntimeId = EffectIds.NIGHT_VISION,
            name = "%potion.nightVision",
            color = Color(0x1f, 0x1f, 0xa1)
        )
    ),
    POISON(
        PoisonEffect(
            internalRuntimeId = EffectIds.POISON,
            name = "%potion.poison",
            color = Color(0x43, 0x93, 0x31),
            bad = true
        )
    ),
    REGENERATION(
        RegenerationEffect(
            internalRuntimeId = EffectIds.REGENERATION,
            name = "%potion.regeneration",
            color = Color(0xcd, 0x5c, 0xab)
        )
    ),
    RESISTANCE(
        Effect(
            internalRuntimeId = EffectIds.RESISTANCE,
            name = "%potion.resistance",
            color = Color(0x99, 0x45, 0x3a)
        )
    ),
    SATURATION(
        Effect(
            internalRuntimeId = EffectIds.SATURATION,
            name = "%potion.saturation",
            color = Color(0xf8, 0x24, 0x23),
            bad = false,
            hasBubbles = false
        )
    ),
    SLOWNESS(
        SlownessEffect(
            internalRuntimeId = EffectIds.SLOWNESS,
            name = "%potion.moveSlowdown",
            color = Color(0x5a, 0x6c, 0x23),
            bad = true
        )
    ),
    SPEED(
        SpeedEffect(
            internalRuntimeId = EffectIds.SPEED,
            name = "%potion.speed",
            color = Color(0x7c, 0xaf, 0xc6)
        )
    ),
    STRENGTH(
        Effect(
            internalRuntimeId = EffectIds.STRENGTH,
            name = "%potion.damageBoost",
            color = Color(0x93, 0x24, 0x23)
        )
    ),
    WATER_BREATHING(
        Effect(
            internalRuntimeId = EffectIds.WATER_BREATHING,
            name = "%potion.waterBreathing",
            color = Color(0x2e, 0x52, 0x99)
        )
    ),
    WEAKNESS(
        Effect(
            internalRuntimeId = EffectIds.WEAKNESS,
            name = "%potion.waterBreathing",
            color = Color(0x2e, 0x52, 0x99),
            bad = true
        )
    ),
    WITHER(
        WitherEffect(
            internalRuntimeId = EffectIds.WITHER,
            name = "%potion.wither",
            color = Color(0x35, 0x2a, 0x27),
            bad = true
        )
    )
}
