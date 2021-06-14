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

import be.zvz.kookie.utils.config.JsonBrowser
import com.koloboke.collect.map.hash.HashIntIntMaps
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.util.concurrent.atomic.AtomicBoolean

object ItemTranslator {
    private val simpleCoreToNetMapping: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()
    private val simpleNetToCoreMapping: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()
    private val complexCoreToNetMapping: MutableMap<Int, MutableMap<Int, Int>> = HashIntObjMaps.newMutableMap()
    private val complexNetToCoreMapping: MutableMap<Int, Pair<Int, Int>> = HashIntObjMaps.newMutableMap()

    init {
        val data = JsonBrowser().parse(this::class.java.getResourceAsStream("vanilla/r16_to_current_item_map.json"))
        val legacyStringToIntMap = JsonBrowser().parse(
            this::class.java.getResourceAsStream("vanilla/item_id_map.json")
        ).toMap<String, String>()

        val simpleMappings: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()
        data["simple"].toMap<String, String>().forEach { (oldId, newId) ->
            if (!legacyStringToIntMap.containsKey(newId)) {
                return@forEach
            }
            simpleMappings[newId] = legacyStringToIntMap.getValue(oldId).toInt()
        }
        legacyStringToIntMap.forEach { (stringId, intId) ->
            if (simpleMappings.containsKey(stringId)) {
                throw IllegalArgumentException("Old ID $stringId collides with new ID")
            }
            simpleMappings[stringId] = intId.toInt()
        }
        val complexMappings: MutableMap<String, Pair<Int, Int>> = HashObjObjMaps.newMutableMap()
        data["complex"].toMap<String, Map<String, String>>().forEach { (oldId, map) ->
            map.forEach { (meta, newId) ->
                complexMappings[newId] = Pair(legacyStringToIntMap.getValue(oldId).toInt(), meta.toInt())
            }
        }
        GlobalItemTypeDictionary.dictionary.getEntires().forEach { entry ->
            val stringId = entry.stringId
            val netId = entry.numericId

            when {
                complexMappings.containsKey(stringId) -> {
                    val (id, meta) = complexMappings.getValue(stringId)
                    complexCoreToNetMapping.getValue(id)[meta] = netId
                    complexNetToCoreMapping[netId] = Pair(id, meta)
                }
                simpleMappings.containsKey(stringId) -> {
                    simpleCoreToNetMapping[simpleMappings.getValue(stringId)] = netId
                    simpleNetToCoreMapping[netId] = simpleMappings.getValue(stringId)
                }
                stringId != "minecraft:unknown" -> {
                    throw IllegalArgumentException("Unmapped entry $stringId")
                }
            }
        }
    }

    fun toNetworkId(internalId: Int, internalMeta: Int): Pair<Int, Int> {
        if (complexCoreToNetMapping[internalId]?.containsKey(internalMeta) == true) {
            return Pair(complexCoreToNetMapping.getValue(internalId).getValue(internalMeta), 0)
        }
        if (simpleCoreToNetMapping.containsKey(internalId)) {
            return Pair(simpleCoreToNetMapping.getValue(internalId), internalMeta)
        }
        throw IllegalArgumentException("Unmapped ID/metadata combination $internalId:$internalMeta")
    }

    @JvmOverloads
    fun fromNetworkId(
        networkId: Int,
        networkMeta: Int,
        isComplexMapping: AtomicBoolean = AtomicBoolean(false)
    ): Pair<Int, Int> {
        if (complexNetToCoreMapping.containsKey(networkId)) {
            if (networkMeta != 0) {
                throw IllegalArgumentException("Unexpected non-zero network meta on complex item mapping")
            }
            isComplexMapping.set(true)
            return complexNetToCoreMapping.getValue(networkId)
        }
        isComplexMapping.set(false)
        if (simpleNetToCoreMapping.containsKey(networkId)) {
            return Pair(simpleNetToCoreMapping.getValue(networkId), networkMeta)
        }
        throw IllegalArgumentException("Unmapped network ID/metadata combination $networkId:$networkMeta")
    }

    fun fromNetworkIdWithWildcardHandling(networkId: Int, networkMeta: Int): Pair<Int, Int> {
        val isComplexMapping = AtomicBoolean(false)
        if (networkMeta != 0x7fff)
            return fromNetworkId(networkId, networkMeta)
        val (id, meta) = fromNetworkId(networkId, 0, isComplexMapping)
        return Pair(id, if (isComplexMapping.get()) meta else -1)
    }
}
