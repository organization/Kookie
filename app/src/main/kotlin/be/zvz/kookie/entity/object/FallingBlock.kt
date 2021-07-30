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
package be.zvz.kookie.entity.`object`

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.EntitySizeInfo
import be.zvz.kookie.entity.Location
import be.zvz.kookie.event.entity.EntityBlockChangeEvent
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.ByteTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import be.zvz.kookie.network.mcpe.convert.RuntimeBlockMapping
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import kotlin.math.abs

open class FallingBlock @JvmOverloads constructor(
    location: Location,
    val block: Block,
    nbt: CompoundTag? = null
) : Entity(location, nbt) {
    override val gravity: Float = 0.04F
    override val drag: Float = 0.02F
    override val entityNetworkIdentifier: EntityIds = EntityIds.FALLING_BLOCK
    override val initialSizeInfo: EntitySizeInfo = EntitySizeInfo(0.98F, 0.98F)

    override fun canCollideWith(entity: Entity): Boolean {
        return false
    }

    override fun canBeMovedByCurrents(): Boolean {
        return false
    }

    override fun attack(source: Any) {
        /*
        TODO:
        if (source.cause == EntityDamageEvent.Type.VOID) {
           super.attack(source)
         */
    }

    override fun entityBaseTick(tickDiff: Long): Boolean {
        if (isClosed()) {
            return false
        }

        var hasUpdate = super.entityBaseTick(tickDiff)
        if (!isFlaggedForDespawn()) {
            val pos = location.add((-size.width / 2).toInt(), (size.height).toInt(), (-size.width / 2).toInt())

            block.position(world, pos.floorX, pos.floorY, pos.floorZ)
            var blockTarget: Block? = null
            /*
            TODO:
            if (block is Fallable) {
                blockTarget = block.tickFalling()
            }
             */
            if (onGround || blockTarget != null) {
                flagForDespawn()

                val block = world.getBlock(pos)
                if (!block.canBePlaced() || !world.isInWorld(pos.floorX, pos.floorY, pos.floorZ) || (onGround && abs(
                        location.y - location.floorY
                    ) > 0.001)
                ) {
                    // FIXME: anvils are supposed to destroy torches
                    world.dropItem(location, this.block.asItem())
                } else {
                    val ev = EntityBlockChangeEvent(this, block, blockTarget ?: this.block)
                    ev.call()
                    if (!ev.isCancelled) {
                        world.setBlock(pos, ev.to)
                    }
                }
                hasUpdate = true
            }
        }
        return hasUpdate
    }

    override fun saveNBT(): CompoundTag {
        return super.saveNBT().apply {
            setInt("TileID", block.getId())
            setByte("Data", block.getMeta())
        }
    }

    override fun syncNetworkData(properties: EntityMetadataCollection) {
        super.syncNetworkData(properties)
        properties.setInt(EntityMetadataProperties.VARIANT, RuntimeBlockMapping.toRuntimeId(block.getFullId().toInt()))
    }

    override fun getOffsetPosition(vector: Vector3): Vector3 {
        return vector.add(0.0, 0.49, 0.0)
    }

    companion object {
        @JvmStatic
        fun parseBlockNBT(nbt: CompoundTag): Block {
            var blockId = 0
            val tileIdTag = nbt.getTag("TileID")
            val tileTag = nbt.getTag("Tile")
            if (tileIdTag is IntTag) {
                blockId = tileIdTag.value
            } else if (tileTag is ByteTag) {
                blockId = tileTag.value
            }
            if (blockId == 0) {
                throw IllegalArgumentException("Missing block info from NBT")
            }
            val damage = nbt.getByte("Data", 0)
            return BlockFactory.get(blockId, damage)
        }
    }
}