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
package be.zvz.kookie.entity

import be.zvz.kookie.Server
import be.zvz.kookie.block.Block
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector2
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.ByteTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.network.mcpe.protocol.MoveActorAbsolutePacket
import be.zvz.kookie.network.mcpe.protocol.SetActorMotionPacket
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataFlags
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.player.Player
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler
import be.zvz.kookie.world.World
import com.koloboke.collect.map.hash.HashLongObjMaps
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sin

abstract class Entity @JvmOverloads constructor(var location: Location, nbt: CompoundTag? = null) {

    var nbt: CompoundTag = nbt ?: CompoundTag()

    private val hasSpawned: MutableMap<Long, Player> = HashLongObjMaps.newMutableMap()

    private val entityRuntimeId: Long = nextRuntimeId()

    private val networkProperties = EntityMetadataCollection()

    // TODO: fill this when event system is implemented
    private var lastDamageCause: Any? = null

    protected val blocksAround = mutableListOf<Block>()

    protected var lastLocation: Location = Location()

    protected var motion: Vector3 = Vector3(0, 0, 0)
    protected var lastMotion: Vector3 = Vector3(0, 0, 0)
    protected var forceMovementUpdate: Boolean = false

    var boundingBox: AxisAlignedBB
    var onGround: Boolean = false

    private lateinit var size: EntitySizeInfo

    private var health: Float = 20F
    private var maxHealth: Int = 20

    protected var ySize: Float = 0F
    protected var stepHeight: Float = 0F
    var fallDistance: Float = 0F
    var ticksLived: Long = 0
    var lastUpdate: Long = 0
    private var fireTicks: Long = 0
    var canCollide: Boolean = true

    var isStatic: Boolean = false
    var savedWithChunk: Boolean = false

    var isCollided: Boolean = false
    var isCollidedHorizontally: Boolean = false
    var isCollidedVertically: Boolean = false

    var noDamageTicks: Long = 0
    var justCreated: Boolean = true

    val attributeMap = AttributeMap()

    protected abstract val gravity: Float
    protected abstract val drag: Float
    protected var gravityEnabled: Boolean = true

    protected open var server: Server = Server.instance

    protected var closed: Boolean = false
    private var closeInFlight: Boolean = false
    private var needsDespawn: Boolean = false

    protected lateinit var timings: TimingsHandler

    protected var networkPropertiesDirty: Boolean = false

    var nameTag: String = ""
    var nameTagVisible: Boolean = true
    var alwaysShowNameTag: Boolean = false
    var scoreTag: String = ""
    var scale: Float = 1F

    protected var canClimb: Boolean = false
    protected var canClimbWalls: Boolean = false
    protected var immobile: Boolean = false
    protected var invisible: Boolean = false
    protected var silent: Boolean = false

    protected var ownerId: Long? = null
    protected var targetId: Long? = null

    init {
        location = location.asLocation()

        assert(
            !location.x.isNaN() && !location.x.isInfinite() &&
                !location.y.isNaN() && !location.y.isInfinite() &&
                !location.z.isNaN() && !location.z.isInfinite()
        )
        boundingBox = AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        recalculateBoundingBox()

        motion = if (nbt != null) {
            EntityDataHelper.parseVec3(nbt, "Motion", true)
        } else {
            Vector3()
        }
        resetLastMovement()

        addAttributes()

        initEntity(nbt ?: CompoundTag.create())

        // TODO: getWorld().addEntity(this)

        // TODO: lastUpdate = server.tick

        // TODO: EntitySpawnEvent call

        scheduleUpdate()
    }

    fun getWorld(): World {
        return location.world!!
    }

    fun recalculateBoundingBox() {
        val halfWidth = size.width / 2
        boundingBox = AxisAlignedBB(
            location.x - halfWidth,
            location.y + ySize,
            location.z - halfWidth,
            location.x + halfWidth,
            location.y + size.height + ySize,
            location.z + halfWidth
        )
    }

    fun resetLastMovement() {
        lastLocation = location.asLocation()
        lastMotion = motion.clone()
    }

    fun addAttributes() {
    }

