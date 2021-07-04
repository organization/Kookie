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

enum class PotionType(val displayName: String, val effectsGetter: () -> List<EffectInstance>) {
    WATER(
        "Water",
        {
            listOf(EffectInstance(VanillaEffects.NIGHT_VISION.effect, 3600))
        }
    ),
    MUNDANE("Mundane", { listOf() }),
    LONG_MUNDANE("Long Mundane", { listOf() }),
    THICK("Thick", { listOf() }),
    AWKWARD("Awkward", { listOf() }),
    NIGHT_VISION(
        "Night Vision",
        {
            listOf(
                EffectInstance(VanillaEffects.NIGHT_VISION.effect, 3600)
            )
        }
    ),
    LONG_NIGHT_VISION(
        "Long Night Vision",
        {
            listOf(
                EffectInstance(VanillaEffects.NIGHT_VISION.effect, 9600)
            )
        }
    ),
    INVISIBILITY(
        "Invisibility",
        {
            listOf(
                EffectInstance(VanillaEffects.INVISIBILITY.effect, 3600)
            )
        }
    ),
    LONG_INVISIBILITY(
        "Long Invisibility",
        {
            listOf(
                EffectInstance(VanillaEffects.INVISIBILITY.effect, 9600)
            )
        }
    ),
    LEAPING(
        "Leaping",
        {
            listOf(
                EffectInstance(VanillaEffects.JUMP_BOOST.effect, 3600)
            )
        }
    ),
    LONG_LEAPING(
        "Long Leaping",
        {
            listOf(
                EffectInstance(VanillaEffects.JUMP_BOOST.effect, 9600)
            )
        }
    ),
    STRONG_LEAPING(
        "Strong Leaping",
        {
            listOf(
                EffectInstance(VanillaEffects.JUMP_BOOST.effect, 1800, 1)
            )
        }
    ),
    FIRE_RESISTANCE(
        "Fire Resistance",
        {
            listOf(
                EffectInstance(VanillaEffects.FIRE_RESISTANCE.effect, 3600)
            )
        }
    ),
    LONG_FIRE_RESISTANCE(
        "Long Fire Resistance",
        {
            listOf(
                EffectInstance(VanillaEffects.FIRE_RESISTANCE.effect, 9600)
            )
        }
    ),
    SWIFTNESS(
        "Swiftness",
        {
            listOf(
                EffectInstance(VanillaEffects.SPEED.effect, 3600)
            )
        }
    ),
    LONG_SWIFTNESS(
        "Long Swiftness",
        {
            listOf(
                EffectInstance(VanillaEffects.SPEED.effect, 9600)
            )
        }
    ),
    STRONG_SWIFTNESS(
        "Strong Swiftness",
        {
            listOf(
                EffectInstance(VanillaEffects.SPEED.effect, 1800, 1)
            )
        }
    ),
    SLOWNESS(
        "Slowness",
        {
            listOf(
                EffectInstance(VanillaEffects.SLOWNESS.effect, 1800)
            )
        }
    ),
    LONG_SLOWNESS(
        "Long Slowness",
        {
            listOf(
                EffectInstance(VanillaEffects.SLOWNESS.effect, 4800)
            )
        }
    ),
    WATER_BREATHING(
        "Water Breathing",
        {
            listOf(
                EffectInstance(VanillaEffects.WATER_BREATHING.effect, 3600)
            )
        }
    ),
    LONG_WATER_BREATHING(
        "Long Water Breathing",
        {
            listOf(
                EffectInstance(VanillaEffects.WATER_BREATHING.effect, 9600)
            )
        }
    ),
    HEALING(
        "Healing",
        {
            listOf(
                EffectInstance(VanillaEffects.INSTANT_HEALTH.effect)
            )
        }
    ),
    STRONG_HEALING(
        "Strong Healing",
        {
            listOf(
                EffectInstance(VanillaEffects.INSTANT_HEALTH.effect, 0, 1)
            )
        }
    ),
    HARMING(
        "Harming",
        {
            listOf(
                EffectInstance(VanillaEffects.INSTANT_DAMAGE.effect)
            )
        }
    ),
    STRONG_HARMING(
        "Strong Harming",
        {
            listOf(
                EffectInstance(VanillaEffects.INSTANT_DAMAGE.effect, 0, 1)
            )
        }
    ),
    POISON(
        "Poison",
        {
            listOf(
                EffectInstance(VanillaEffects.POISON.effect, 900)
            )
        }
    ),
    LONG_POISON(
        "Long Poison",
        {
            listOf(
                EffectInstance(VanillaEffects.POISON.effect, 2400)
            )
        }
    ),
    STRONG_POISON(
        "Strong Poison",
        {
            listOf(
                EffectInstance(VanillaEffects.POISON.effect, 440, 1)
            )
        }
    ),
    REGENERATION(
        "Regeneration",
        {
            listOf(
                EffectInstance(VanillaEffects.REGENERATION.effect, 900)
            )
        }
    ),
    LONG_REGENERATION(
        "Long Regeneration",
        {
            listOf(
                EffectInstance(VanillaEffects.REGENERATION.effect, 2400)
            )
        }
    ),
    STRONG_REGENERATION(
        "Strong Regeneration",
        {
            listOf(
                EffectInstance(VanillaEffects.REGENERATION.effect, 440, 1)
            )
        }
    ),
    STRENGTH(
        "Strength",
        {
            listOf(
                EffectInstance(VanillaEffects.STRENGTH.effect, 3600)
            )
        }
    ),
    LONG_STRENGTH(
        "Long Strength",
        {
            listOf(
                EffectInstance(VanillaEffects.STRENGTH.effect, 9600)
            )
        }
    ),
    STRONG_STRENGTH(
        "Strong Strength",
        {
            listOf(
                EffectInstance(VanillaEffects.STRENGTH.effect, 1800, 1)
            )
        }
    ),
    WEAKNESS(
        "Weakness",
        {
            listOf(
                EffectInstance(VanillaEffects.WEAKNESS.effect, 1800)
            )
        }
    ),
    LONG_WEAKNESS(
        "Long Weakness",
        {
            listOf(
                EffectInstance(VanillaEffects.WEAKNESS.effect, 4800)
            )
        }
    ),
    WITHER(
        "Wither",
        {
            listOf(
                EffectInstance(VanillaEffects.WITHER.effect, 800, 1)
            )
        }
    ),
    TURTLE_MASTER(
        "Turtle Master",
        {
            listOf(
                // TODO
            )
        }
    ),
    LONG_TURTLE_MASTER(
        "Long Turtle Master",
        {
            listOf(
                // TODO
            )
        }
    ),
    STRONG_TURTLE_MASTER(
        "Strong Turtle Master",
        {
            listOf(
                // TODO
            )
        }
    ),
    SLOW_FALLING(
        "Slow Falling",
        {
            listOf(
                // TODO
            )
        }
    ),
    LONG_SLOW_FALLING(
        "Long Slow Falling",
        {
            listOf(
                // TODO
            )
        }
    )
}
