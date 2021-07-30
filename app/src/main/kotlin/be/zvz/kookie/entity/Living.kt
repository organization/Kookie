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

import be.zvz.kookie.block.Block
import be.zvz.kookie.data.bedrock.EffectIdMap
import be.zvz.kookie.entity.animation.DeathAnimation
import be.zvz.kookie.entity.animation.HurtAnimation
import be.zvz.kookie.entity.animation.RespawnAnimation
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.entity.effect.EffectManager
import be.zvz.kookie.entity.effect.VanillaEffects
import be.zvz.kookie.inventory.ArmorInventory
import be.zvz.kookie.inventory.CallbackInventoryListener
import be.zvz.kookie.item.Durable
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.enchantment.Enchantment
import be.zvz.kookie.item.enchantment.VanillaEnchantments
import be.zvz.kookie.math.VoxelRayTrace
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.FloatTag
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataFlags
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.player.Player
import be.zvz.kookie.utils.Binary
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

abstract class Living(location: Location, nbt: CompoundTag? = null) : Entity(location, nbt) {
    override val gravity: Float = 0.08F
    override val drag: Float = 0.02F

    var attackTime: Long = 0

    var deadTicks: Long = 0

    protected var maxDeadTicks: Long = 25

    var jumpVelocity: Double = 0.42
        get() {
            val jumpBoost = effectManager.get(VanillaEffects.JUMP_BOOST.effect)
            return field + (jumpBoost?.effectLevel ?: 0) / 10
        }
        protected set

    lateinit var effectManager: EffectManager

    open lateinit var armorInventory: ArmorInventory

    var breathing: Boolean = true
    var breathTicks: Long = DEFAULT_BREATH_TICKS
        set(value) {
            networkPropertiesDirty = true
            field = value
        }
    var maxBreathTicks: Long = DEFAULT_BREATH_TICKS
        protected set

    protected lateinit var healthAttr: Attribute
    protected lateinit var absorptionAttr: Attribute
    protected lateinit var knockbackResistanceAttr: Attribute
    protected lateinit var moveSpeedAttr: Attribute

    var sprinting: Boolean = false
        set(value) {
            if (value != isSprinting()) {
                field = value
                networkPropertiesDirty = true
                val moveSpeed = getMovementSpeed()
                setMovementSpeed(if (value) moveSpeed * 1.3F else moveSpeed / 1.3F)
                moveSpeedAttr.markSynchronized(false)
            }
            networkPropertiesDirty = true
            field = value
        }
    var sneaking: Boolean = false
        set(value) {
            networkPropertiesDirty = true
            field = value
        }

    override var maxHealth: Int
        set(amount) {
            healthAttr.maxValue = amount.toFloat()
            healthAttr.defaultValue = amount.toFloat()
        }
        get() = healthAttr.maxValue.toInt()

    override fun initEntity(nbt: CompoundTag) {
        super.initEntity(nbt)
        effectManager = EffectManager(this)
        effectManager.effectAddHooks.add { _, _ ->
            networkPropertiesDirty = true
        }
        effectManager.effectRemoveHooks.add { _ ->
            networkPropertiesDirty = true
        }

        armorInventory = ArmorInventory(this)

        armorInventory.getListeners().add(
            CallbackInventoryListener.onAnyChange {
                getViewers().forEach { (_, viewer) ->
                    // TODO: viewer.networkSession.onMobArmorChange(this)
                }
            }
        )

        var health = maxHealth.toFloat()

        val healthTag = nbt.getTag("HealF")
        if (healthTag is FloatTag) {
            health = healthTag.value
        }

        setHealth(health)

        // TODO: setAirSupplyTicks(nbt.getShort("Air", DEFAULT_BREATH_TICKS))

        nbt.getListTag("ActiveEffects")?.let { activeEffectsTag ->
            activeEffectsTag.value.forEach {
                if (it is CompoundTag) {
                    val effect = EffectIdMap.fromId(it.getByte("Id")) ?: return
                    effectManager.add(
                        EffectInstance(
                            effect,
                            it.getInt("Duration"),
                            Binary.unsignByte(it.getByte("Amplifier")),
                            it.getByte("ShowParticles", 1) != 0,
                            it.getByte("Ambient", 0) != 0
                        )
                    )
                }
            }
        }
    }

