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

import be.zvz.kookie.item.Item

class BlockBreakInfo(
    val hardness: Float,
    val toolType: BlockToolType,
    val toolHarvestLevel: Int = 0,
    val blastResistance: Float = hardness * 5,
) {
    fun isBreakable(): Boolean = hardness >= 0

    fun breaksInstantly(): Boolean = hardness == 0f

    fun isToolCompatible(tool: Item): Boolean = when {
        hardness < 0 -> false
        else ->
            toolType == BlockToolType.NONE || toolHarvestLevel == 0 ||
                (
                    toolType.state and tool.blockToolType.state != BlockToolType.NONE.state &&
                        tool.blockToolHarvestLevel >= toolHarvestLevel
                    )
    }

    fun getBreakTime(item: Item): Float {
        var base = if (isToolCompatible(item)) {
            hardness * 1.5f
        } else {
            hardness * 5f
        }

        val efficiency = item.getMiningEfficiency(toolType.state and item.blockToolType.state != 0)
        if (efficiency <= 0) {
            throw IllegalArgumentException("${item::class.simpleName} has invalid mining efficiency: expected >= 0, got $efficiency")
        }

        base /= efficiency

        return base
    }

    companion object {
        fun instant(toolType: BlockToolType = BlockToolType.NONE, toolHarvestLevel: Int = 0): BlockBreakInfo {
            return BlockBreakInfo(0f, toolType, toolHarvestLevel, 0f)
        }
    }
}
