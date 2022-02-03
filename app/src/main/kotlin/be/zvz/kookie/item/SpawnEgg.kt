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
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player
import be.zvz.kookie.world.World
import kotlin.random.Random

abstract class SpawnEgg(identifier: ItemIdentifier, vanillaName: String = "Unknown") : Item(identifier, vanillaName) {
    protected abstract fun createEntity(world: World, pos: Vector3, yaw: Float, pitch: Float): Entity

    override fun onInteractBlock(
        player: Player,
        blockReplace: Block,
        blockClicked: Block,
        face: Facing,
        clickVector: Vector3
    ): ItemUseResult {
        val entity = createEntity(player.world, blockClicked.pos.add(0.5, 0.0, 0.5), Random.nextFloat() * 360, 0F)

        if (customName.isNotEmpty()) entity.nameTag = customName
        pop()
        entity.spawnToAll()

        return ItemUseResult.SUCCESS
    }
}
