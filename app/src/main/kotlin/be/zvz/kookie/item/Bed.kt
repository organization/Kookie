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
import be.zvz.kookie.block.utils.DyeColor
import be.zvz.kookie.math.Facing

class Bed(identifier: ItemIdentifier, vanillaName: String = "Unknown", val color: DyeColor) : Item(identifier, vanillaName) {
    override val maxStackSize: Int = 1
    override fun getBlock(clickedFace: Facing?): Block = VanillaBlocks.BED.block
}
