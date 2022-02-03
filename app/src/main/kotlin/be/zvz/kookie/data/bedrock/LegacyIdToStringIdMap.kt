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

import be.zvz.kookie.utils.config.JsonBrowser
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps

enum class LegacyIdToStringIdMap(path: String) {
    ENTITY("entity_id_map"),
    ITEM("item_id_map"),
    BLOCK("block_id_map");

    val legacyToString: MutableMap<Int, String> = HashIntObjMaps.newMutableMap()
    val stringToLegacy: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()

    init {
        val data = JsonBrowser().parse(this::class.java.getResourceAsStream("/vanilla/$path.json")).toMap<String, Int>()
        data.forEach { (stringId, legacyId) ->
            legacyToString[legacyId] = stringId
            stringToLegacy[stringId] = legacyId
        }
    }
}
