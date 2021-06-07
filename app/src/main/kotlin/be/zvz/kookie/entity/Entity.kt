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
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.ByteTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.player.Player
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler
import be.zvz.kookie.world.World
import com.koloboke.collect.map.hash.HashLongObjMaps

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
        /*
        $properties->setByte(EntityMetadataProperties::ALWAYS_SHOW_NAMETAG, $this->alwaysShowNameTag ? 1 : 0);
		$properties->setFloat(EntityMetadataProperties::BOUNDING_BOX_HEIGHT, $this->size->getHeight());
		$properties->setFloat(EntityMetadataProperties::BOUNDING_BOX_WIDTH, $this->size->getWidth());
		$properties->setFloat(EntityMetadataProperties::SCALE, $this->scale);
		$properties->setLong(EntityMetadataProperties::LEAD_HOLDER_EID, -1);
		$properties->setLong(EntityMetadataProperties::OWNER_EID, $this->ownerId ?? -1);
		$properties->setLong(EntityMetadataProperties::TARGET_EID, $this->targetId ?? 0);
		$properties->setString(EntityMetadataProperties::NAMETAG, $this->nameTag);
		$properties->setString(EntityMetadataProperties::SCORE_TAG, $this->scoreTag);
		$properties->setByte(EntityMetadataProperties::COLOR, 0);

		$properties->setGenericFlag(EntityMetadataFlags::AFFECTED_BY_GRAVITY, true);
		$properties->setGenericFlag(EntityMetadataFlags::CAN_CLIMB, $this->canClimb);
		$properties->setGenericFlag(EntityMetadataFlags::CAN_SHOW_NAMETAG, $this->nameTagVisible);
		$properties->setGenericFlag(EntityMetadataFlags::HAS_COLLISION, true);
		$properties->setGenericFlag(EntityMetadataFlags::IMMOBILE, $this->immobile);
		$properties->setGenericFlag(EntityMetadataFlags::INVISIBLE, $this->invisible);
		$properties->setGenericFlag(EntityMetadataFlags::SILENT, $this->silent);
		$properties->setGenericFlag(EntityMetadataFlags::ONFIRE, $this->isOnFire());
		$properties->setGenericFlag(EntityMetadataFlags::WALLCLIMBING, $this->canClimbWalls);
         */

        // TODO
        properties.setByte(EntityMetadataProperties.ALWAYS_SHOW_NAMETAG, if (alwaysShowNameTag) 1 else 0)
        properties.setFloat(EntityMetadataProperties.BOUNDING_BOX_HEIGHT, size.height)
        properties.setFloat(EntityMetadataProperties.BOUNDING_BOX_WIDTH, size.width)
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

    companion object {
        var entityId: Long = 0

        const val MOTION_THRESHOLD = 0.00001
        const val STEP_CLIP_MULTIPLIER = 0.4

        fun nextRuntimeId(): Long = ++entityId
    }
}
