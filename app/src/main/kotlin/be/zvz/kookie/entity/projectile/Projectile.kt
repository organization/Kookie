package be.zvz.kookie.entity.projectile

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.Location
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.math.VoxelRayTrace
import be.zvz.kookie.nbt.tag.ByteTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import kotlin.math.ceil

abstract class Projectile(location: Location, val shootingEntity: Entity?, nbt: CompoundTag?) : Entity(location, nbt) {

    var damage: Float = 0F

    var blockHit: Block? = null

    init {
        shootingEntity?.let {
            owningEntity = it
        }
    }

    override fun attack(source: Any) {
        /*
        TODO:
        if (source.cause == EntityDamageEvent.CAUSE_VOID) {
        super.attack(source)
         */
    }

    override fun initEntity(nbt: CompoundTag) {
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

    override fun canCollideWith(entity: Entity): Boolean {
        return entity is Living && !onGround
    }

    override fun canBeCollidedWith(): Boolean {
        return false
    }

    open fun getBaseDamage(): Float = damage

    open fun setBaseDamage(damage: Float) {
        this.damage = damage
    }

    open fun getResultDamage(): Int {
        return ceil(motion.length() * damage).toInt()
    }

    override fun saveNBT(): CompoundTag {
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

        /*
        world.getEntityCollidingEntities().forEach {

        }
        /*
        $this->blocksAround = null;

		Timings::$entityMove->startTiming();

		$start = $this->location->asVector3();
		$end = $start->addVector($this->motion);

		$blockHit = null;
		$entityHit = null;
		$hitResult = null;

		foreach(VoxelRayTrace::betweenPoints($start, $end) as $vector3){
			$block = $this->getWorld()->getBlockAt($vector3->x, $vector3->y, $vector3->z);

			$blockHitResult = $this->calculateInterceptWithBlock($block, $start, $end);
			if($blockHitResult !== null){
				$end = $blockHitResult->hitVector;
				$blockHit = $block;
				$hitResult = $blockHitResult;
				break;
			}
		}

		$entityDistance = PHP_INT_MAX;

		$newDiff = $end->subtractVector($start);
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
         */
    }
}
