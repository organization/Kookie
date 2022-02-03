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
package be.zvz.kookie.network.mcpe.protocol.serializer

import be.zvz.kookie.network.mcpe.protocol.types.ItemTypeEntry
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps

class ItemTypeDictionary(val itemTypes: List<ItemTypeEntry>) {

    private val stringToIntMap: MutableMap<String, Int> = HashObjObjMaps.newMutableMap()
    private val intToStringIdMap: MutableMap<Int, String> = HashIntObjMaps.newMutableMap()

    init {
        itemTypes.forEach { type ->
            stringToIntMap[type.stringId] = type.numericId
            intToStringIdMap[type.numericId] = type.stringId
        }
    }

    fun getEntries(): List<ItemTypeEntry> = itemTypes

    fun fromStringId(id: String): Int = stringToIntMap.getValue(id)

    fun fromIntId(id: Int): String = intToStringIdMap.getValue(id)
}
