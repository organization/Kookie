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

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockToolType
import be.zvz.kookie.entity.Entity

class Pickaxe(identifier: ItemIdentifier, name: String, tier: ToolTier) : TieredTool(identifier, name, tier) {

    override val blockToolType: BlockToolType = BlockToolType.PICKAXE
    override val blockToolHarvestLevel: Int = tier.harvestLevel
    override val attackPoint: Int = tier.baseAttackPoints - 2

    override fun onDestroyBlock(block: Block): Boolean = if (!block.breakInfo.breaksInstantly()) applyDamage(1) else false

    override fun onAttackEntity(victim: Entity): Boolean = applyDamage(2)
}
