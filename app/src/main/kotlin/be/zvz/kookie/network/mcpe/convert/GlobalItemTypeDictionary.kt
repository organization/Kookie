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
package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.network.mcpe.protocol.serializer.ItemTypeDictionary
import be.zvz.kookie.network.mcpe.protocol.types.ItemTypeEntry
import be.zvz.kookie.utils.Json
import com.fasterxml.jackson.core.type.TypeReference

object GlobalItemTypeDictionary {
    val dictionary: ItemTypeDictionary

    init {
        val data = Json.jsonMapper.readValue(
            this::class.java.getResourceAsStream("/vanilla/required_item_list.json"),
            object : TypeReference<Map<String, Map<String, String>>>() {}
        )

        val itemTypes: MutableList<ItemTypeEntry> = mutableListOf()
        data.forEach { (name, entry) ->
            val runtimeId: Int
            val componentBased: Boolean
            try {
                runtimeId = entry.getValue("runtime_id").toInt()
                componentBased = entry.getValue("component_based").toBoolean()
            } catch (ignored: NumberFormatException) {
                return@forEach
            } catch (ignored: NoSuchElementException) {
                return@forEach
            }

            itemTypes.add(ItemTypeEntry(name, runtimeId, componentBased))
        }
        dictionary = ItemTypeDictionary(itemTypes)
    }
}
