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
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player

class FlintSteel(identifier: ItemIdentifier, name: String) : Tool(identifier, name) {
    override val maxDurability: Int = 65

    override fun onInteractBlock(
        player: Player,
        blockReplace: Block,
        blockClicked: Block,
        face: Facing,
        clickVector: Vector3
    ): ItemUseResult {
        if (blockReplace.idInfo.blockId == VanillaBlocks.AIR.id) {
            val world = player.world
            world.setBlock(blockReplace.pos, VanillaBlocks.AIR.block)
            // TODO: addSound

            applyDamage(1)

            return ItemUseResult.SUCCESS
        }

        return ItemUseResult.NONE
    }
}