    protected fun initEntity(nbt: CompoundTag) {
        // JLS 7 17.5
        timings = Timings.getEntityTimings(this)
        size = getInitialSizeInfo()

        fireTicks = nbt.getShort("Fire", 0).toLong()
        onGround = nbt.getByte("OnGround", 0) != 0
        fallDistance = nbt.getFloat("FallDistance", 0F)

        val customNameTag = nbt.getTag("CustomName")
        if (customNameTag is StringTag) {
            nameTag = customNameTag.value

            val customNameVisibleTag = nbt.getTag("CustomNameVisible")
            if (customNameVisibleTag is ByteTag) {
                nameTagVisible = customNameVisibleTag.value != 0
            }
        }
    }

    abstract fun getInitialSizeInfo(): EntitySizeInfo

    fun attack(source: Any) {
        TODO("Requires event implementation")
    }

    fun heal(source: Any) {
        TODO("Requires event implementation")
    }

    fun kill() {
        if (isAlive()) {
            health = 0F
            onDeath()
            scheduleUpdate()
        }
    }

    protected fun onDeath() {
    }

    protected fun onDeathUpdate(tickDiff: Long): Boolean {
        return true
    }

    fun getHealth(): Float = health

    fun setHealth(amount: Float) {
        if (amount == health) {
            return
        }

        if (amount <= 0) {
            if (isAlive()) {
                kill()
            } else {
                health = 0F
            }
        } else if (amount <= maxHealth || amount < health) {
            health = amount
        } else {
            health = maxHealth.toFloat()
        }
    }

    fun getMaxHealth(): Int = maxHealth

    fun setMaxHealth(amount: Int) {
        maxHealth = amount
    }

    fun setLastDamageCause(type: Any) {
        lastDamageCause = type
    }

    fun getLastDamageCause(): Any? = lastDamageCause

    fun getNetworkProperties(): EntityMetadataCollection = networkProperties

    protected fun entityBaseTick(tickDiff: Long): Boolean {
        if (justCreated) {
            justCreated = false
            if (!isAlive()) {
                kill()
            }
        }

        val changedProperties = getDirtyNetworkData()
        if (changedProperties.size > 0) {
            sendData(null, changedProperties)
            networkProperties.clearDirtyProperties()
        }

        var hasUpdate = false

        checkBlockCollision()

        if (location.y <= -16 && isAlive()) {
            // TODO: attack(EntityDamageEvent)
            hasUpdate = true
        }
        if (isOnFire() && doOnFireTick(tickDiff)) {
            hasUpdate = true
        }

        if (noDamageTicks > 0) {
            noDamageTicks -= tickDiff
            if (noDamageTicks < 0) {
                noDamageTicks = 0
            }
        }
        ticksLived += tickDiff
        return hasUpdate
    }

    fun getDirtyNetworkData(): MutableMap<Int, MetadataProperty> {
        if (networkPropertiesDirty) {
            syncNetworkData(networkProperties)
            networkPropertiesDirty = false
        }
        return networkProperties.getDirty()
    }

    fun syncNetworkData(properties: EntityMetadataCollection) {
        properties.setByte(EntityMetadataProperties.ALWAYS_SHOW_NAMETAG, if (alwaysShowNameTag) 1 else 0)
        properties.setFloat(EntityMetadataProperties.BOUNDING_BOX_HEIGHT, size.height)
        properties.setFloat(EntityMetadataProperties.BOUNDING_BOX_WIDTH, size.width)
        properties.setFloat(EntityMetadataProperties.SCALE, scale)
        properties.setLong(EntityMetadataProperties.LEAD_HOLDER_EID, -1)
        properties.setLong(EntityMetadataProperties.OWNER_EID, -1)
        properties.setLong(EntityMetadataProperties.TARGET_EID, -1)
        properties.setString(EntityMetadataProperties.NAMETAG, nameTag)
        properties.setString(EntityMetadataProperties.SCORE_TAG, scoreTag)
        properties.setByte(EntityMetadataProperties.COLOR, 0) // Offhand, blame mojang

        properties.setGenericFlag(EntityMetadataFlags.AFFECTED_BY_GRAVITY, true)
        properties.setGenericFlag(EntityMetadataFlags.CAN_CLIMB, canClimb)
        properties.setGenericFlag(EntityMetadataFlags.CAN_SHOW_NAMETAG, nameTagVisible)
        properties.setGenericFlag(EntityMetadataFlags.HAS_COLLISION, true)
        properties.setGenericFlag(EntityMetadataFlags.IMMOBILE, immobile)
        properties.setGenericFlag(EntityMetadataFlags.INVISIBLE, invisible)
        properties.setGenericFlag(EntityMetadataFlags.SILENT, silent)
        properties.setGenericFlag(EntityMetadataFlags.ONFIRE, isOnFire())
        properties.setGenericFlag(EntityMetadataFlags.WALLCLIMBING, canClimbWalls)
    }

