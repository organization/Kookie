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

enum class ToolTier(val harvestLevel: Int, val maxDurability: Int, val baseAttackPoints: Int, val baseEfficiency: Int) {
    WOOD(1, 60, 5, 2),
    GOLD(2, 33, 5, 12),
    STONE(3, 132, 6, 4),
    IRON(4, 251, 7, 6),
    DIAMOND(5, 1562, 8, 8),
    NETHERITE(6, 2032, 9, 9);
}
