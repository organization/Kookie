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
package be.zvz.kookie.data.bedrock

import be.zvz.kookie.item.PotionType
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps

object PotionTypeIdMap {

    private val idToEnum: MutableMap<Int, PotionType> = HashIntObjMaps.newMutableMap()

    private val enumToId: MutableMap<PotionType, Int> = HashObjIntMaps.newMutableMap()

    init {
        register(PotionTypeIds.WATER.id, PotionType.WATER)
        register(PotionTypeIds.MUNDANE.id, PotionType.MUNDANE)
        register(PotionTypeIds.LONG_MUNDANE.id, PotionType.LONG_MUNDANE)
        register(PotionTypeIds.THICK.id, PotionType.THICK)
        register(PotionTypeIds.AWKWARD.id, PotionType.AWKWARD)
        register(PotionTypeIds.NIGHT_VISION.id, PotionType.NIGHT_VISION)
        register(PotionTypeIds.LONG_NIGHT_VISION.id, PotionType.LONG_NIGHT_VISION)
        register(PotionTypeIds.INVISIBILITY.id, PotionType.INVISIBILITY)
        register(PotionTypeIds.LONG_INVISIBILITY.id, PotionType.LONG_INVISIBILITY)
        register(PotionTypeIds.LEAPING.id, PotionType.LEAPING)
        register(PotionTypeIds.LONG_LEAPING.id, PotionType.LONG_LEAPING)
        register(PotionTypeIds.STRONG_LEAPING.id, PotionType.STRONG_LEAPING)
        register(PotionTypeIds.FIRE_RESISTANCE.id, PotionType.FIRE_RESISTANCE)
        register(PotionTypeIds.LONG_FIRE_RESISTANCE.id, PotionType.LONG_FIRE_RESISTANCE)
        register(PotionTypeIds.SWIFTNESS.id, PotionType.SWIFTNESS)
        register(PotionTypeIds.LONG_SWIFTNESS.id, PotionType.LONG_SWIFTNESS)
        register(PotionTypeIds.STRONG_SWIFTNESS.id, PotionType.STRONG_SWIFTNESS)
        register(PotionTypeIds.SLOWNESS.id, PotionType.SLOWNESS)
        register(PotionTypeIds.LONG_SLOWNESS.id, PotionType.LONG_SLOWNESS)
        register(PotionTypeIds.WATER_BREATHING.id, PotionType.WATER_BREATHING)
        register(PotionTypeIds.LONG_WATER_BREATHING.id, PotionType.LONG_WATER_BREATHING)
        register(PotionTypeIds.HEALING.id, PotionType.HEALING)
        register(PotionTypeIds.STRONG_HEALING.id, PotionType.STRONG_HEALING)
        register(PotionTypeIds.HARMING.id, PotionType.HARMING)
        register(PotionTypeIds.STRONG_HARMING.id, PotionType.STRONG_HARMING)
        register(PotionTypeIds.POISON.id, PotionType.POISON)
        register(PotionTypeIds.LONG_POISON.id, PotionType.LONG_POISON)
        register(PotionTypeIds.STRONG_POISON.id, PotionType.STRONG_POISON)
        register(PotionTypeIds.REGENERATION.id, PotionType.REGENERATION)
        register(PotionTypeIds.LONG_REGENERATION.id, PotionType.LONG_REGENERATION)
        register(PotionTypeIds.STRONG_REGENERATION.id, PotionType.STRONG_REGENERATION)
        register(PotionTypeIds.STRENGTH.id, PotionType.STRENGTH)
        register(PotionTypeIds.LONG_STRENGTH.id, PotionType.LONG_STRENGTH)
        register(PotionTypeIds.STRONG_STRENGTH.id, PotionType.STRONG_STRENGTH)
        register(PotionTypeIds.WEAKNESS.id, PotionType.WEAKNESS)
        register(PotionTypeIds.LONG_WEAKNESS.id, PotionType.LONG_WEAKNESS)
        register(PotionTypeIds.WITHER.id, PotionType.WITHER)
        register(PotionTypeIds.TURTLE_MASTER.id, PotionType.TURTLE_MASTER)
        register(PotionTypeIds.LONG_TURTLE_MASTER.id, PotionType.LONG_TURTLE_MASTER)
        register(PotionTypeIds.STRONG_TURTLE_MASTER.id, PotionType.STRONG_TURTLE_MASTER)
        register(PotionTypeIds.SLOW_FALLING.id, PotionType.SLOW_FALLING)
        register(PotionTypeIds.LONG_SLOW_FALLING.id, PotionType.LONG_SLOW_FALLING)
    }

    fun register(id: Int, type: PotionType) {
        idToEnum[id] = type
        enumToId[type] = id
    }

    fun toId(type: PotionType): Int? = enumToId[type]

    fun fromId(id: Int): PotionType? = idToEnum[id]
}