    fun sendData(targets: MutableMap<Long, Player>?, data: MutableMap<Int, MetadataProperty>? = null) {
        val target = targets ?: hasSpawned
        var sendData = data ?: getAllNetworkData()

        target.forEach { (_, player) ->
            // TODO: player.session.syncActorData()
        }
    }

    fun scheduleUpdate() {
        // TODO: $this->getWorld()->updateEntities[$this->id] = $this
    }

    fun isAlive(): Boolean {
        return health > 0
    }

    fun getAllNetworkData(): Any? {
        // TODO
        return null
    }

    fun checkBlockCollision() {
        // TODO
    }

    fun setOnFire(seconds: Long) {
        val ticks = seconds * 20
        if (ticks > fireTicks) {
            fireTicks
        }
    }

    fun setFireTicks(ticks: Long) {
        if (fireTicks < 0 || ticks > 0x7fff) {
            throw IllegalArgumentException("Fire ticks must be range in 0 ... " + 0x7fff + ", got $ticks")
        }
        fireTicks = ticks
    }

    fun isOnFire(): Boolean = fireTicks > 0

    fun doOnFireTick(tickDiff: Long): Boolean {
        if (isFireProof() && fireTicks > 1) {
            fireTicks = 1
        } else {
            fireTicks -= tickDiff
        }
        if ((fireTicks % 20 == 0L) || tickDiff > 20) {
            dealFireDamage()
        }
        if (!isOnFire()) {
            extinguish()
        } else {
            return true
        }
        return false
    }

    fun isFireProof(): Boolean = false

    fun extinguish() {
        fireTicks = 0
    }

    protected fun dealFireDamage() {
        // TODO: attack(EntityDamageEvent) here
    }

    fun canCollideWith(entity: Entity): Boolean = !justCreated && entity != this

    fun canBeCollidedWith(): Boolean = isAlive()

    protected fun updateMovement(teleport: Boolean = false) {
        val diffPosition = location.distanceSquared(lastLocation)
        val diffRotation = (location.yaw - lastLocation.yaw).pow(2) + (location.pitch - lastLocation.pitch).pow(2)

        val diffMotion = motion.subtract(lastMotion).lengthSquared()

        val still = motion.lengthSquared() == 0.0
        val wasStill = lastMotion.lengthSquared() == 0.0

        if (wasStill != still) {
            immobile = still
        }

        if (teleport || diffPosition > 0.0001 || diffRotation > 1.0 || (!wasStill && still)) {
            lastLocation = location.asLocation()
            // TODO: broadcastMovement(teleport)
        }

        if (diffMotion > 0.0025 || wasStill != still) {
            lastMotion = motion.clone()
            // TODO: broadcastMotion()
        }
    }

    fun getOffsetPosition(vector: Vector3): Vector3 = vector

    fun broadcastMovement(teleport: Boolean = false) {
        val packet = MoveActorAbsolutePacket.create(
            entityRuntimeId,
            getOffsetPosition(location),
            location.pitch.toFloat(),
            location.yaw.toFloat(),
            location.yaw.toFloat(),
            if (teleport) {
                MoveActorAbsolutePacket.FLAG_TELEPORT
            } else {
                0
            } or if (onGround) {
                MoveActorAbsolutePacket.FLAG_GROUND
            } else {
                0
            }
        )
        // TODO: server.broadcastPackets(hasSpawned, listOf(packet))
    }

    fun broadcastMotion() {
        val packet = SetActorMotionPacket.create(entityRuntimeId, motion)
        // TODO: server.broadcastPackets(hasSpawned, listOf(packet))
    }

