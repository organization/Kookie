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
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.Facing

class ItemBlockWallOrFloor(identifier: ItemIdentifier, name: String, val floorVariant: Block, val wallVariant: Block) :
    Item(identifier, name) {
    override fun getBlock(clickedFace: Int?): Block {
        if (clickedFace != null && Facing.axis(clickedFace) != Axis.Y.value) {
            return BlockFactory.fromFullBlock(wallVariant.getFullId())
        }
        return BlockFactory.fromFullBlock(floorVariant.getFullId())
    }
}