    override fun addAttributes() {
        attributeMap.apply {
            healthAttr = AttributeFactory.mustGet(Attribute.Identifier.HEALTH)
            add(healthAttr)
            add(AttributeFactory.mustGet(Attribute.Identifier.FOLLOW_RANGE))
            knockbackResistanceAttr = AttributeFactory.mustGet(Attribute.Identifier.KNOCKBACK_RESISTANCE)
            add(knockbackResistanceAttr)
            moveSpeedAttr = AttributeFactory.mustGet(Attribute.Identifier.MOVEMENT_SPEED)
            add(moveSpeedAttr)
            add(AttributeFactory.mustGet(Attribute.Identifier.ATTACK_DAMAGE))
            absorptionAttr = AttributeFactory.mustGet(Attribute.Identifier.ABSORPTION)
            add(absorptionAttr)
        }
    }

    override fun setHealth(amount: Float) {
        val wasAlive = isAlive()
        super.setHealth(amount)
        healthAttr.setValue(ceil(getHealth()), true)
        if (isAlive() && !wasAlive) {
            broadcastAnimation(RespawnAnimation(this))
        }
    }

    open fun getAbsorption(): Float = absorptionAttr.currentValue

    open fun setAbsorption(absorption: Float) {
        absorptionAttr.setValue(absorption)
    }

    open fun isSneaking(): Boolean = sneaking

    open fun isSprinting(): Boolean = sprinting

    open fun getMovementSpeed(): Float = moveSpeedAttr.currentValue

    open fun setMovementSpeed(value: Float, fit: Boolean = false) {
        moveSpeedAttr.setValue(value, fit)
    }

    override fun saveNBT(): CompoundTag {
        return super.saveNBT().apply {
            setFloat("Health", getHealth())
            setShort("Air", breathTicks.toInt())

            if (effectManager.all().isNotEmpty()) {
                setTag(
                    "ActiveEffects",
                    ListTag(
                        mutableListOf<CompoundTag>().apply {
                            effectManager.all().forEach { (_, instance) ->
                                add(
                                    CompoundTag.create().apply {
                                        setByte("Id", EffectIdMap.toId(instance.effectType))
                                        setByte("Amplifier", Binary.signByte(instance.amplifier))
                                        setInt("Duration", instance.duration)
                                        setByte("Ambient", if (instance.ambient) 1 else 0)
                                        setByte("ShowParticles", if (instance.visible) 1 else 0)
                                    }
                                )
                            }
                        }
                    )
                )
            }
        }
    }

    open fun hasLineOfSight(entity: Entity): Boolean = true

    fun getEffects(): EffectManager = effectManager

    open fun consumeObject(consumable: Consumable): Boolean {
        // TODO: Consumable
        applyConsumptionResult(consumable)
        return true
    }

    open fun applyConsumptionResult(consumable: Consumable) {
        consumable.getAdditionalEffects().forEach {
            effectManager.add(it)
        }
        consumable.onConsume(this)
    }

    open fun jump() {
        if (onGround) {
            motion = motion.withComponents(
                null,
                jumpVelocity,
                null
            )
        }
    }

    override fun fall(fallDistance: Float) {
        val jumpBoost = effectManager.get(VanillaEffects.JUMP_BOOST.effect)
        val damage = ceil(fallDistance - 3 - (jumpBoost?.effectLevel ?: 0))
        if (damage > 0) {
            /** TODO: Implements after implemented EntityDamageEvent
             * val ev = EntityDamageEvent(this, EntityDamageEvent.CAUSE_FALL, damage)
             * attack(ev)
             *
             * broadcastSound(
             *     if (damage > 4) EntityLongFallSound(this) else EntityShortFallSound(this)
             * )
             */
        } else {
            /** TODO: Implements after implemented World methods
             * var fallBlockPos = location.floor()
             * var fallBlock = world.getBlockAt(fallBlockPos)
             * if (fallBlock.getId() == VanillaBlocks.AIR.id) {
             *     fallBlockPos = fallBlockPos.subtract(0, 1, 0)
             *     fallBlock = world.getBlock(fallBlockPos)
             * }
             * if (fallBlock.getId() != VanillaBlocks.AIR.id) {
             *     broadcastSound(EntityLandSound(this, fallBlock))
             * }
             */
        }
    }

