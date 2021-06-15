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
package be.zvz.kookie.block

import be.zvz.kookie.block.tile.Spawnable
import be.zvz.kookie.block.tile.Tile
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.item.enchantment.VanillaEnchantments
import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.RayTraceResult
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.player.Player
import be.zvz.kookie.world.BlockTransaction
import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World

open class Block(val idInfo: BlockIdentifier, val name: String, val breakInfo: BlockBreakInfo) {
    var pos: Position

    protected var collisionBoxes: List<AxisAlignedBB>? = null
        get() = field ?: run {
            val offset = getPosOffset()?.let(pos::add) ?: pos
            recalculateCollisionBoxes().let { boxes ->
                boxes.forEach { it.offset(offset.x, offset.y, offset.z) }
                field = boxes
            }
            field
        }

    init {
        // TODO: Variant collides with state bitmask
        if ((idInfo.variant and getStateBitmask()) != 0) {
            throw IllegalArgumentException(
                "Variant 0x" +
                    idInfo.variant.toString(16) +
                    " collides with state bitmask 0x" +
                    getStateBitmask().toString(16)
            )
        }
        pos = Position()
    }

    fun clone(): Block = Block(idInfo, name, breakInfo).also { it.pos = pos.asPosition() }

    fun getId(): Int = idInfo.blockId

    fun getFullId(): Int = (getId() shl 4) or getMeta()

    fun asItem(): Item = ItemFactory.get(
        idInfo.itemId!!,
        idInfo.variant or (writeStateToMeta() and getNonPersistentStateBitmask().inv())
    )

    fun getMeta(): Int {
        val stateMeta = writeStateToMeta()
        assert((stateMeta and getStateBitmask().inv()) == 0)
        return idInfo.variant or stateMeta
    }

    open fun getStateBitmask(): Int = 0

    open fun getNonPersistentStateBitmask(): Int = getStateBitmask()

    protected open fun writeStateToMeta(): Int = 0

    open fun readStateFromData(id: Int, stateMeta: Int) {
        // NOOP
    }

    open fun readStateFromWorld() {
        collisionBoxes = null
    }

    open fun writeStateToWorld() {
        pos.world?.getOrLoadChunkAtPosition(pos)
            ?.setFullBlock(
                pos.x.toInt() and 0xf,
                pos.y.toInt(),
                pos.z.toInt() and 0xf,
                getFullId().toLong()
            )

        val tileType: Class<*>? = try {
            Class.forName(idInfo.tileClass)
        } catch (_: ClassNotFoundException) {
            null
        }
        pos.world?.getTile(pos)?.let { oldTile ->
            if (tileType?.takeIf { it.isAssignableFrom(oldTile::class.java) } === null) {
                oldTile.close()
                null
            } else {
                if (oldTile is Spawnable) {
                    oldTile.dirty = true // destroy old network cache
                }
                oldTile
            }
        } ?: tileType?.let {
            pos.world?.addTile(
                it.getConstructor(World::class.java, Vector3::class.java)
                    .newInstance(pos.world, pos.asVector3())
                    as Tile
            )
        }
    }

    fun isSameType(other: Block): Boolean =
        idInfo.blockId == other.idInfo.blockId && idInfo.variant == other.idInfo.variant

    fun isSameState(other: Block): Boolean = isSameType(other) && writeStateToMeta() == other.writeStateToMeta()

    open fun canBePlaced(): Boolean = true

    open fun canBeReplaced(): Boolean = true

    open fun canBePlacedAt(
        blockReplace: Block,
        clickVector: Vector3,
        face: Int,
        isClickedBlock: Boolean
    ): Boolean = blockReplace.canBeReplaced()

    @JvmOverloads
    open fun place(
        tx: BlockTransaction,
        item: Item,
        blockReplace: Block,
        blockClicked: Block,
        face: Int,
        clickVector: Vector3,
        player: Player? = null
    ): Boolean {
        tx.addBlock(blockReplace.pos, this)
        return true
    }

    open fun onPostPlace() {
    }

    @JvmOverloads
    open fun onBreak(item: Item, player: Player? = null): Boolean {
        pos.world?.getTile(pos)?.onBlockDestroyed()
        pos.world?.setBlock(pos, VanillaBlocks.AIR.block)
        return true
    }

    open fun onNearbyBlockChange() {
    }

    open fun ticksRandomly(): Boolean = false

    /**
     * Called when this block is randomly updated due to chunk ticking.
     * WARNING: This will not be called if ticksRandomly() does not return true!
     */
    open fun onRandomTick() {
    }

    open fun onScheduledUpdate() {
    }

    @JvmOverloads
    open fun onInteract(item: Item, face: Int, clickVector: Vector3, player: Player? = null): Boolean = false

    @JvmOverloads
    open fun onAttack(item: Item, face: Int, player: Player? = null): Boolean = false

    open fun getFrictionFactor(): Float = 0.6f

    open fun getLightLevel(): Int = 0

    /**
     * Returns the amount of light this block will filter out when light passes through this block.
     * This value is used in light spread calculation.
     *
     * @return Int 0-15
     */
    open fun getLightFilter(): Int = if (isTransparent()) 0 else 15

