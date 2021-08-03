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
package be.zvz.kookie.world.generator.objects

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.block.utils.TreeType
import be.zvz.kookie.world.BlockTransaction
import be.zvz.kookie.world.ChunkManager
import kotlin.math.abs
import kotlin.random.Random

open class Tree @JvmOverloads constructor(
    protected val trunkBlock: Block,
    protected val leafBlock: Block,
    protected var treeHeight: Int = 7
) {
    open fun canPlaceObject(world: ChunkManager, x: Int, y: Int, z: Int, random: Random): Boolean {
        var radiusToCheck = 0
        repeat(treeHeight + 3) { yy ->
            if (yy == 1 || yy == treeHeight) {
                ++radiusToCheck
            }
            for (xx in -radiusToCheck until radiusToCheck + 1) {
                for (zz in -radiusToCheck until radiusToCheck + 1) {
                    if (!canOverride(world.getBlockAt(x + xx, y + yy, z + zz))) {
                        return false
                    }
                }
            }
        }

        return true
    }

    open fun placeObject(world: ChunkManager, x: Int, y: Int, z: Int, random: Random) {
        val transaction = BlockTransaction(world)
        placeTrunk(x, y, z, random, generateChunkHeight(random), transaction)
        placeCanopy(x, y, z, random, transaction)

        transaction.apply() // TODO: handle return value on failure
    }

    protected open fun generateChunkHeight(random: Random): Int {
        return treeHeight - 1
    }

    protected open fun placeTrunk(x: Int, y: Int, z: Int, random: Random, trunkHeight: Int, transaction: BlockTransaction) {
        // The base dirt block
        transaction.addBlockAt(x, y - 1, z, VanillaBlocks.DIRT.block)

        repeat(trunkHeight) { yy ->
            if (canOverride(transaction.fetchBlockAt(x, y + yy, z))) {
                transaction.addBlockAt(x, y + yy, z, trunkBlock)
            }
        }
    }

    protected open fun placeCanopy(x: Int, y: Int, z: Int, random: Random, transaction: BlockTransaction) {
        for (yy in y - 3 + treeHeight..y + treeHeight) {
            val yOff = yy - (y + treeHeight)
            val mid = 1 - yOff / 2
            for (xx in x - mid..x + mid) {
                val xOff = abs(xx - x)
                for (zz in z - mid..z + mid) {
                    val zOff = abs(zz - z)
                    if (xOff == mid && zOff == mid && (yOff == 0 || random.nextInt(2) == 0)) {
                        continue
                    }
                    if (!transaction.fetchBlockAt(xx, yy, zz).isSolid()) {
                        transaction.addBlockAt(xx, yy, zz, leafBlock)
                    }
                }
            }
        }
    }

    protected open fun canOverride(block: Block): Boolean =
        block.canBeReplaced() /* TODO: || block is Sapling || block is Leaves */

    companion object {
        fun growTree(world: ChunkManager, x: Int, y: Int, z: Int, random: Random, type: TreeType = TreeType.OAK) {
            when (type) {
                TreeType.OAK -> OakTree()
                TreeType.SPRUCE -> SpruceTree()
                TreeType.BIRCH -> BirchTree(random.nextInt(39) == 0)
                TreeType.JUNGLE -> JungleTree()
                else -> null
            }?.let { tree ->
                if (tree.canPlaceObject(world, x, y, z, random)) {
                    tree.placeObject(world, x, y, z, random)
                }
            }
        }
    }
}