    fun hasGravity(): Boolean = gravityEnabled

    fun setHasGravityEnabled(v: Boolean = true) {
        gravityEnabled = v
    }

    protected fun applyDragBeforeGravity(): Boolean = false

    protected fun tryChangeMovement() {
        val friction = 1 - drag

        var mY = motion.y

        if (applyDragBeforeGravity()) {
            mY *= friction
        }

        if (gravityEnabled) {
            mY -= gravity
        }

        if (!applyDragBeforeGravity()) {
            mY *= friction
        }

        if (onGround) {
            // TODO: friction *= getWorld().getBlockAt(floor(location.x), floor(location.y - 1), floor(location.z)).getFrictionFactor()
        }
        motion = Vector3(motion.x * friction, mY, motion.z * friction)
    }

    fun checkObstruction(x: Float, y: Float, z: Float): Boolean {
        val world = getWorld()
        /*
        TODO:
        if (world.getCollisionBoxes(this, getBoundingBox(), false).size == 0) {
            return false
        }
         */

        val floorX = floor(x).toInt()
        val floorY = floor(y).toInt()
        val floorZ = floor(z).toInt()

        val diffX = x - floorX
        val diffY = y - floorY
        val diffZ = z - floorZ

        /*
        TODO:
        if (world.getBlockAt(floorX, floorY, floorZ).isSolid()) {
            val westNonSolid = !world.getBlockAt(floorX - 1, floorY, floorZ).isSolid()
            val eastNonSolid = !world.getBlockAt(floorX + 1, floorY, floorZ).isSolid()
            val downNonSolid = !world.getBlockAt(floorX, floorY - 1, floorZ).isSolid()
            val upNonSolid    = !world.getBlockAt(floorX, floorY + 1, floorZ).isSolid()
            val northNonSolid = !world.getBlockAt(floorX, floorY, floorZ - 1).isSolid()
            val southNonSolid = !world.getBlockAt(floorX, floorY, floorZ + 1).isSolid()

            var direction = -1
            var limit = 9999F

            if (westNonSolid) {
                limit = diffX
                direction = Facing.WEST.value
            }

            if (eastNonSolid && 1 - diffX < limit) {
                limit = 1 - diffX
                direction = Facing.EAST.value
            }

            if (northNonSolid && diffY < limit) {
                limit = diffY
                direction = Facing.DOWN.value
            }

            if (upNonSolid && 1 - diffY < limit) {
                limit = diffY
                direction = Facing.DOWN.value
            }

            if (northNonSolid && diffZ < limit) {
                limit = diffZ
                direction = Facing.NORTH.value
            }

            if (southNonSolid && 1 - diffZ < limit) {
                direction = Facing.SOUTH
            }

            val force = Math.random() * 0.2 + 0.1

            if (direction == Facing.WEST.value) {
                motion = motion.withComponents(-force, null, null)
                return true
            }

            if (direction == Facing.EAST.value) {
                motion = motion.withComponents(force, null, null)
                return true
            }

            if (direction == Facing.DOWN.value) {
                motion = motion.withComponents(null, -force, null)
                return true
            }
            if (direction == Facing.UP.value) {
                motion = motion.withComponents(null, force, null)
                return true
            }

            if (direction == Facing.NORTH.value) {
                motion = motion.withComponents(null, null, -force)
                return true
            }
            if (direction == Facing.SOUTH.value) {
                motion = motion.withComponents(null, null, force)
                return true
            }
        }
         */
        return false
    }

    fun getHorizontalFacing(): Facing {
        var angle = location.yaw.mod(360.0)
        if (angle < 0) {
            angle += 360.0
        }
        if ((0 <= angle && angle < 45) || (315 <= angle && angle < 360)) {
            return Facing.SOUTH
        }
        if (45 <= angle && angle < 135) {
            return Facing.WEST
        }
        if (135 <= angle && angle < 255) {
            return Facing.NORTH
        }
        return Facing.EAST
    }

    fun getDirectionVector(): Vector3 {
        val y = -sin(Math.toRadians(location.pitch))
        val xz = cos(Math.toRadians(location.pitch))
        val x = -xz * sin(Math.toRadians(location.yaw))
        val z = xz * cos(Math.toRadians(location.yaw))
        return Vector3(x, y, z).normalize()
    }

