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

import kotlin.math.max
import kotlin.math.min

class HungerManager(private val entity: Human) {

    private var hungerAttr: Attribute = fetchAttribute(entity, Attribute.Identifier.HUNGER)

    private var saturationAttr: Attribute = fetchAttribute(entity, Attribute.Identifier.SATURATION)

    private var exhauseAttr: Attribute = fetchAttribute(entity, Attribute.Identifier.EXHAUSTION)

    var foodTickTimer: Long = 0

    var enabled: Boolean = false

    fun getFood(): Float = hungerAttr.currentValue

    fun setFood(new: Float) {
        val old = hungerAttr.currentValue
        hungerAttr.setValue(new)

        intArrayOf(17, 6, 0).forEach {
            if ((old > it) != (new > it)) {
                foodTickTimer = 0
                return@forEach
            }
        }
    }

    fun getMaxFood(): Float = hungerAttr.maxValue

    fun addFood(amount: Float) {
        var amount = amount
        amount += hungerAttr.currentValue
        amount = max(min(amount, hungerAttr.maxValue), hungerAttr.minValue)
        setFood(amount)
    }

    fun isHungry(): Boolean = getFood() < getMaxFood()

    fun getSaturation(): Float = saturationAttr.currentValue

    fun setSaturation(saturation: Float) {
        saturationAttr.setValue(saturation)
    }

    fun addSaturation(amount: Float) {
        saturationAttr.setValue(saturationAttr.currentValue + amount, true)
    }

    fun getExhaustion(): Float = exhauseAttr.currentValue

    fun setExhaustion(amount: Float) {
        exhauseAttr.setValue(amount)
    }

    fun exhaust(amount: Float, cause: Int = 0): Float {
        if (!enabled) {
            return 0F
        }
        /*
        TODO:
        val ev = PlayerExhaustEvent(entity, amount, cause)
        ev.call()
        if (ev.isCancelled()) {
        return 0F
        }
         */
        var exhaustion = getExhaustion()
        // TODO: exhaustion += ev.getAmount()
        // TODO: replace exhaustion += amount to ev.getAmount()
        exhaustion += amount
        while (exhaustion >= 4F) {
            exhaustion -= 4F
            var saturation = getSaturation()
            if (saturation > 0) {
                saturation = max(0F, saturation - 1F)
                setSaturation(saturation)
            } else {
                var food = getFood()
                if (food > 0) {
                    food--
                    setFood(max(food, 0F))
                }
            }
        }
        setExhaustion(exhaustion)
        // TODO: return ev.getAmount()
        // TODO: replace return amount to return ev.getAmount()
        return amount
    }

    @JvmOverloads
    fun tick(tickDiff: Long = 1) {
        if (!entity.isAlive() || !enabled) {
            return
        }
        var food = getFood()

        val health = entity.getHealth()
        val difficulty = 1 // TODO: entity.world.getDifficulty()

        foodTickTimer += tickDiff

        if (foodTickTimer >= 80) {
            foodTickTimer = 0
        }

        if (difficulty == 0 && foodTickTimer % 10 == 0L) {
            if (food < getMaxFood()) {
                addFood(1F)
                food = getFood()
            }
            if (foodTickTimer % 20 == 0L && health < entity.maxHealth) {
                // TODO: entity.heal(EntityRegainHealthEvent(entity, 1, EntityRegainHealthEvent.CAUSE_SATURATION)
            }
        }

        if (foodTickTimer == 0L) {
            if (food >= 18) {
                if (health < entity.maxHealth) {
                    // TODO: entity.heal(EntityRegainHealthEvent(entity, 1, EntityRegainHealthEvent.CAUSE_SATURATION)
                    exhaust(3F) // TODO: add parameter for cause
                }
            } else if (food <= 0) {
                if ((difficulty in 1..3) && entity.getHealth() > 10) {
                    // TODO: entity.attack(EntityDamageEvent(entity, EntityDamageEVent.STARVATION, 1))
                }
            }
        }
        if (food <= 6) {
            entity.sprinting = false
        }
    }

    companion object {
        @JvmStatic
        private fun fetchAttribute(entity: Human, attribute: Attribute.Identifier): Attribute {
            val attr = AttributeFactory.mustGet(attribute)
            entity.attributeMap.add(attr)
            return attr
        }
    }
}
