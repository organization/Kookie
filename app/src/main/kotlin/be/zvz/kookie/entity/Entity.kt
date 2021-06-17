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
import be.zvz.kookie.entity.animation.Animation
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector2
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.ByteTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.network.mcpe.protocol.AddActorPacket
import be.zvz.kookie.network.mcpe.protocol.MoveActorAbsolutePacket
import be.zvz.kookie.network.mcpe.protocol.SetActorMotionPacket
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataFlags
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.entity.NetworkAttribute
import be.zvz.kookie.player.Player
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler
import be.zvz.kookie.world.Position
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

    val networkProperties = EntityMetadataCollection()

    // TODO: fill this when event system is implemented
    var lastDamageCause: Any? = null

    protected var blocksAround: MutableList<Block>? = mutableListOf()

    protected var lastLocation: Location = Location()

    var motion: Vector3 = Vector3(0, 0, 0)
        protected set
    protected var lastMotion: Vector3 = Vector3(0, 0, 0)
    protected var forceMovementUpdate: Boolean = false

    var boundingBox: AxisAlignedBB
    var onGround: Boolean = false

    lateinit var size: EntitySizeInfo

    private var health: Float = 20F
    open var maxHealth: Int = 20

    protected var ySize: Float = 0F
    protected var stepHeight: Float = 0F
    var keepMovement: Boolean = false
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

    var canClimb: Boolean = false
        set(value) {
            networkProperties.setGenericFlag(EntityMetadataFlags.CAN_CLIMB, value)
            networkPropertiesDirty = true
            field = value
        }
    protected var canClimbWalls: Boolean = false
    var immobile: Boolean = false
        set(value) {
            networkProperties.setGenericFlag(EntityMetadataFlags.IMMOBILE, value)
            networkPropertiesDirty = true
            field = value
        }
    var invisible: Boolean = false
        set(value) {
            networkProperties.setGenericFlag(EntityMetadataFlags.INVISIBLE, value)
            networkPropertiesDirty = true
            field = value
        }
    var silent: Boolean = false
        set(value) {
            networkProperties.setGenericFlag(EntityMetadataFlags.SILENT, value)
            networkPropertiesDirty = true
            field = value
        }

    private var ownerId: Long = -1
    private var targetId: Long = -1

    var initialized: Boolean = false

    abstract val entityNetworkIdentifier: EntityIds

    init {
        location = location.asLocation()

        assert(
            !location.x.isNaN() && !location.x.isInfinite() &&
                !location.y.isNaN() && !location.y.isInfinite() &&
                !location.z.isNaN() && !location.z.isInfinite()
        )
        boundingBox = AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        motion = if (nbt !== null) {
            EntityDataHelper.parseVec3(nbt, "Motion", true)
        } else {
            Vector3()
        }

        // TODO: getWorld().addEntity(this)

        // TODO: lastUpdate = server.tick

        // TODO: EntitySpawnEvent call

        scheduleUpdate()
    }

    val world: World
        get() = location.world ?: throw AssertionError("Position world is null or has been unloaded")

    open fun recalculateBoundingBox() {
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

    open fun resetLastMovement() {
        lastLocation = location.asLocation()
        lastMotion = motion.clone()
    }

    open fun addAttributes() {
    }

    open fun initEntity(nbt: CompoundTag) {
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

    open fun saveNBT(): CompoundTag = EntityDataHelper.createBaseNBT(location, motion).also { compoundTag ->
        if (this !is Player) {
            EntityFactory.injectSaveId(this::class.java, nbt)

            if (nameTag != "") {
                compoundTag.setString("CustomName", nameTag)
                compoundTag.setByte("CustomNameVisible", if (nameTagVisible) 1 else 0)
            }
        }
        compoundTag.setFloat("FallDistance", fallDistance)
        compoundTag.setShort("Fire", fireTicks.toInt())
        compoundTag.setByte("OnGround", if (onGround) 1 else 0)
    }

    fun getOwningEntity(): Entity? {
        TODO("Should be implemented with World")
    }

    fun getTargetEntity(): Entity? {
        TODO("Should be implemented with world")
    }

    abstract fun getInitialSizeInfo(): EntitySizeInfo

    open fun attack(source: Any) {
        TODO("Requires event implementation")
    }

    open fun heal(source: Any) {
        TODO("Requires event implementation")
    }

    open fun kill() {
        if (isAlive()) {
            health = 0F
            onDeath()
            scheduleUpdate()
        }
    }

    protected open fun onDeath() {
    }

    protected open fun onDeathUpdate(tickDiff: Long): Boolean {
        return true
    }

    fun getHealth(): Float = health

    open fun setHealth(amount: Float) {
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

    protected open fun entityBaseTick(tickDiff: Long): Boolean {
        if (justCreated) {
            justCreated = false
            if (!isAlive()) {
                kill()
            }
        }

        val changedProperties = getDirtyNetworkData()
        if (changedProperties.isNotEmpty()) {
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

    open fun syncNetworkData(properties: EntityMetadataCollection) {
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

    fun getAllNetworkData(): MutableMap<Int, MetadataProperty> {
        if (networkPropertiesDirty) {
            syncNetworkData(networkProperties)
            networkPropertiesDirty = false
        }
        return networkProperties.getAll()
    }

    open fun setOnFire(seconds: Long) {
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

    protected open fun dealFireDamage() {
        // TODO: attack(EntityDamageEvent) here
    }

    open fun canCollideWith(entity: Entity): Boolean = !justCreated && entity != this

    open fun canBeCollidedWith(): Boolean = isAlive()

    protected open fun updateMovement(teleport: Boolean = false) {
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

    open fun getOffsetPosition(vector: Vector3): Vector3 = vector

    open fun broadcastMovement(teleport: Boolean = false) {
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

    open fun broadcastMotion() {
        val packet = SetActorMotionPacket.create(entityRuntimeId, motion)
        // TODO: server.broadcastPackets(hasSpawned, listOf(packet))
    }

    open fun hasGravity(): Boolean = gravityEnabled

    open fun setHasGravityEnabled(v: Boolean = true) {
        gravityEnabled = v
    }

    protected open fun applyDragBeforeGravity(): Boolean = false

    protected open fun tryChangeMovement() {
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

    open fun checkObstruction(x: Float, y: Float, z: Float): Boolean {
        val worldT = world
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

    open fun onUpdate(currentTick: Long): Boolean {
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

    open fun onNearByBlockChange() {
        forceMovementUpdate = true
        scheduleUpdate()
    }

    open fun onRandomUpdate() {
        scheduleUpdate()
    }

    open fun hasMovementUpdate(): Boolean {
        return forceMovementUpdate ||
            motion.x != 0.0 ||
            motion.y != 0.0 ||
            motion.z != 0.0 ||
            !onGround
    }

    open fun resetFallDistance() {
        fallDistance = 0F
    }

    protected open fun updateFallState(distanceThisTick: Long, onGround: Boolean) {
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

    open fun fall(fallDistance: Float) {
    }

    fun getEyeHeight(): Float = size.eyeHeight!!

    fun getEyePos(): Vector3 = Vector3(location.x, location.y + getEyeHeight(), location.z)

    open fun onCollideWithPlayer(player: Player) {
    }

    open fun isInsideOfSolid(): Boolean {
        /*
        TODO:
        val block = getWorld().getBlockAt(floor(location.x).toInt(), floor(location.y + getEyeHeight()).toInt(), floor(location.z).toInt())
        return block.isSolid() && !block.isTransparent() && block.collidesWithBB(boundingBox)
         */
        return false
    }

    open fun isUnderWater(): Boolean {
        /*
        val block = getWorld().getBlockAt(floor(location.x).toInt(), floor(location.y + getEyeHeight()).toInt(), floor(location.z).toInt())
        if (block is Water) {
            val y = floor(location.y + getEyeHeight()).toInt()
            val f = (y + 1) - (block.getFluidHeightPercent() - 0.1111111)
            return y < f
        }

         */
        return false
    }

    open fun move(dx: Double, dy: Double, dz: Double) {
        var dx = dx
        var dy = dy
        var dz = dz
        // TODO
        blocksAround = null

        // TODO: Timings.entityMove.startTiming()

        val movX = dx
        val movY = dy
        val movZ = dz

        if (keepMovement) {
            boundingBox.offset(dx, dy, dz)
        } else {
            ySize *= STEP_CLIP_MULTIPLIER

            var moveBB = boundingBox.clone()

            assert(abs(dx) <= 20 && abs(dy) <= 20 && abs(dz) <= 20)

            /*
            TODO:
            val list = getWorld().getCollisonBoxes(this, moveBB.addCoord(dx, dy, dz), false)
            list.forEach {
                dy = it.calculateYOffset(moveBB, dy)
            }
             */
            moveBB.offset(0.0, dy, 0.0)

            val fallingFlag = onGround || (dy != movY && movY < 0)
            /*
            list.forEach {
                dx = it.calculateXOffset(moveBB, dx)
            }
             */
            moveBB.offset(dx, 0.0, 0.0)
            /*
            list.forEach {
                dz = it.calculateZOffset(moveBB, dz)
            }
            */

            moveBB.offset(0.0, 0.0, dz)

            if (stepHeight > 0 && fallingFlag && (movX != dx || movZ != dz)) {
                val cx = dx
                val cy = dy
                val cz = dz
                dx = movX
                dy = stepHeight.toDouble()
                dz = movZ

                val stepBB = boundingBox.clone()

                /*
                val list = getWorld().getCollisionBoxes(this, stepBB.addCoord(dx, dy, dz), false)

                list.forEach {
                    dy = it.calculateYOffset(stepBB, dy)
                }

                stepBB.offset(0.0, dy, 0.0)

                list.forEach {
                    dx = it.calculateXOffset(stepBB, dx)
                }

                stepBB.offset(dx, 0.0, 0.0)

                list.forEach {
                    dz = it.calculateZOffset(stepBB, dz)
                }

                stepBB.offset(0.0, 0.0, dz)

                var reverseDY = -dy

                list.forEach {
                    reverseDY = it.calculateYOffset(stepBB, reverseDY)
                }
                dy += reverseDY
                stepBB.offset(0.0, reverseDY, 0.0)

                if ((cx.pow(2) + cz.pow(2)) >= (dx.pow(2) + dz.pow(2))) {
                    dx = cx
                    dy = cy
                    dz = cz
                } else {
                    moveBB = stepBB
                    ySize += dy.toFloat()
                }
                
                 */
            }
            boundingBox = moveBB
        }

        location = Location(
            (boundingBox.minX + boundingBox.maxX) / 2,
            boundingBox.minY - ySize,
            (boundingBox.minZ + boundingBox.maxZ) / 2,
            location.yaw,
            location.pitch,
            location.world
        )

        // getWorld().onEntityMoved(this)
        checkBlockCollision()
        checkGroundState(movX, movY, movZ, dx, dy, dz)
        updateFallState(dy.toLong(), onGround)

        motion = motion.withComponents(
            if (movX != dx) 0 else null,
            if (movY != dy) 0 else null,
            if (movZ != dz) 0 else null
        )
        // Timings.entityMove.stopTiming()
    }

    open fun checkGroundState(movX: Double, movY: Double, movZ: Double, dx: Double, dy: Double, dz: Double) {
        isCollidedVertically = movY != dy
        isCollidedHorizontally = (movX != dx || movZ != dz)
        isCollided = isCollidedHorizontally || isCollidedVertically
        onGround = movY != dy && movY < 0
    }

    open fun getBlocksAroundWithEntityInsideActions(): MutableList<Block> {
        if (blocksAround == null) {
            val inset = 0.001

            val minX = floor(boundingBox.minX + inset).toInt()
            val minY = floor(boundingBox.minY + inset).toInt()
            val minZ = floor(boundingBox.minZ + inset).toInt()
            val maxX = floor(boundingBox.maxX - inset).toInt()
            val maxY = floor(boundingBox.maxY - inset).toInt()
            val maxZ = floor(boundingBox.maxZ - inset).toInt()

            blocksAround = mutableListOf()

            world.let {
                for (z in minZ until maxZ) {
                    for (x in minX until maxX) {
                        for (y in minY until maxY) {
                            /*
                            val block = world.getBlockAt(x, y, z)
                            if (block.hasEntityCollision()) {
                                blocksAround?.add(block)
                            }
                             */
                        }
                    }
                }
            }
        }
        return blocksAround as MutableList<Block>
    }

    open fun canBeMovedByCurrents(): Boolean = true

    open fun checkBlockCollision() {
        val vectors = mutableListOf<Vector3>()

        getBlocksAroundWithEntityInsideActions().forEach {
            if (!it.onEntityInside(this)) {
                blocksAround = null
            }
            val v = it.addVelocityToEntity(this)
            if (v != null) {
                vectors.add(v)
            }
        }
        val vector = Vector3()
        vectors.forEach {
            vector.sum(it)
        }

        if (vector.lengthSquared() > 0) {
            val d = 0.014
            motion.add(vector.normalize().multiply(d))
        }
    }

    fun getPosition(): Position = location.asPosition()

    fun setPosition(pos: Vector3): Boolean {
        if (!closed) {
            return false
        }

        val oldWorld = world

        val newWorld = if (pos is Position) pos.world else oldWorld

        if (oldWorld != newWorld) {
            // despawnFromAll()
            // oldWorld.removeEntity(this)
        }

        location = Location.fromObject(pos, newWorld, location.yaw, location.pitch)
        recalculateBoundingBox()

        blocksAround = null

        if (oldWorld != newWorld) {
            // newWorld.addEntity(this)
        } else {
            // newWorld.onEntityMoved(this)
        }
        return true
    }

    fun setRotation(yaw: Double, pitch: Double) {
        location.yaw = yaw
        location.pitch = pitch
        scheduleUpdate()
    }

    protected fun setPositionAndRotation(pos: Vector3, yaw: Double, pitch: Double): Boolean {
        if (setPosition(pos)) {
            setRotation(yaw, pitch)
            return true
        }
        return false
    }

    fun setMotion(motion: Vector3): Boolean {
        if (!justCreated) {
            // TODO:
            // ev = EntityMotionEvent(this, motion)
            // ev.call()
            // if (ev.isCancelled()) {
            //     return false
            // }
        }
        this.motion = motion.clone()

        if (!justCreated) {
            updateMovement()
        }
        return true
    }

    fun addMotion(x: Double, y: Double, z: Double) {
        motion = motion.add(x, y, z)
    }

    fun addMotion(motion: Vector3) {
        this.motion = motion.add(motion)
    }

    open fun teleport(pos: Vector3, yaw: Double? = null, pitch: Double? = null): Boolean {
        val yaw = if (pos is Location) {
            if (yaw == null) {
                pos.yaw
            } else {
                yaw
            }
        } else {
            location.yaw
        }
        val pitch = if (pos is Location) {
            if (pitch == null) {
                pos.pitch
            } else {
                pitch
            }
        } else {
            location.pitch
        }

        val from = location.asPosition()
        val to = Position.fromObject(pos, if (pos is Position) pos.world else world)
        /*
        TODO:
        ev = EntityTeleportEvent(this, from, to)
        ev.call()
        if (ev.isCancelled()) {
            return false
        }
         */
        setMotion(Vector3(0, 0, 0))
        if (setPositionAndRotation(pos, yaw, pitch)) {
            resetFallDistance()
            forceMovementUpdate = true
            updateMovement(true)
            return true
        }
        return false
    }

    fun getId(): Long = entityRuntimeId

    fun getViewers(): MutableMap<Long, Player> = hasSpawned

    open fun sendSpawnPacket(player: Player) {
        val pk = AddActorPacket()
        pk.entityRuntimeId = getId()
        pk.type = entityNetworkIdentifier.value
        pk.position = location.asVector3()
        pk.motion = motion
        pk.yaw = location.yaw.toFloat()
        pk.headYaw = location.yaw.toFloat()
        pk.pitch = location.pitch.toFloat()
        attributeMap.getAll().forEach { (_, attr) ->
            pk.attributes.add(NetworkAttribute(attr.id, attr.minValue, attr.maxValue, attr.currentValue, attr.defaultValue))
        }
        pk.metadata = getAllNetworkData()

        player.session.sendDataPacket(pk)
    }

    open fun spawnTo(player: Player) {
        // TODO
        if (!initialized) {
            throw AssertionError("Entity must be initialized before calling spawnTo()")
        }
        if (
            !hasSpawned.containsKey(player.getId())
            /* && player.hasReceovedChunk(floor(location.x).toInt() shr 4, floor(location.z).toInt() shr 4)*/
        ) {
            hasSpawned[player.getId()] = player
            sendSpawnPacket(player)
        }
    }

    open fun spawnToAll() {
        if (closed) {
            return
        }
        /*
        TODO:
        getWorld().getViewersForPosition(location).forEach {
            spawnTo(it)
        }
         */
    }

    open fun respawnToAll() {
        hasSpawned.apply {
            val iterator = iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                iterator.remove()
                spawnTo(current.value)
            }
        }
    }

    open fun despawnFrom(player: Player, send: Boolean = true) {
        /*
        $id = spl_object_id($player);
		if(isset($this->hasSpawned[$id])){
			if($send){
				$player->getNetworkSession()->onEntityRemoved($this);
			}
			unset($this->hasSpawned[$id]);
		}
         */
        val id = player.getId()
        if (hasSpawned.containsKey(id)) {
            if (send) {
                // TODO: player.session.onEntityRemoved(this)
            }
            hasSpawned.remove(id)
        }
    }

    open fun despawnFromAll() {
        hasSpawned.forEach { (_, player) ->
            despawnFrom(player)
        }
    }

    open fun flagForDespawn() {
        needsDespawn = true
        scheduleUpdate()
    }

    fun isFlaggedForDespawn(): Boolean = needsDespawn

    fun isClosed(): Boolean = closed

    fun close() {
        if (closeInFlight) {
            return
        }
        if (!closed) {
            closeInFlight = true
            // TODO: EntityDespawnEvent(this).call()

            onDispose()
            closed = true
            destroyCycles()
            closeInFlight = false
        }
    }

    open fun onDispose() {
        despawnFromAll()
        if (location.isValid()) {
            // TODO: getWorld().removeEntity(this)
        }
    }

    open fun destroyCycles() {
        // FIXME: location = null???
        lastDamageCause = null
    }

    fun broadcastAnimation(animation: Animation, targets: List<Player>? = null) {
        // TODO: server.broadcastPackets(if (targets == null) getViewers() else targets, animation.encode())
    }

    /**
     * TODO: Sound
     */
    fun broadcastSound(sound: Any, targets: List<Player>? = null) {
        if (!silent) {
            // TODO: server.broadcastPackets(if (targets == null) getViewers() else targets, sound.encode(location))
        }
    }

    companion object {
        var entityId: Long = 0

        const val MOTION_THRESHOLD = 0.00001
        const val STEP_CLIP_MULTIPLIER = 0.4F

        fun nextRuntimeId(): Long = ++entityId
    }
}
