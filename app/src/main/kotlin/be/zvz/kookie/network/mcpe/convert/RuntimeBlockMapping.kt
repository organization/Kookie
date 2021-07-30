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

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.data.bedrock.LegacyIdToStringIdMap
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.serializer.NetworkNbtSerializer
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import com.koloboke.collect.map.hash.HashIntIntMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.util.concurrent.atomic.AtomicInteger

object RuntimeBlockMapping {

    private val legacyToRuntimeMap: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()
    private val runtimeToLegacyMap: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()
    private val bedrockKnownStates: MutableList<CompoundTag> = mutableListOf()

    init {
        // FIXME: is this correct?
        val canonicalBlockStatesReader = this::class.java.getResourceAsStream("/vanilla/canonical_block_states.nbt")
            ?: throw AssertionError("Missing required resource file")
        val canonicalBlockStatesBuffer = canonicalBlockStatesReader.bufferedReader().toString()

        val stream = PacketSerializer(canonicalBlockStatesBuffer, AtomicInteger(0))
        while (!stream.feof()) {
            bedrockKnownStates.add(stream.getNbtCompoundRoot())
        }

        setupLegacyMappings()
    }

    private fun setupLegacyMappings() {

        val legacyIdMap = LegacyIdToStringIdMap.BLOCK
        val legacyStateMap: MutableList<R12ToCurrentBlockMapEntry> = mutableListOf()
        // FIXME: is this correct?
        val r12CurrentBlockMapReader = this::class.java.getResourceAsStream("/vanilla/r12_to_current_block_map.bin")
            ?: throw AssertionError("Missing required resource file")
        val legacyStateMapReader = PacketSerializer(r12CurrentBlockMapReader.bufferedReader().toString())
        val nbtReader = NetworkNbtSerializer()
        while (!legacyStateMapReader.feof()) {
            val id = legacyStateMapReader.getString()
            val meta = legacyStateMapReader.getLShort()

            val offset = legacyStateMapReader.offset
            val state = nbtReader.read(legacyStateMapReader.buffer.toString(), offset).mustGetCompoundTag()
            legacyStateMapReader.offset.set(offset.get())
            legacyStateMap.add(R12ToCurrentBlockMapEntry(id, meta, state))
        }
        val idToStateMap: MutableMap<String, MutableList<Int>> = HashObjObjMaps.newMutableMap()
        bedrockKnownStates.forEachIndexed { k, state ->
            // FIXME: Why getOrPut() doesn't work on here?
            if (idToStateMap[state.getString("name")] == null) {
                idToStateMap[state.getString("name")] = mutableListOf()
            }
            idToStateMap[state.getString("name")]!!.add(k)
        }
        legacyStateMap.forEach { pair ->
            val id = legacyIdMap.stringToLegacy[pair.id] ?: throw RuntimeException("No legacy ID matches ${pair.id}")
            val data = pair.meta
            if (data > 15) {
                return@forEach
            }
            val mappedState = pair.blockState
            val mappedName = mappedState.getString("name")
            if (idToStateMap[mappedName] == null) {
                throw RuntimeException("Mapped new state does not appear in network table")
            }
            idToStateMap[mappedName]!!.forEach forEach2@{ k ->
                val networkState = bedrockKnownStates[k]
                if (mappedState == networkState) {
                    registerMapping(k, id, data)
                    return@forEach
                }
            }
            throw RuntimeException("Mapped new state does not appear in network table")
        }
    }

    private fun registerMapping(staticRuntimeId: Int, legacyId: Int, legacyMeta: Int) {
        legacyToRuntimeMap[(legacyId shl 4) or legacyMeta] = staticRuntimeId
        runtimeToLegacyMap[staticRuntimeId] = (legacyId shl 4) or legacyMeta
    }

    fun toRuntimeId(internalStateId: Int): Int {
        return legacyToRuntimeMap[internalStateId]
            ?: legacyToRuntimeMap[VanillaBlocks.INFO_UPDATE.id shl 4]!! // INFO_UPDATE should never fail
    }

    fun fromRuntimeId(runtimeId: Int): Int {
        return runtimeToLegacyMap[runtimeId] ?: -1
    }
}