    fun getDirectionPlane(): Vector2 {
        return Vector2(-cos(Math.toRadians(location.yaw) - PI), -sin(Math.toRadians(location.yaw) - PI)).normalize()
    }

    fun onUpdate(currentTick: Long): Boolean {
        if (closed) {
            return false
        }

        val tickDiff = currentTick - lastUpdate
        if (tickDiff <= 0) {
            if (!justCreated) {
                // TODO: make logger public? 
                // server.logger.debug("Expected tick difference of at least 1, got $tickDiff for " + this::class.java.simpleName)
            }
            return true
        }
        lastUpdate = currentTick

        if (isAlive()) {
            if (onDeathUpdate(tickDiff)) {
                flagForDespawn()
            }
            return true
        }

        timings.startTiming()

        if (hasMovementUpdate()) {
            tryChangeMovement()
            motion = motion.withComponents(
                if (abs(motion.x) <= MOTION_THRESHOLD) 0 else null,
                if (abs(motion.y) <= MOTION_THRESHOLD) 0 else null,
                if (abs(motion.z) <= MOTION_THRESHOLD) 0 else null
            )
            if (motion.x != 0.0 || motion.y != 0.0 || motion.z != 0.0) {
                move(motion.x, motion.y, motion.z)
            }
            forceMovementUpdate = false
        }
        updateMovement()

        // TODO: Timings.entityBaseTick.startTiming()
        val hasUpdate = entityBaseTick(tickDiff)
        // TODO: Timings.entityBaseTick.stopTiming()
        return hasUpdate
    }

    fun onNearByBlockChange() {
        forceMovementUpdate = true
        scheduleUpdate()
    }

    fun onRandomUpdate() {
        scheduleUpdate()
    }

    fun hasMovementUpdate(): Boolean {
        return forceMovementUpdate ||
            motion.x != 0.0 ||
            motion.y != 0.0 ||
            motion.z != 0.0 ||
            !onGround
    }

    fun resetFallDistance() {
        fallDistance = 0F
    }

    protected fun updateFallState(distanceThisTick: Long, onGround: Boolean) {
        if (onGround) {
            if (fallDistance > 0) {
                fall(fallDistance)
                resetFallDistance()
            }
        } else if (distanceThisTick < fallDistance) {
            fallDistance -= distanceThisTick
        } else {
            fallDistance = 0F
        }
    }

    fun fall(fallDistance: Float) {
    }

    fun getEyeHeight(): Float = size.eyeHeight!!

    fun getEyePos(): Vector3 = Vector3(location.x, location.y + getEyeHeight(), location.z)

    fun onCollideWithPlayer(player: Player) {
    }

    fun isUnderWater(): Boolean {
        /*
        TODO:
        val block = getWorld().getBlockAt(floor(location.x).toInt(), floor(location.y + getEyeHeight()).toInt(), floor(location.z).toInt())
        return block.isSolid() && !block.isTransparent() && block.collidesWithBB(boundingBox)
         */
        return false
    }

