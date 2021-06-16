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
package be.zvz.kookie.block

import com.koloboke.collect.map.hash.HashLongFloatMaps
import com.koloboke.collect.map.hash.HashLongIntMaps
import com.koloboke.collect.map.hash.HashLongObjMaps
import be.zvz.kookie.block.BlockIdentifier as BID
import be.zvz.kookie.block.BlockLegacyIds as Ids

object BlockFactory {
    private val fullList: MutableMap<Long, Block> = HashLongObjMaps.newMutableMap()
    var light: MutableMap<Long, Int> = HashLongIntMaps.newMutableMap()
    var lightFilter: MutableMap<Long, Int> = HashLongIntMaps.newMutableMap()
    var blocksDirectSkyLight: MutableMap<Long, Boolean> = HashLongObjMaps.newMutableMap()
    var blastResistance: MutableMap<Long, Float> = HashLongFloatMaps.newMutableMap()

    init {
        /*val railBreakInfo = BlockBreakInfo(0.7f)
        register(ActivatorRail(BID(Ids.ACTIVATOR_RAIL.id, 0), "Activator Rail", railBreakInfo))*/
        register(Air(BID(Ids.AIR.id, 0), "Air", BlockBreakInfo.indestructible(-1f)))
        TODO("Add Blocks")
    }

    @JvmStatic
    @JvmOverloads
    fun register(block: Block, override: Boolean = false) {
        val variant = block.idInfo.variant

        val stateMask = block.getStateBitmask()
        if ((variant and stateMask) != 0) {
            throw IllegalArgumentException("Block variant collides with state bitmask")
        }

        block.idInfo.getAllBlockIds().forEach { id ->
            if (!override && isRegistered(id, variant)) {
                throw IllegalArgumentException("Block registration $id:$variant conflicts with an existing block")
            }

            for (meta in variant until (variant or stateMask)) {
                if (meta and stateMask.inv() != variant) {
                    continue
                }
                if (!override) assertRegisterValid(block, id, meta)

                if (!override && isRegistered(id, meta)) {
                    throw IllegalArgumentException(
                        "Block registration ${block::class} has states which conflict with other blocks"
                    )
                }

                val fullId = Block.fullId(id, meta)
                val v = block.clone().apply { readStateFromData(id, meta) }
                if (v.getFullId() != fullId) {
                    // if the fullID comes back different, this is a broken state that we can't rely on; map it to default
                    fillStaticArrays(fullId, block)
                } else {
                    fillStaticArrays(fullId, v)
                }
            }
        }
    }

    private fun assertRegisterValid(block: Block, id: Int, variant: Int) {
        if (isRegistered(id, variant)) {
            throw IllegalArgumentException(
                "Block registration ${block::class} has states which conflict with other blocks"
            )
        }
    }

    @JvmStatic
    fun remap(id: Int, meta: Int, block: Block) {
        if (isRegistered(id, meta)) {
            val fullId = Block.fullId(id, meta)
            val existing = fullList[fullId]
            if (existing !== null && existing.getFullId() == fullId) {
                throw IllegalArgumentException("$id:$meta is already mapped")
            } else {
                // if it's not a match, this was already remapped for some reason remapping overwrites are OK
            }
        }
        fillStaticArrays(Block.fullId(id, meta), block)
    }

    private fun fillStaticArrays(fullId: Long, block: Block) {
        fullList[fullId] = block
        light[fullId] = block.getLightLevel()
        lightFilter[fullId] = 15.coerceAtMost(block.getLightFilter() + 1) // opacity plus 1 standard light filter
        blocksDirectSkyLight[fullId] = block.blocksDirectSkyLight()
        blastResistance[fullId] = block.breakInfo.blastResistance
    }

    /**
     * @deprecated This method should ONLY be used for deserializing data, e.g. from a config or database. For all other
     * purposes, use VanillaBlocks.
     * @see VanillaBlocks
     *
     * Deserializes a block from the provided legacy ID and legacy meta.
     */
    @JvmStatic
    @JvmOverloads
    fun get(id: Int, meta: Int = 0): Block {
        if (meta !in 0..0xf) {
            throw IllegalArgumentException("Block meta value meta is out of bounds")
        }

        var block: Block? = null
        val fullId = Block.fullId(id, meta)
        fullList[fullId]?.let {
            block = it.clone()
        }
        return block ?: UnknownBlock(BID(id, meta), BlockBreakInfo.instant())
    }

    fun fromFullBlock(fullState: Long): Block = get((fullState shr 4).toInt(), (fullState and 0xf).toInt())

    @JvmStatic
    @JvmOverloads
    fun isRegistered(id: Int, meta: Int = 0): Boolean = fullList[id shl 4 or meta]?.takeIf { it !is UnknownBlock } !== null

    @Deprecated("In PMMP, it is used to fetch only blocks without null ")
    fun getAllKnownStates() = fullList.toList()
}
