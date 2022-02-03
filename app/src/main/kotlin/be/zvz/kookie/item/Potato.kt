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

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.math.Facing

class Potato(identifier: ItemIdentifier, name: String) : Food(identifier, name) {
    override val foodRestore: Int = 1
    override val saturationRestore: Float = 0.6F

    override fun getBlock(clickedFace: Facing?): Block = VanillaBlocks.POTATOES.block
}
