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
package be.zvz.kookie.entity.projectile

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.Location
import be.zvz.kookie.event.entity.EntityCombustByEntityEvent
import be.zvz.kookie.event.entity.EntityDamageByChildEntityEvent
import be.zvz.kookie.event.entity.EntityDamageByEntityEvent
import be.zvz.kookie.event.entity.EntityDamageEvent
import be.zvz.kookie.event.entity.ProjectileHitEvent
import be.zvz.kookie.math.RayTraceResult
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.math.VoxelRayTrace
import be.zvz.kookie.nbt.tag.ByteTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import kotlin.math.ceil

abstract class Projectile @JvmOverloads constructor(
    location: Location,
    shootingEntity: Entity?,
    nbt: CompoundTag? = null
) : Entity(location, nbt) {

    open var damage: Float = 0F

    open var blockHit: Block? = null

    init {
        shootingEntity?.let {
            owningEntity = it
        }
    }

    open override fun attack(source: Any) {
        /*
        TODO:
        if (source.cause == EntityDamageEvent.CAUSE_VOID) {
        super.attack(source)
         */
    }

    open override fun initEntity(nbt: CompoundTag) {
        super.initEntity(nbt)

        maxHealth = 1
        setHealth(1F)
        damage = nbt.getDouble("damage", 0.0).toFloat()
        do {
            var blockPos: Vector3? = null
            var blockId: Int? = null
            var blockData: Int? = null

            val tileXTag = nbt.getTag("tileX")
            val tileYTag = nbt.getTag("tileY")
            val tileZTag = nbt.getTag("tileZ")
            if (tileXTag is IntTag && tileYTag is IntTag && tileZTag is IntTag) {
                blockPos = Vector3(tileXTag.value, tileYTag.value, tileZTag.value)
            } else {
                break
            }
            val blockIdTag = nbt.getTag("blockId")
            if (blockIdTag is IntTag) {
                blockId = blockIdTag.value
            } else {
                break
            }
            val blockDataTag = nbt.getTag("blockData")
            if (blockDataTag is ByteTag) {
                blockData = blockDataTag.value
            } else {
                break
            }
            blockHit = BlockFactory.get(blockId, blockData)
            blockHit!!.position(world, blockPos.x.toInt(), blockPos.y.toInt(), blockPos.z.toInt())
        } while (false)
    }

    open override fun canCollideWith(entity: Entity): Boolean {
        return entity is Living && !onGround
    }

    open override fun canBeCollidedWith(): Boolean {
        return false
    }

    open fun getBaseDamage(): Float = damage

    open fun setBaseDamage(damage: Float) {
        this.damage = damage
    }

    open fun getResultDamage(): Int {
        return ceil(motion.length() * damage).toInt()
    }

    open override fun saveNBT(): CompoundTag {
        return super.saveNBT().apply {
            setDouble("damage", damage.toDouble())
            blockHit?.let {
                val pos = it.pos
                setInt("tileX", pos.x.toInt())
                setInt("tileY", pos.y.toInt())
                setInt("tileZ", pos.z.toInt())

                setInt("blockId", it.getId())
                setByte("blockData", it.getMeta())
            }
        }
    }

    override fun applyDragBeforeGravity(): Boolean {
        return true
    }

    override fun onNearByBlockChange() {
        if (
            blockHit != null && // &&
            // TODO: world.isInLoadedTerrian(blockHit!!.pos)
            !blockHit!!.isSameState(world.getBlock(blockHit!!.pos))
        ) {
            blockHit = null
        }
        super.onNearByBlockChange()
    }

    override fun hasMovementUpdate(): Boolean {
        return blockHit != null && super.hasMovementUpdate()
    }

    override suspend fun move(dx: Double, dy: Double, dz: Double) {
        blocksAround = null

        // TODO: Timings.entityMove.startTiming()

        val start = location.asVector3()
        var end = start.add(motion)

        var blockHit: Block? = null
        var entityHit: Entity? = null
        var hitResult: Any? = null

        VoxelRayTrace.betweenPoints(start, end).forEach {
            val block = world.getBlock(it)

            /*
            TODO:
            val blockHitResult = calculateInterceptWithBlock(block, start, end)
            if(blockHitResult != null){
                end = blockHitResult!!.hitVector
                blockHit = block
                hitResult = blockHitResult
                return@forEach
            }
            
             */
        }

        var entityDistance = Double.MAX_VALUE

        var newDiff = end.subtract(start)

        // TODO: do this when world get merged
        /*
        world.getEntityCollidingEntities(boundingBox.addCoord(newDiff.x, newDiff.y, newDiff.z).expand(1, 1, 1)).forEach {
            owningEntity?.let { ownE ->
                if (ownE.getId() == it.id && ticksLived < 5) {
                    return@forEach
                }
            }
            val entityBB = it.boundingBox.expandCopy(0.3, 0.3, 0.3)
        }
         */
        /*
		foreach($this->getWorld()->getCollidingEntities($this->boundingBox->addCoord($newDiff->x, $newDiff->y, $newDiff->z)->expand(1, 1, 1), $this) as $entity){
			if($entity->getId() === $this->getOwningEntityId() and $this->ticksLived < 5){
				continue;
			}

			$entityBB = $entity->boundingBox->expandedCopy(0.3, 0.3, 0.3);
			$entityHitResult = $entityBB->calculateIntercept($start, $end);

			if($entityHitResult === null){
				continue;
			}

			$distance = $this->location->distanceSquared($entityHitResult->hitVector);

			if($distance < $entityDistance){
				$entityDistance = $distance;
				$entityHit = $entity;
				$hitResult = $entityHitResult;
				$end = $entityHitResult->hitVector;
			}
		}

		$this->location = Location::fromObject(
			$end,
			$this->location->world,
			$this->location->yaw,
			$this->location->pitch
		);
		$this->recalculateBoundingBox();

		if($hitResult !== null){
			/** @var ProjectileHitEvent|null $ev */
			$ev = null;
			if($entityHit !== null){
				$ev = new ProjectileHitEntityEvent($this, $hitResult, $entityHit);
			}elseif($blockHit !== null){
				$ev = new ProjectileHitBlockEvent($this, $hitResult, $blockHit);
			}else{
				assert(false, "unknown hit type");
			}

			if($ev !== null){
				$ev->call();
				$this->onHit($ev);

				if($ev instanceof ProjectileHitEntityEvent){
					$this->onHitEntity($ev->getEntityHit(), $ev->getRayTraceResult());
				}elseif($ev instanceof ProjectileHitBlockEvent){
					$this->onHitBlock($ev->getBlockHit(), $ev->getRayTraceResult());
				}
			}

			$this->isCollided = $this->onGround = true;
			$this->motion = new Vector3(0, 0, 0);
		}else{
			$this->isCollided = $this->onGround = false;
			$this->blockHit = null;

			//recompute angles...
			$f = sqrt(($this->motion->x ** 2) + ($this->motion->z ** 2));
			$this->location->yaw = (atan2($this->motion->x, $this->motion->z) * 180 / M_PI);
			$this->location->pitch = (atan2($this->motion->y, $f) * 180 / M_PI);
		}

		$this->getWorld()->onEntityMoved($this);
		$this->checkBlockCollision();

		Timings::$entityMove->stopTiming();
         */
    }

    protected open fun calculateInterceptWithBlock(block: Block, start: Vector3, end: Vector3): RayTraceResult? =
        block.calculateIntercept(start, end)

    protected open fun onHit(event: ProjectileHitEvent) {
    }

    protected open fun onHitEntity(entityHit: Entity, hitResult: RayTraceResult) {
        val damage = getResultDamage()
        if (damage >= 0) {

            val ev = if (owningEntity != null) {
                EntityDamageByEntityEvent(this, entityHit, EntityDamageEvent.Type.PROJECTILE, damage.toFloat())
            } else {
                EntityDamageByChildEntityEvent(
                    owningEntity!!,
                    entityHit,
                    this,
                    EntityDamageEvent.Type.PROJECTILE,
                    damage.toFloat()
                )
            }
            entityHit.attack(ev)

            if (isOnFire()) {
                val ev = EntityCombustByEntityEvent(this, entityHit, 5)
                ev.call()
                if (!ev.isCancelled) {
                    entityHit.setOnFire(ev.duration.toLong())
                }
            }
        }
        flagForDespawn()
    }

    protected open fun onHitBlock(blockHit: Block, hitResult: RayTraceResult) {
        this.blockHit = blockHit.clone()
    }
}
