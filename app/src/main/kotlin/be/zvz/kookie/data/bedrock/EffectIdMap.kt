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
package be.zvz.kookie.data.bedrock

import be.zvz.kookie.entity.effect.Effect
import be.zvz.kookie.entity.effect.VanillaEffects
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps

object EffectIdMap {

    private val idToEffect: MutableMap<Int, Effect> = HashIntObjMaps.newMutableMap()
    private val effectToId: MutableMap<Effect, Int> = HashObjIntMaps.newMutableMap()

    init {
        register(EffectIds.SPEED, VanillaEffects.SPEED)
        register(EffectIds.SLOWNESS, VanillaEffects.SLOWNESS)
        register(EffectIds.HASTE, VanillaEffects.HASTE)
        register(EffectIds.MINING_FATIGUE, VanillaEffects.MINING_FATIGUE)
        register(EffectIds.STRENGTH, VanillaEffects.STRENGTH)
        register(EffectIds.INSTANT_HEALTH, VanillaEffects.INSTANT_HEALTH)
        register(EffectIds.INSTANT_DAMAGE, VanillaEffects.INSTANT_DAMAGE)
        register(EffectIds.JUMP_BOOST, VanillaEffects.JUMP_BOOST)
        register(EffectIds.NAUSEA, VanillaEffects.NAUSEA)
        register(EffectIds.REGENERATION, VanillaEffects.REGENERATION)
        register(EffectIds.RESISTANCE, VanillaEffects.RESISTANCE)
        register(EffectIds.FIRE_RESISTANCE, VanillaEffects.FIRE_RESISTANCE)
        register(EffectIds.WATER_BREATHING, VanillaEffects.WATER_BREATHING)
        register(EffectIds.INVISIBILITY, VanillaEffects.INVISIBILITY)
        register(EffectIds.BLINDNESS, VanillaEffects.BLINDNESS)
        register(EffectIds.NIGHT_VISION, VanillaEffects.NIGHT_VISION)
        register(EffectIds.HUNGER, VanillaEffects.HUNGER)
        register(EffectIds.WEAKNESS, VanillaEffects.WEAKNESS)
        register(EffectIds.POISON, VanillaEffects.POISON)
        register(EffectIds.WITHER, VanillaEffects.WITHER)
        register(EffectIds.HEALTH_BOOST, VanillaEffects.HEALTH_BOOST)
        register(EffectIds.ABSORPTION, VanillaEffects.ABSORPTION)
        register(EffectIds.SATURATION, VanillaEffects.SATURATION)
        register(EffectIds.LEVITATION, VanillaEffects.LEVITATION)
        register(EffectIds.FATAL_POISON, VanillaEffects.FATAL_POISON)
        register(EffectIds.CONDUIT_POWER, VanillaEffects.CONDUIT_POWER)
    }

    @JvmStatic
    fun register(mcpeId: Int, effect: VanillaEffects) {
        idToEffect[mcpeId] = effect.effect
        effectToId[effect.effect] = mcpeId
    }

    @JvmStatic
    fun fromId(id: Int): Effect? = idToEffect[id]

    @JvmStatic
    fun toId(effect: Effect): Int = effectToId.getValue(effect)
}