    /**
     * Returns whether this block blocks direct sky light from passing through it. This is independent from the light
     * filter value, which is used during propagation.
     *
     * In most cases, this is the same as isTransparent() however, some special cases exist such as leaves and cobwebs,
     * which don't have any additional effect on light propagation, but don't allow direct sky light to pass through.
     */
    open fun blocksDirectSkyLight(): Boolean = getLightFilter() > 0

    open fun isTransparent(): Boolean = false

    open fun isSolid(): Boolean = true

    /**
     * AKA: Block.isFlowable
     */
    open fun canBeFlowedInto(): Boolean = false

    open fun hasEntityCollision(): Boolean = false

    /**
     * Returns whether entities can climb up this block.
     */
    open fun canClimb(): Boolean = false

    open fun addVelocityToEntity(entity: Entity): Vector3? = null

    fun position(world: World, x: Int, y: Int, z: Int) {
        pos = Position(x, y, z, world)
    }

    open fun getDrops(item: Item): List<Item> =
        if (breakInfo.isToolCompatible(item))
            if (isAffectedBySilkTouch() && item.hasEnchantment(VanillaEnchantments.SILK_TOUCH.enchantment))
                getSilkTouchDrops(item)
            else getDropsForCompatibleTool(item)
        else listOf()

    open fun getDropsForCompatibleTool(item: Item): List<Item> = listOf(asItem())

    open fun getSilkTouchDrops(item: Item): List<Item> = listOf(asItem())

    fun getXpDropForTool(item: Item): Int = when {
        item.hasEnchantment(VanillaEnchantments.SILK_TOUCH.enchantment) || breakInfo.isToolCompatible(item) -> 0
        else -> getXpDropForTool(item)
    }

    open fun getXpDropAmount(): Int = 0

    open fun isAffectedBySilkTouch(): Boolean = false

    @JvmOverloads
    open fun getPickedItem(addUserData: Boolean = false): Item {
        val item = asItem()
        if (addUserData) {
            pos.world?.getTile(pos)?.let { tile ->
                tile.getCleanedNBT()?.let { nbt ->
                    item.setCustomBlockData(nbt)
                    item.lore = mutableListOf("+(DATA)")
                }
            }
        }
        return item
    }

    open fun getFuelTime(): Int = 0

    open fun getFlameEncouragement(): Int = 0

    open fun getFlammability(): Int = 0

    open fun burnsForever(): Boolean = false

    fun isFlammable(): Boolean = getFlammability() > 0

    open fun onIncinerate() {
    }

    @JvmOverloads
    fun getSide(side: Facing, step: Int = 1): Block = pos.takeIf { it.isValid() }?.world?.getBlock(pos.getSide(side, step))
        ?: throw IllegalStateException("Block does not have a valid world")

    fun getHorizontalSides() = sequence {
        pos.sidesAroundAxis(Axis.Y).forEach {
            yield(pos.world!!.getBlock(it.second))
        }
    }

    /**
     * Returns the six blocks around this block.
     *
     * @return Block[]|\Generator
     * @phpstan-return \Generator<Int, Block, void, void>
     */
    fun getAllSides() = sequence {
        pos.sides().forEach {
            pos.world?.let { world ->
                yield(world.getBlock(it))
            }
        }
    }

    /**
     * Returns a list of blocks that this block is part of. In most cases, only contains the block itself, but in cases
     * such as double plants, beds and doors, will contain both halves.
     *
     * @return Block[]
     */
    open fun getAffectedBlocks(): List<Block> = listOf(this)

    override fun toString(): String = "Block[$name] (${getId()}:${getMeta()})"

    /** Checks for collision against an AxisAlignedBB */
    fun collidesWithBB(bb: AxisAlignedBB): Boolean = collisionBoxes?.find { bb.intersectsWith(it) } !== null

    /**
     * Called when an entity's bounding box clips inside this block's cell. Note that the entity may not be intersecting
     * with the collision box or bounding box.
     *
     * @return Boolean Whether the block is still the same after the intersection. If it changed (e.g. due to an explosive
     * being ignited), this should return false.
     */
    open fun onEntityInside(entity: Entity): Boolean = true

    /**
     * Returns an additional fractional vector to shift the block's effective position by based on the current position.
     * Used to randomize position of things like bamboo canes and tall grass.
     */
    open fun getPosOffset(): Vector3? = null

    /**
     * @return AxisAlignedBB[]
     */
    protected open fun recalculateCollisionBoxes(): List<AxisAlignedBB> = listOf(AxisAlignedBB.one())

    fun isFullCube(): Boolean =
        collisionBoxes?.takeIf { it.size == 1 && it[0].getAverageEdgeLength() >= 1 && it[0].isCube() } !== null

    fun calculateIntercept(pos1: Vector3, pos2: Vector3): RayTraceResult? {
        var currentHit: RayTraceResult? = null
        var currentDistance = Double.MAX_VALUE

        collisionBoxes
            ?.takeIf { it.isNotEmpty() }
            ?.forEach {
                val nextHit = it.calculateIntercept(pos1, pos2) ?: return@forEach
                val nextDistance = nextHit.hitVector.distanceSquared(pos1)
                if (nextDistance < currentDistance) {
                    currentHit = nextHit
                    currentDistance = nextDistance
                }
            }
        return currentHit
    }
}
