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
package be.zvz.kookie.world

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.utils.Union
import be.zvz.kookie.utils.inline.repeat3
import be.zvz.kookie.world.utils.SubChunkExplorer
import com.koloboke.collect.map.hash.HashLongObjMaps
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

open class Explosion @JvmOverloads constructor(
    val world: World,
    val source: Vector3,
    val size: Float,
    val what: Union.U2<Entity, Block>? = null
) {
    val affectedBlocks: MutableMap<Long, Block> = HashLongObjMaps.newMutableMap()
    var stepLen: Double = 0.3
    private val subChunkExplorer = SubChunkExplorer(world)

    init {
        if (size <= 0) {
            throw IllegalArgumentException("Explosion radius must be greater than 0, got $size")
        }
    }

    @JvmOverloads constructor(center: Position, size: Float, what: Union.U2<Entity, Block>? = null) :
        this(
            center.world ?: throw IllegalArgumentException("Position does not have a valid world"),
            center.asVector3(),
            size,
            what
        )

    /**
     * Calculates which blocks will be destroyed by this explosion.
     * If explodeB() is called without calling this, no blocks will be destroyed.
     */
    fun explodeA(): Boolean {
        if (size <= 0) {
            return false
        }

        repeat3(RAYS) { x, y, z ->
            if (pointValid(x) && pointValid(y) && pointValid(z)) {
                val shift = Vector3(x, y, z).divide(M_RAYS / 2).subtract(1, 1, 1).normalize().multiply(stepLen)
                var pointer = source.asVector3()
                var blastForce: Double = size.toDouble() * Random.nextDouble(0.7, 1.3)
                while (blastForce > 0) {
                    val vBlockX = pointToInt(pointer.x)
                    val vBlockY = pointToInt(pointer.y)
                    val vBlockZ = pointToInt(pointer.z)
                    pointer -= shift

                    subChunkExplorer.moveTo(vBlockX, vBlockY, vBlockZ)
                    subChunkExplorer.currentSubChunk?.let {
                        val state = it.getFullBlock(vBlockX and 0x0f, vBlockY and 0x0f, vBlockZ and 0x0f)
                        val block = BlockFactory.fromFullBlock(state)
                        val blastResistance = BlockFactory.blastResistance[state] ?: 0F
                        if (blastResistance >= 0) {
                            blastForce -= (blastResistance / 5 + 0.3F) * stepLen
                            if (blastForce > 0 && !affectedBlocks.containsKey(World.blockHash(vBlockX, vBlockY, vBlockZ))) {
                                val targetBlock = block.clone().apply { position(world, vBlockX, vBlockY, vBlockZ) }
                                targetBlock.getAffectedBlocks().forEach { affectedBlock ->
                                    affectedBlocks[
                                        World.blockHash(
                                            affectedBlock.pos.floorX,
                                            affectedBlock.pos.floorY,
                                            affectedBlock.pos.floorZ
                                        )
                                    ] = affectedBlock
                                }
                            }
                        }
                        blastForce -= stepLen * 0.75
                    }
                }
            }
        }
        return true
    }

    /**
     * Executes the explosion's effects on the world.
     * This includes destroying blocks (if any), harming and knockback entities, and creating sounds and particles.
     */
    fun explodeB(): Boolean {
        val updateBlocks: MutableMap<Long, Boolean> = HashLongObjMaps.newMutableMap()
        var yieldValue = (1 / size) * 100
        if (what is Entity) {
            TODO("Implements after implemented EntityExplodeEvent")
            /**
             * val ev = EntityExplodeEvent(what, this.source, affectedBlocks, yieldValue)
             * ev.call()
             * if (ev.isCancelled()) {
             *     return false
             * } else {
             *     yieldValue = ev.getYield()
             *     this.affectedBlocks = ev.getBlockList()
             * }
             */
        }

        val explosionSize = size * 2
        val explosionBB = AxisAlignedBB(
            minX = floor(source.x - explosionSize - 1),
            maxX = ceil(source.x + explosionSize + 1),
            minY = floor(source.y - explosionSize - 1),
            maxY = ceil(source.y + explosionSize + 1),
            minZ = floor(source.z - explosionSize - 1),
            maxZ = ceil(source.z + explosionSize + 1)
        )

        /** @var Entity[] $list */
        val list: List<Entity> = listOf() // TODO: = world.getNearbyEntities(explosionBB, this.what.takeIf { it is Entity })

        list.forEach { entity ->
            val entityPos: Position = Position() // TODO: = entity.pos
            val distance = entityPos.distance(source) / explosionSize

            if (distance <= 1) {
                val motion = entityPos.subtract(source).normalize()
                val impact = 1 - distance
                val damage = (((impact * impact + impact) / 2) * 8 * explosionSize + 1).toInt()

                /** TODO: Implements after implemented Entity methods and EntityDamageEvents
                 * entity.attack(
                 *     when (what) {
                 *         is Entity -> EntityDamageByEntityEvent(what, entity, EntityDamageEvent.CAUSE_ENTITY_EXPLOSION, damage)
                 *         is Block -> EntityDamageByBlockEvent(what, entity, EntityDamageEvent.CAUSE_BLOCK_EXPLOSION, damage)
                 *         else -> EntityDamageEvent(entity, EntityDamageEvent.CAUSE_BLOCK_EXPLOSION, damage)
                 *     }
                 * )
                 *
                 * entity.setMotion(motion.multiply(impact))
                 */
            }
        }

        val air = ItemFactory.air()
        val airBlock = VanillaBlocks.AIR.block

        /* TODO: Implements after implemented World methods and TNT
         * affectedBlocks.forEachValue { block ->
         *     val blockPos: Position = block.pos
         *     if (block is TNT) {
         *         block.ignite(Random.nextInt(10, 30))
         *     } else {
         *         if (Random.nextInt(0, 100) < yieldValue) {
         *             block.getDrops(air).forEach { drop ->
         *                 world.dropItem(blockPos.add(0.5, 0.5, 0.5), drop)
         *             }
         *         }
         *         world.getTile(blockPos)?.onBlockDestroyed()
         *         world.setBlock(blockPos, airBlock)
         *         world.updateAllLight(blockPos)
         *     }
         * }
         */

        affectedBlocks.values.forEach { block ->
            val blockPos: Position = block.pos
            Facing.ALL.forEach { side ->
                val sideBlock = blockPos.getSide(side)
                val (sideX, sideY, sideZ) = listOf(sideBlock.floorX, sideBlock.floorY, sideBlock.floorZ)
                if (world.isInWorld(sideX, sideY, sideZ)) {
                    val index = World.blockHash(sideX, sideY, sideZ)
                    if (!affectedBlocks.containsKey(index) && !updateBlocks.containsKey(index)) {
                        /* TODO: Implements after implemented BlockUpdateEvent
                         * val ev = BlockUpdateEvent(world.getBlockAt(sideX, sideY, sideZ))
                         * ev.call()
                         * if (!ev.isCancelled()) {
                         *     world.getNearbyEntities(AxisAlignedBB.one().offset(sideX, sideY, sideZ).expand(1, 1, 1))
                         *         .forEach(Entity::onNearbyBlockChange)
                         * }
                         * ev.getBlock().onNearbyBlockChange()
                         */
                    }
                    updateBlocks[index] = true
                }
            }
        }

        this.source.floor().let {
            /* TODO("Implements after implemented World::addParticle() and World::addSound()")
             * world.addParticle(it, HugeExplodeSeedParticle());
             * world.addSound(it, ExplodeSound());
             */
        }

        return true
    }

    private fun pointValid(point: Int) = point == 0 || point == M_RAYS
    private fun pointToInt(point: Double) = (point.takeIf { it >= it.toInt() } ?: point - 1).toInt()

    companion object {
        const val RAYS = 16
        const val M_RAYS = RAYS - 1
    }
}