    fun move(dx: Double, dy: Double, dz: Double) {
        // TODO
        /*
        $this->blocksAround = null;

		Timings::$entityMove->startTiming();

		$movX = $dx;
		$movY = $dy;
		$movZ = $dz;

		if($this->keepMovement){
			$this->boundingBox->offset($dx, $dy, $dz);
		}else{
			$this->ySize *= self::STEP_CLIP_MULTIPLIER;

			/*
			if($this->isColliding){ //With cobweb?
				$this->isColliding = false;
				$dx *= 0.25;
				$dy *= 0.05;
				$dz *= 0.25;
				$this->motionX = 0;
				$this->motionY = 0;
				$this->motionZ = 0;
			}
			*/

			$moveBB = clone $this->boundingBox;

			/*$sneakFlag = $this->onGround and $this instanceof Player;

			if($sneakFlag){
				for($mov = 0.05; $dx != 0.0 and count($this->world->getCollisionCubes($this, $this->boundingBox->getOffsetBoundingBox($dx, -1, 0))) === 0; $movX = $dx){
					if($dx < $mov and $dx >= -$mov){
						$dx = 0;
					}elseif($dx > 0){
						$dx -= $mov;
					}else{
						$dx += $mov;
					}
				}

				for(; $dz != 0.0 and count($this->world->getCollisionCubes($this, $this->boundingBox->getOffsetBoundingBox(0, -1, $dz))) === 0; $movZ = $dz){
					if($dz < $mov and $dz >= -$mov){
						$dz = 0;
					}elseif($dz > 0){
						$dz -= $mov;
					}else{
						$dz += $mov;
					}
				}

				//TODO: big messy loop
			}*/

			assert(abs($dx) <= 20 and abs($dy) <= 20 and abs($dz) <= 20, "Movement distance is excessive: dx=$dx, dy=$dy, dz=$dz");

			$list = $this->getWorld()->getCollisionBoxes($this, $moveBB->addCoord($dx, $dy, $dz), false);

			foreach($list as $bb){
				$dy = $bb->calculateYOffset($moveBB, $dy);
			}

			$moveBB->offset(0, $dy, 0);

			$fallingFlag = ($this->onGround or ($dy != $movY and $movY < 0));

			foreach($list as $bb){
				$dx = $bb->calculateXOffset($moveBB, $dx);
			}

			$moveBB->offset($dx, 0, 0);

			foreach($list as $bb){
				$dz = $bb->calculateZOffset($moveBB, $dz);
			}

			$moveBB->offset(0, 0, $dz);

			if($this->stepHeight > 0 and $fallingFlag and ($movX != $dx or $movZ != $dz)){
				$cx = $dx;
				$cy = $dy;
				$cz = $dz;
				$dx = $movX;
				$dy = $this->stepHeight;
				$dz = $movZ;

				$stepBB = clone $this->boundingBox;

				$list = $this->getWorld()->getCollisionBoxes($this, $stepBB->addCoord($dx, $dy, $dz), false);
				foreach($list as $bb){
					$dy = $bb->calculateYOffset($stepBB, $dy);
				}

				$stepBB->offset(0, $dy, 0);

				foreach($list as $bb){
					$dx = $bb->calculateXOffset($stepBB, $dx);
				}

				$stepBB->offset($dx, 0, 0);

				foreach($list as $bb){
					$dz = $bb->calculateZOffset($stepBB, $dz);
				}

				$stepBB->offset(0, 0, $dz);

				$reverseDY = -$dy;
				foreach($list as $bb){
					$reverseDY = $bb->calculateYOffset($stepBB, $reverseDY);
				}
				$dy += $reverseDY;
				$stepBB->offset(0, $reverseDY, 0);

				if(($cx ** 2 + $cz ** 2) >= ($dx ** 2 + $dz ** 2)){
					$dx = $cx;
					$dy = $cy;
					$dz = $cz;
				}else{
					$moveBB = $stepBB;
					$this->ySize += $dy;
				}
			}

			$this->boundingBox = $moveBB;
		}

		$this->location = new Location(
			($this->boundingBox->minX + $this->boundingBox->maxX) / 2,
			$this->boundingBox->minY - $this->ySize,
			($this->boundingBox->minZ + $this->boundingBox->maxZ) / 2,
			$this->location->yaw,
			$this->location->pitch,
			$this->location->world
		);

		$this->getWorld()->onEntityMoved($this);
		$this->checkBlockCollision();
		$this->checkGroundState($movX, $movY, $movZ, $dx, $dy, $dz);
		$this->updateFallState($dy, $this->onGround);

		$this->motion = $this->motion->withComponents(
			$movX != $dx ? 0 : null,
			$movY != $dy ? 0 : null,
			$movZ != $dz ? 0 : null
		);

		//TODO: vehicle collision events (first we need to spawn them!)

		Timings::$entityMove->stopTiming();
         */
    }

    fun flagForDespawn() {
        needsDespawn = true
        scheduleUpdate()
    }

    companion object {
        var entityId: Long = 0

        const val MOTION_THRESHOLD = 0.00001
        const val STEP_CLIP_MULTIPLIER = 0.4

        fun nextRuntimeId(): Long = ++entityId
    }
}
