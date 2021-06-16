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

import be.zvz.kookie.data.bedrock.EffectIdMap
import be.zvz.kookie.entity.effect.EffectInstance
import be.zvz.kookie.entity.effect.EffectManager
import be.zvz.kookie.entity.effect.VanillaEffects
import be.zvz.kookie.inventory.ArmorInventory
import be.zvz.kookie.inventory.CallbackInventoryListener
import be.zvz.kookie.item.Durable
import be.zvz.kookie.item.enchantment.Enchantment
import be.zvz.kookie.item.enchantment.VanillaEnchantments
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.FloatTag
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.utils.Binary
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

abstract class Living(location: Location) : Entity(location) {
    override val gravity: Float = 0.08F
    override val drag: Float = 0.02F

    var attackTime: Int = 0

    var deadTicks: Long = 0

    protected var maxDeadTicks: Long = 25

    var jumpVelocity: Double = 0.42
        get() {
            val jumpBoost = effectManager.get(VanillaEffects.JUMP_BOOST.effect)
            return field + (jumpBoost?.effectLevel ?: 0) / 10
        }
        protected set

    lateinit var effectManager: EffectManager

    lateinit var armorInventory: ArmorInventory

    protected var breathing: Boolean = true
    protected var breathTicks: Int = DEFAULT_BREATH_TICKS
    protected var maxBreathTicks: Int = DEFAULT_BREATH_TICKS

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
                getViewers().forEach { (_, it) ->
                    // TODO: it.session.onMobArmorChange(this)
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
            // TODO: broadcastAnimation(RespawnAnimation(this))
        }
    }

    override var maxHealth: Int
        set(amount) {
            healthAttr.maxValue = amount.toFloat()
            healthAttr.defaultValue = amount.toFloat()
        }
        get() = healthAttr.maxValue.toInt()

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

    open override fun saveNBT(): CompoundTag {
        return super.saveNBT().apply {
            setFloat("Health", getHealth())
            setShort("Air", breathTicks)

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

    open fun consumeObject(consumable: Any): Boolean {
        // TODO: Consumable
        applyConsumptionResult(consumable)
        return true
    }

    open fun applyConsumptionResult(consumable: Any) {
        /*
        TODO:
        consumable.getAdditionalEffects().forEach {
        effectManager.add(it)
        }
         */
        // TODO: consumable.onConsume(this)
    }

    fun jump() {
        if (onGround) {
            motion = motion.withComponents(
                null,
                jumpVelocity,
                null
            )
        }
    }

    override fun fall(fallDistance: Float) {
        /*
        $damage = ceil($fallDistance - 3 - (($jumpBoost = $this->effectManager->get(VanillaEffects::JUMP_BOOST())) !== null ? $jumpBoost->getEffectLevel() : 0));
		if($damage > 0){
			$ev = new EntityDamageEvent($this, EntityDamageEvent::CAUSE_FALL, $damage);
			$this->attack($ev);

			$this->broadcastSound($damage > 4 ?
				new EntityLongFallSound($this) :
				new EntityShortFallSound($this)
			);
		}else{
			$fallBlockPos = $this->location->floor();
			$fallBlock = $this->getWorld()->getBlock($fallBlockPos);
			if($fallBlock->getId() === BlockLegacyIds::AIR){
				$fallBlockPos = $fallBlockPos->subtract(0, 1, 0);
				$fallBlock = $this->getWorld()->getBlock($fallBlockPos);
			}
			if($fallBlock->getId() !== BlockLegacyIds::AIR){
				$this->broadcastSound(new EntityLandSound($this, $fallBlock));
			}
		}
         */
        val jumpBoost = effectManager.get(VanillaEffects.JUMP_BOOST.effect)
        val damage = ceil(fallDistance - 3 - (jumpBoost?.effectLevel ?: 0))
        if (damage > 0) {
            /*
            TODO:
            val ev = ENtityDamageEvent(this, EntityDamageEvent.CAUSE_FALL, damage)
            attack(ev)

            broadcastSound(
                if (damage > 4) EntityLongFallSound(this) else EntityShortFallSound(this)
            )
             */
        } else {
            /*
            var fallBlockPos = location.floor()
            var fallBlock = world.getBlockAt(fallBlockPos)
            if (fallBlock.getId() == BlockLegacyIds.AIR.id) {
                fallBlockPos = fallBlockPos.subtract(0, 1, 0)
                fallBlock = world.getBlock(fallBlockPos)
            }
            if (fallBlock.getId() != BlockLegacyIds.AIR.id) {
                // TODO: broadcastSound(EntityLandSound(this, fallBlock))
            }
            
             */
        }
    }

    fun getArmorPoints(): Int {
        var total = 0
        armorInventory.getContents().values.forEach { item ->
            // total += item.getDefensePoints()
        }
        return total
    }

    fun getHighestArmorEnchantmentLevel(enchantment: Enchantment): Int {
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

    fun applyDamageModifiers(source: Any) {
        // TODO
    }

    fun applyPostDamageEffects(source: Any) {
        // TODO
    }

    fun damageItem(item: Durable, durabilityRemoved: Int) {
        item.applyDamage(durabilityRemoved)
    }

    companion object {
        const val DEFAULT_BREATH_TICKS = 300
    }
}