    open fun getArmorPoints(): Int {
        var total = 0
        armorInventory.getContents().values.forEach { item ->
            total += item.defensePoints
        }
        return total
    }

    open fun getHighestArmorEnchantmentLevel(enchantment: Enchantment): Int {
        var result = 0
        armorInventory.getContents().values.forEach { item ->
            result = max(result, item.getEnchantmentLevel(enchantment))
        }
        return result
    }

    override fun setOnFire(seconds: Long) {
        super.setOnFire(
            (
                seconds - min(
                    seconds.toDouble(),
                    seconds * getHighestArmorEnchantmentLevel(VanillaEnchantments.PROTECTION.enchantment) * 0.15
                )
                ).toLong()
        )
    }

    open fun applyDamageModifiers(source: Any) {
        // TODO
    }

    open fun applyPostDamageEffects(source: Any) {
        // TODO
    }

    open fun damageItem(item: Durable, durabilityRemoved: Int) {
        item.applyDamage(durabilityRemoved)
    }

    override fun attack(source: Any) {
        // TODO: implement attack()
    }

    open fun doHitAnimation() {
        broadcastAnimation(HurtAnimation(this))
    }

    @JvmOverloads
    open fun knockback(x: Float, y: Float, z: Float, base: Double = 0.4) {
        var f = sqrt(x * x + z * z)
        if (f <= 0) {
            return
        }
        if (Math.random() / RANDOM_MAX > knockbackResistanceAttr.currentValue) {
            f = 1 / f

            var motionX = motion.x / 2
            var motionY = motion.y / 2
            var motionZ = motion.z / 2

            motionX += x * f * base
            motionY += base
            motionZ += z * f * base

            if (motionY > base) {
                motionY = base
            }
            addMotion(motionX, motionY, motionZ)
        }
    }

    override fun onDeath() {
        /** TODO: Implements after implemented EntityDeathEvent
         * $ev = new EntityDeathEvent($this, $this->getDrops(), $this->getXpDropAmount());
         * $ev->call();
         * foreach($ev->getDrops() as $item){
         *     $this->getWorld()->dropItem($this->location, $item);
         * }
         * //TODO: check death conditions (must have been damaged by player < 5 seconds from death)
         * $this->getWorld()->dropExperience($this->location, $ev->getXpDropAmount());
         *
         * $this->startDeathAnimation();
         */
    }

    override fun onDeathUpdate(tickDiff: Long): Boolean {
        if (deadTicks < maxDeadTicks) {
            deadTicks += tickDiff
            if (deadTicks >= maxDeadTicks) {
                endDeathAnimation()
            }
        }
        return deadTicks >= maxDeadTicks
    }

    protected open fun startDeathAnimation() {
        broadcastAnimation(DeathAnimation(this))
    }

    protected open fun endDeathAnimation() {
        despawnFromAll()
    }

    override fun entityBaseTick(tickDiff: Long): Boolean {
        // TODO: Timings.livingEntityBaseTick.startTiming()
        var hasUpdate = super.entityBaseTick(tickDiff)
        if (isAlive()) {
            if (effectManager.tick(tickDiff)) {
                hasUpdate = true
            }
            if (isInsideOfSolid()) {
                hasUpdate = true
                /** TODO: Implements after implemented EntityDamageEvent
                 *  attack(EntityDamageEvent(this, EntityDamageEvent.CAUSE_SUFFOCATION, 1))
                 */
            }
            if (doAirSupplyTick(tickDiff)) {
                hasUpdate = true
            }
        }
        if (attackTime > 0) {
            attackTime -= tickDiff
        }

        // TODO: Timings.livingEntityBaseTick.stopTiming()
        return hasUpdate
    }

