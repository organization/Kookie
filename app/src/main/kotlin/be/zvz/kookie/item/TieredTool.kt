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
package be.zvz.kookie.item

abstract class TieredTool(identifier: ItemIdentifier, name: String, val tier: ToolTier) : Tool(identifier, name) {

    override val maxDurability: Int = tier.maxDurability
    override val baseMiningEfficiency: Float = tier.baseEfficiency + 0f
    override val fuelTime: Int = if (tier == ToolTier.WOOD) 200 else 0
}
