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
package be.zvz.kookie.event.inventory

import be.zvz.kookie.block.Block
import be.zvz.kookie.event.block.BlockEvent
import be.zvz.kookie.item.Item

// TODO: Furnace should be tile
class FurnaceBurnEvent(furnace: Block, val fuel: Item, burnTime: Int) : BlockEvent(furnace) {
    // TODO
}