    protected open fun doAirSupplyTick(tickDiff: Long): Boolean {
        var ticks = breathTicks
        val oldTicks = ticks
        if (!canBreath()) {
            breathing = false

            val respirationLevel = armorInventory.getHelmet().getEnchantmentLevel(VanillaEnchantments.RESPIRATION.enchantment)
            if (respirationLevel <= 0 || Math.random() <= (1 / respirationLevel + 1)) {
                ticks -= tickDiff
                if (ticks <= -20) {
                    ticks = 0
                    onAirExpired()
                }
            }
        } else if (!isBreathing()) {
            val max = maxBreathTicks
            if (ticks < max) {
                ticks += tickDiff * 5
            }
            if (ticks <= max) {
                ticks = max
                breathing = true
            }
        }
        if (ticks != oldTicks) {
            breathTicks = ticks
        }
        return ticks != oldTicks
    }

    open fun canBreath(): Boolean =
        effectManager.has(VanillaEffects.WATER_BREATHING.effect) ||
            effectManager.has(VanillaEffects.CONDUIT_POWER.effect) ||
            !isUnderWater()

    open fun isBreathing(): Boolean = breathing

    open fun onAirExpired() {
        /** TODO: Implements after implemented EntityDamageEvent
         * attack(EntityDamageEvent(this, EntityDamageEvent.CAUSE_DROWNING, 2))
         */
    }

    open fun getDrops(): List<Item> = listOf()

    open fun getXpDropAmount(): Int = 0

    open suspend fun getLineOfSight(maxDistance: Int, maxLength: Int = 0, transparent: Map<Int, Boolean>): MutableList<Block> {
        var maxDistance = maxDistance
        if (maxDistance > 120) {
            maxDistance = 120
        }

        val blocks: MutableList<Block> = mutableListOf()
        var nextIndex = 0

        VoxelRayTrace.inDirection(
            location.add(0.0, size.eyeHeight!!.toDouble(), 0.0),
            getDirectionVector(),
            maxDistance.toDouble()
        ).forEach {
            /** TODO: Implements after implemented World methods
             * val block = world.getBlockAt(it.x, it.y, it.z)
             * blocks[nextIndex++] = block
             *
             * if (maxLength != 0 && blocks.size > maxLength) {
             *     blocks.removeFirst()
             *     --nextIndex
             * }
             *
             * val id = block.getId()
             *
             * if (transparent.isEmpty()) {
             *     if (id != 0){
             *         return@forEach
             *     }
             * } else {
             *     if (!transparent.containsKey(id)){
             *         return@forEach
             *     }
             * }
             */
        }
        return blocks
    }

    open suspend fun getTargetBlock(maxDistance: Int, transparent: Map<Int, Boolean>): Block? {
        val line = getLineOfSight(maxDistance, 1, transparent)
        if (line.isNotEmpty()) {
            return line.removeFirst()
        }
        return null
    }

    override fun sendSpawnPacket(player: Player) {
        super.sendSpawnPacket(player)
        // TODO: player.networkSession.onMobArmorChange(this)
    }

    override fun syncNetworkData(properties: EntityMetadataCollection) {
        super.syncNetworkData(properties)

        properties.setByte(EntityMetadataProperties.POTION_AMBIENT, if (effectManager.onlyAmbientEffects) 1 else 0)
        properties.setInt(EntityMetadataProperties.POTION_COLOR, Binary.signInt(effectManager.bubbleColor.toARGB()))
        properties.setShort(EntityMetadataProperties.AIR, breathTicks.toInt())
        properties.setShort(EntityMetadataProperties.MAX_AIR, maxBreathTicks.toInt())

        properties.setGenericFlag(EntityMetadataFlags.BREATHING, breathing)
        properties.setGenericFlag(EntityMetadataFlags.SNEAKING, sneaking)
        properties.setGenericFlag(EntityMetadataFlags.SPRINTING, sprinting)
    }

    override fun onDispose() {
        // TODO: armorInventory.removeAllViewers()
        effectManager.effectAddHooks = mutableListOf()
        effectManager.effectRemoveHooks = mutableListOf()
        super.onDispose()
    }

    companion object {
        const val DEFAULT_BREATH_TICKS = 300L
        const val RANDOM_MAX = 2147483647
    }
}
